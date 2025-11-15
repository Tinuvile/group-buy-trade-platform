/* ===== 登录页面JavaScript ===== */

// 页面加载完成后显示登录弹窗
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById("loginMask").style.display = "flex";
});

// 处理登录逻辑
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
        // 登录成功后跳转到首页
        setTimeout(() => {
            window.location.href = './index.html';
        }, 1000);
    } else {
        alert('用户名或密码错误！');
        // 清空密码输入框
        document.getElementById('password').value = '';
        document.getElementById('password').focus();
    }
}

// 关闭登录弹窗（返回首页）
function closeLogin() {
    window.location.href = './index.html';
}

// 键盘事件监听
document.addEventListener('keydown', function(event) {
    // 按ESC键返回首页
    if (event.key === 'Escape') {
        closeLogin();
    }
    
    // 按Enter键提交登录
    if (event.key === 'Enter') {
        handleLogin();
    }
});

// 点击遮罩区域返回首页
document.getElementById('loginMask').addEventListener('click', function(event) {
    // 只有点击遮罩本身（不是登录框）时才返回首页
    if (event.target === this) {
        closeLogin();
    }
});

// 表单验证辅助函数
function validateForm() {
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();
    const loginButton = document.querySelector('.login-box button');
    
    // 当用户名和密码都不为空时启用登录按钮
    if (username && password) {
        loginButton.style.opacity = '1';
        loginButton.disabled = false;
    } else {
        loginButton.style.opacity = '0.6';
        loginButton.disabled = true;
    }
}

// 为输入框添加实时验证
document.addEventListener('DOMContentLoaded', function() {
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');
    
    if (usernameInput && passwordInput) {
        usernameInput.addEventListener('input', validateForm);
        passwordInput.addEventListener('input', validateForm);
        
        // 初始状态禁用登录按钮
        validateForm();
    }
});
