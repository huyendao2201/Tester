(function () {
    const form = document.getElementById("registerForm");
    const btn = document.getElementById("btnRegister");

    if (!form || !btn) return;

    // Các field bắt buộc theo đặc tả + checkbox điều khoản
    const requiredIds = [
        "fullName",
        "username",
        "email",
        "phone",
        "password",
        "confirmPassword",
        "agreeTerms"
    ];

    function getElByThymeleafName(name) {
        // thymeleaf th:field sẽ tạo id = name
        return document.getElementById(name);
    }

    function isFilled(el) {
        if (!el) return false;
        if (el.type === "checkbox") return el.checked;
        return (el.value || "").trim().length > 0;
    }

    function validateBasicClient() {
        // Chỉ check "trống" cho việc bật/tắt nút theo yêu cầu
        // (server vẫn validate đầy đủ)
        for (const id of requiredIds) {
            const el = getElByThymeleafName(id);
            if (!isFilled(el)) return false;
        }
        return true;
    }

    function updateButton() {
        btn.disabled = !validateBasicClient();
    }

    form.addEventListener("input", updateButton);
    form.addEventListener("change", updateButton);
    document.addEventListener("DOMContentLoaded", updateButton);

    // set initial state
    updateButton();
})();