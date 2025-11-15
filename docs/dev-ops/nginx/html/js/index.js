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
    window.location.href = './login.html';
}

function openPay() {
    document.getElementById("payMask").style.display = "flex";
}

function closePay() {
    document.getElementById("payMask").style.display = "none";
}