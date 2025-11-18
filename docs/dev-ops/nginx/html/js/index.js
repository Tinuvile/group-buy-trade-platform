/* ===== 全局变量 ===== */
let currentGoodsData = null;
let currentGoodsId = 'default_goods_001'; // 默认商品ID

/* ===== 轮播图自动播放 ===== */
let index = 0;
setInterval(() => {
    index = (index + 1) % 3;
    document.getElementById("swiperTrack").style.transform = `translateX(-${index * 33.33}%)`;
    document.querySelectorAll(".swiper-dots span").forEach((d, i) => {
        d.classList.toggle("active", i === index);
    });
}, 3000);

/* ===== 弹窗控制 ===== */
function goToLogin() {
    // 显示登录弹窗而不是跳转
    document.getElementById("loginMask").style.display = "flex";
}

function openLogin() {
    document.getElementById("loginMask").style.display = "flex";
}

function closeLogin() {
    document.getElementById("loginMask").style.display = "none";
    // 清空表单
    document.getElementById('username').value = '';
    document.getElementById('password').value = '';
}

function openPay(type = 'single') {
    if (!currentGoodsData || !currentGoodsData.goods) {
        alert('商品信息加载中，请稍后再试');
        return;
    }
    
    // 根据购买类型设置支付金额
    const payAmount = currentGoodsData.goods.payPrice;
    const payAmountElement = document.querySelector('.pay-box div');
    if (payAmountElement) {
        payAmountElement.textContent = `支付金额 ${utils.formatPrice(payAmount)}`;
    }
    
    document.getElementById("payMask").style.display = "flex";
}

function closePay() {
    document.getElementById("payMask").style.display = "none";
}

/* ===== 拼团和购买逻辑 ===== */
/**
 * 加入拼团
 */
async function joinTeam(teamId, activityId) {
    try {
        // 如果是临时用户，先要求登录
        if (userSession.isTempUser()) {
            alert('请先登录后再参与拼团');
            goToLogin();
            return;
        }
        
        // 执行支付流程
        await processPayment(teamId, activityId);
        
    } catch (error) {
        console.error('加入拼团失败:', error);
        alert('加入拼团失败，请重试');
    }
}

/**
 * 开团购买
 */
async function startGroupBuy() {
    try {
        // 如果是临时用户，先要求登录
        if (userSession.isTempUser()) {
            alert('请先登录后再开团');
            goToLogin();
            return;
        }
        
        // 开团逻辑（可以创建新团队）
        await processPayment(null, null, 'start_group');
        
    } catch (error) {
        console.error('开团失败:', error);
        alert('开团失败，请重试');
    }
}

/**
 * 处理支付流程
 */
async function processPayment(teamId = null, activityId = null, type = 'join_group') {
    try {
        const requestParams = {
            source: 'web',
            channel: 'direct',
            userId: userSession.getUserId(),
            outTradeNo: utils.generateOutTradeNo(),
            outTradeTime: new Date()
        };
        
        // 使用重试机制处理支付
        const response = await utils.retry(async () => {
            utils.showLoading(true, '正在处理支付信息...');
            return await apiClient.settlementMarketPayOrder(requestParams);
        });
        
        // 显示支付弹窗
        openPay(type);
        
        // 保存支付信息供后续使用
        window.currentPaymentInfo = {
            ...response,
            teamId: teamId,
            activityId: activityId,
            type: type
        };
        
        utils.showSuccess('支付信息生成成功，请完成支付');
        
    } catch (error) {
        console.error('支付处理失败:', error);
        utils.showError(`支付处理失败: ${error.message}`);
        throw error;
    } finally {
        utils.showLoading(false);
    }
}

/**
 * 支付完成处理
 */
function handlePaymentSuccess() {
    utils.showSuccess('支付成功！感谢您的购买');
    closePay();
    
    // 重新加载数据以刷新拼团状态
    setTimeout(() => {
        loadGoodsMarketData();
    }, 1000);
}

/**
 * 网络状态监控
 */
function initNetworkMonitoring() {
    // 监听网络状态变化
    window.addEventListener('online', () => {
        utils.showSuccess('网络连接已恢复');
        // 重新加载数据
        loadGoodsMarketData();
    });
    
    window.addEventListener('offline', () => {
        utils.showError('网络连接已断开，部分功能可能无法使用');
    });
}

/**
 * 刷新数据
 */
function refreshData() {
    loadGoodsMarketData();
}

/**
 * 添加刷新按钮
 */
function addRefreshButton() {
    // 检查是否已经添加过刷新按钮
    if (document.getElementById('refresh-btn')) return;
    
    const refreshBtn = document.createElement('button');
    refreshBtn.id = 'refresh-btn';
    refreshBtn.textContent = '刷新';
    refreshBtn.style.cssText = `
        position: fixed;
        top: 10px;
        right: 10px;
        background: #007bff;
        color: white;
        border: none;
        padding: 8px 16px;
        border-radius: 4px;
        cursor: pointer;
        z-index: 1000;
        font-size: 14px;
    `;
    refreshBtn.onclick = refreshData;
    
    document.body.appendChild(refreshBtn);
}

/* ===== 数据加载和显示逻辑 ===== */
async function loadGoodsMarketData() {
    try {
        const requestParams = {
            userId: userSession.getUserId(),
            source: 'web',
            channel: 'direct',
            goodsId: currentGoodsId
        };
        
        // 使用重试机制加载数据
        const response = await utils.retry(async () => {
            utils.showLoading(true, '正在加载商品信息...');
            return await apiClient.getGoodsMarketInfo(requestParams);
        });
        
        currentGoodsData = response;
        
        // 更新页面数据
        updateGoodsInfo(response.goods);
        updateTeamCards(response.teamList);
        updateTeamStatistics(response.teamStatistic);
        
        utils.showSuccess('商品信息加载成功');
        
    } catch (error) {
        console.error('加载商品数据失败:', error);
        utils.showError(`商品数据加载失败: ${error.message}`);
        
        // 显示默认数据以保证页面可用性
        showDefaultData();
    } finally {
        utils.showLoading(false);
    }
}

/**
 * 更新商品信息显示
 */
function updateGoodsInfo(goods) {
    if (!goods) return;
    
    // 更新价格显示
    const priceBig = document.querySelector('.price-big');
    const priceSmall = document.querySelector('.price-small');
    
    if (priceBig) priceBig.textContent = utils.formatPrice(goods.payPrice);
    if (priceSmall) {
        priceSmall.textContent = utils.formatPrice(goods.originalPrice);
        priceSmall.style.textDecoration = 'line-through';
    }
}

/**
 * 更新拼团卡片显示
 */
function updateTeamCards(teamList) {
    if (!teamList || teamList.length === 0) {
        // 显示默认的拼团卡片
        return;
    }
    
    const container = document.querySelector('.container');
    const titleElement = container.querySelector('.title');
    const tagElement = container.querySelector('.tag');
    
    // 找到现有的拼团卡片并删除
    const existingCards = container.querySelectorAll('.group-card');
    existingCards.forEach(card => card.remove());
    
    // 生成新的拼团卡片
    teamList.forEach((team, index) => {
        const cardElement = createTeamCard(team, index);
        // 在tag元素后插入卡片
        tagElement.insertAdjacentElement('afterend', cardElement);
    });
}

/**
 * 创建拼团卡片元素
 */
function createTeamCard(team, index) {
    const card = document.createElement('div');
    card.className = 'group-card';
    card.dataset.teamId = team.teamId;
    card.dataset.activityId = team.activityId;
    
    // 生成随机头像
    const avatarUrl = `https://picsum.photos/50?random=${index + 1}`;
    const countdown = utils.formatCountdown(team.validTimeCountdown);
    const progress = `${team.completeCount}/${team.targetCount}`;
    
    card.innerHTML = `
        <div class="group-left">
            <img class="avatar" src="${avatarUrl}"/>
            <div>拼团进度: ${progress}<br><span style="font-size:12px;color:#999;">${countdown}</span></div>
        </div>
        <button class="right-btn" onclick="joinTeam('${team.teamId}', '${team.activityId}')">立即抢单</button>
    `;
    
    return card;
}

/**
 * 更新拼团统计信息
 */
function updateTeamStatistics(teamStatistic) {
    if (!teamStatistic) return;
    
    // 更新标签信息
    const tagElement = document.querySelector('.tag');
    if (tagElement) {
        tagElement.textContent = `大促优惠 | 已开团${teamStatistic.allTeamCount}个，成团${teamStatistic.allTeamCompleteCount}个，${teamStatistic.allTeamUserCount}人参与`;
    }
}

/**
 * 显示默认数据（当API调用失败时）
 */
function showDefaultData() {
    // 显示默认商品信息
    currentGoodsData = {
        goods: {
            goodsId: currentGoodsId,
            originalPrice: 160,
            payPrice: 100,
            deductionPrice: 60
        },
        teamList: [],
        teamStatistic: {
            allTeamCount: 8,
            allTeamCompleteCount: 5,
            allTeamUserCount: 76
        }
    };
    
    updateGoodsInfo(currentGoodsData.goods);
    updateTeamStatistics(currentGoodsData.teamStatistic);
    
    utils.showWarning('当前显示为演示数据，请检查网络连接');
}

/* ===== 登录处理逻辑 ===== */
function handleLogin() {
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();
    
    // 基础验证
    if (!username) {
        alert('请输入用户名');
        document.getElementById('username').focus();
        return;
    }
    
    if (!password) {
        alert('请输入密码');
        document.getElementById('password').focus();
        return;
    }
    
    // 模拟登录验证（实际项目中应该调用后端API）
    if (username === 'admin' && password === '123456') {
        // 保存用户信息
        userSession.setUserInfo({
            userId: 'user_' + username,
            username: username,
            isTemp: false
        });
        
        alert('登录成功！');
        closeLogin();
        
        // 重新加载数据
        loadGoodsMarketData();
    } else {
        alert('用户名或密码错误！');
        // 清空密码输入框
        document.getElementById('password').value = '';
        document.getElementById('password').focus();
    }
}

// 键盘事件监听（仅在登录弹窗显示时生效）
document.addEventListener('keydown', function(event) {
    const loginMask = document.getElementById('loginMask');
    if (loginMask && loginMask.style.display === 'flex') {
        // 按ESC键关闭登录弹窗
        if (event.key === 'Escape') {
            closeLogin();
        }
        
        // 按Enter键提交登录
        if (event.key === 'Enter') {
            handleLogin();
        }
    }
});

// 页面初始化和事件监听
document.addEventListener('DOMContentLoaded', function() {
    // 初始化网络监控
    initNetworkMonitoring();
    
    // 添加刷新按钮
    addRefreshButton();
    
    // 初始化加载商品数据
    loadGoodsMarketData();
    
    // 登录遮罩点击事件
    const loginMask = document.getElementById('loginMask');
    if (loginMask) {
        loginMask.addEventListener('click', function(event) {
            // 只有点击遮罩本身（不是登录框）时才关闭弹窗
            if (event.target === this) {
                closeLogin();
            }
        });
    }
    
    // 支付遮罩点击事件
    const payMask = document.getElementById('payMask');
    if (payMask) {
        payMask.addEventListener('click', function(event) {
            // 只有点击遮罩本身（不是支付框）时才关闭弹窗
            if (event.target === this) {
                closePay();
            }
        });
    }
    
    // 修改支付完成按钮事件
    const payDoneBtn = document.querySelector('.pay-btns .done');
    if (payDoneBtn) {
        payDoneBtn.onclick = function() {
            handlePaymentSuccess();
        };
    }
});