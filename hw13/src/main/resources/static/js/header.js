(async function loadHeader() {
    try {
        const resp = await fetch('/header.html');
        const html = await resp.text();
        const container = document.getElementById('header-target');
        if (container) container.innerHTML = html;
    } catch (e) {
        console.error('Cannot load header', e);
    }
})();
