/**
 * API工具类 - 封装后端接口调用
 * @author Tinuvile
 */
class ApiClient {
    constructor() {
        // 后端API基础URL - 可根据环境配置
        this.baseURL = 'http://localhost:8080/api';
        
        // 默认请求头
        this.defaultHeaders = {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        };
    }

    /**
     * 发送HTTP请求
     * @param {string} url 请求URL
     * @param {Object} options 请求选项
     * @returns {Promise} 响应数据
     */
    async request(url, options = {}) {
        const config = {
            method: 'GET',
            headers: { ...this.defaultHeaders, ...options.headers },
            timeout: 10000, // 10秒超时
            ...options
        };

        try {
            // 添加请求超时控制
            const controller = new AbortController();
            const timeoutId = setTimeout(() => controller.abort(), config.timeout);
            
            const response = await fetch(`${this.baseURL}${url}`, {
                ...config,
                signal: controller.signal
            });
            
            clearTimeout(timeoutId);
            
            if (!response.ok) {
                const errorText = await response.text().catch(() => 'Unknown error');
                throw new Error(`HTTP ${response.status}: ${errorText}`);
            }
            
            const data = await response.json();
            
            // 检查业务响应状态
            if (data.code && data.code !== 200 && data.code !== '200') {
                throw new Error(data.message || '业务处理失败');
            }
            
            return data.data || data;
        } catch (error) {
            console.error('API请求失败:', error);
            
            // 网络错误处理
            if (error.name === 'AbortError') {
                throw new Error('请求超时，请检查网络连接');
            } else if (!navigator.onLine) {
                throw new Error('网络连接已断开，请检查网络');
            } else if (error.message.includes('Failed to fetch')) {
                throw new Error('网络连接失败，请检查服务器状态');
            }
            
            throw error;
        }
    }

    /**
     * GET请求
     * @param {string} url 请求URL
     * @param {Object} params 查询参数
     * @returns {Promise} 响应数据
     */
    async get(url, params = {}) {
        const queryString = new URLSearchParams(params).toString();
        const fullUrl = queryString ? `${url}?${queryString}` : url;
        return this.request(fullUrl);
    }

    /**
     * POST请求
     * @param {string} url 请求URL
     * @param {Object} data 请求体数据
     * @returns {Promise} 响应数据
     */
    async post(url, data = {}) {
        return this.request(url, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    }

    /**
     * 获取商品营销信息
     * @param {Object} params - GoodsMarketRequestDTO参数
     * @param {string} params.userId 用户ID
     * @param {string} params.source 渠道
     * @param {string} params.channel 来源
     * @param {string} params.goodsId 商品ID
     * @returns {Promise<GoodsMarketResponseDTO>} 商品营销信息
     */
    async getGoodsMarketInfo(params) {
        return this.post('/goods/market/info', params);
    }

    /**
     * 结算营销支付订单
     * @param {Object} params - SettlementMarketPayOrderRequestDTO参数
     * @param {string} params.source 渠道
     * @param {string} params.channel 来源
     * @param {string} params.userId 用户ID
     * @param {string} params.outTradeNo 外部交易单号
     * @param {Date} params.outTradeTime 外部交易时间
     * @returns {Promise<SettlementMarketPayOrderResponseDTO>} 结算响应信息
     */
    async settlementMarketPayOrder(params) {
        return this.post('/settlement/market/pay/order', params);
    }
}

/**
 * 用户会话管理类
 */
class UserSession {
    constructor() {
        this.storageKey = 'group_buy_user_session';
    }

    /**
     * 设置用户会话信息
     * @param {Object} userInfo 用户信息
     */
    setUserInfo(userInfo) {
        localStorage.setItem(this.storageKey, JSON.stringify(userInfo));
    }

    /**
     * 获取用户会话信息
     * @returns {Object|null} 用户信息
     */
    getUserInfo() {
        const userInfo = localStorage.getItem(this.storageKey);
        return userInfo ? JSON.parse(userInfo) : null;
    }

    /**
     * 清除用户会话
     */
    clearUserInfo() {
        localStorage.removeItem(this.storageKey);
    }

    /**
     * 生成用户ID（如果用户未登录，生成临时ID）
     * @returns {string} 用户ID
     */
    getUserId() {
        let userInfo = this.getUserInfo();
        if (!userInfo || !userInfo.userId) {
            // 生成临时用户ID
            const tempUserId = 'temp_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
            userInfo = { userId: tempUserId, isTemp: true };
            this.setUserInfo(userInfo);
        }
        return userInfo.userId;
    }

    /**
     * 是否为临时用户
     * @returns {boolean}
     */
    isTempUser() {
        const userInfo = this.getUserInfo();
        return userInfo && userInfo.isTemp === true;
    }
}

/**
 * 工具函数类
 */
class Utils {
    /**
     * 生成唯一的外部交易单号
     * @returns {string} 交易单号
     */
    static generateOutTradeNo() {
        const timestamp = Date.now();
        const random = Math.random().toString(36).substr(2, 9);
        return `GROUP_BUY_${timestamp}_${random}`;
    }

    /**
     * 格式化金额显示
     * @param {number|string} amount 金额
     * @returns {string} 格式化后的金额
     */
    static formatPrice(amount) {
        return `¥${parseFloat(amount).toFixed(2)}`;
    }

    /**
     * 格式化倒计时显示
     * @param {string} timeStr 时间字符串（HH:MM:SS格式）
     * @returns {string} 格式化后的显示文本
     */
    static formatCountdown(timeStr) {
        if (!timeStr || timeStr === '已结束') {
            return '已结束';
        }
        return `拼单即将结束 ${timeStr}`;
    }

    /**
     * 显示加载状态
     * @param {boolean} show 是否显示
     * @param {string} message 加载消息
     */
    static showLoading(show = true, message = '加载中...') {
        const existingLoader = document.getElementById('loading-overlay');
        
        if (show) {
            if (!existingLoader) {
                const loader = document.createElement('div');
                loader.id = 'loading-overlay';
                loader.innerHTML = `
                    <div style="
                        position: fixed;
                        top: 0;
                        left: 0;
                        width: 100%;
                        height: 100%;
                        background: rgba(0,0,0,0.5);
                        display: flex;
                        flex-direction: column;
                        justify-content: center;
                        align-items: center;
                        z-index: 9999;
                        color: white;
                        font-size: 16px;
                    ">
                        <div class="loading-spinner" style="
                            width: 40px;
                            height: 40px;
                            border: 3px solid rgba(255,255,255,0.3);
                            border-top: 3px solid white;
                            border-radius: 50%;
                            animation: spin 1s linear infinite;
                            margin-bottom: 15px;
                        "></div>
                        <div>${message}</div>
                    </div>
                    <style>
                        @keyframes spin {
                            0% { transform: rotate(0deg); }
                            100% { transform: rotate(360deg); }
                        }
                    </style>
                `;
                document.body.appendChild(loader);
            } else {
                // 更新消息
                const messageDiv = existingLoader.querySelector('div div:last-child');
                if (messageDiv) messageDiv.textContent = message;
            }
        } else {
            if (existingLoader) {
                existingLoader.remove();
            }
        }
    }

    /**
     * 显示成功消息
     * @param {string} message 消息内容
     * @param {number} duration 显示时长（毫秒）
     */
    static showSuccess(message, duration = 3000) {
        Utils.showMessage(message, 'success', duration);
    }

    /**
     * 显示错误消息
     * @param {string} message 消息内容
     * @param {number} duration 显示时长（毫秒）
     */
    static showError(message, duration = 5000) {
        Utils.showMessage(message, 'error', duration);
    }

    /**
     * 显示警告消息
     * @param {string} message 消息内容
     * @param {number} duration 显示时长（毫秒）
     */
    static showWarning(message, duration = 4000) {
        Utils.showMessage(message, 'warning', duration);
    }

    /**
     * 显示消息
     * @param {string} message 消息内容
     * @param {string} type 消息类型：success, error, warning, info
     * @param {number} duration 显示时长（毫秒）
     */
    static showMessage(message, type = 'info', duration = 3000) {
        const existingMessage = document.getElementById('toast-message');
        if (existingMessage) {
            existingMessage.remove();
        }

        const colors = {
            success: '#4CAF50',
            error: '#f44336',
            warning: '#ff9800',
            info: '#2196F3'
        };

        const messageDiv = document.createElement('div');
        messageDiv.id = 'toast-message';
        messageDiv.style.cssText = `
            position: fixed;
            top: 20px;
            left: 50%;
            transform: translateX(-50%);
            background: ${colors[type] || colors.info};
            color: white;
            padding: 12px 24px;
            border-radius: 6px;
            z-index: 10000;
            font-size: 14px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.3);
            max-width: 90%;
            text-align: center;
            opacity: 0;
            transition: opacity 0.3s ease;
        `;
        messageDiv.textContent = message;

        document.body.appendChild(messageDiv);

        // 动画显示
        setTimeout(() => {
            messageDiv.style.opacity = '1';
        }, 10);

        // 自动消失
        setTimeout(() => {
            messageDiv.style.opacity = '0';
            setTimeout(() => {
                if (messageDiv.parentNode) {
                    messageDiv.remove();
                }
            }, 300);
        }, duration);
    }

    /**
     * 重试机制
     * @param {Function} asyncFn 异步函数
     * @param {number} maxRetries 最大重试次数
     * @param {number} delay 重试间隔（毫秒）
     */
    static async retry(asyncFn, maxRetries = 3, delay = 1000) {
        let lastError;
        
        for (let i = 0; i <= maxRetries; i++) {
            try {
                return await asyncFn();
            } catch (error) {
                lastError = error;
                
                if (i < maxRetries) {
                    Utils.showWarning(`操作失败，正在重试... (${i + 1}/${maxRetries})`);
                    await new Promise(resolve => setTimeout(resolve, delay));
                    delay *= 1.5; // 指数退避
                }
            }
        }
        
        throw lastError;
    }
}

// 创建全局实例
window.apiClient = new ApiClient();
window.userSession = new UserSession();
window.utils = Utils;
