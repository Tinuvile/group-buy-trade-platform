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

function openPay() {
    document.getElementById("payMask").style.display = "flex";
}

function closePay() {
    document.getElementById("payMask").style.display = "none";
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
        alert('登录成功！');
        closeLogin();
        // 这里可以添加登录成功后的逻辑
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

// 点击遮罩区域关闭弹窗
document.addEventListener('DOMContentLoaded', function() {
    const loginMask = document.getElementById('loginMask');
    if (loginMask) {
        loginMask.addEventListener('click', function(event) {
            // 只有点击遮罩本身（不是登录框）时才关闭弹窗
            if (event.target === this) {
                closeLogin();
            }
        });
    }
    
    const payMask = document.getElementById('payMask');
    if (payMask) {
        payMask.addEventListener('click', function(event) {
            // 只有点击遮罩本身（不是支付框）时才关闭弹窗
            if (event.target === this) {
                closePay();
            }
        });
    }
});