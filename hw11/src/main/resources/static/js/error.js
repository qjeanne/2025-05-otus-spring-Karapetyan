(function () {
    const params = new URLSearchParams(location.search);
    const msg = params.get("msg") || "Something went wrong";
    const el = document.getElementById('msg');
    if (el) el.textContent = decodeURIComponent(msg);
})();
