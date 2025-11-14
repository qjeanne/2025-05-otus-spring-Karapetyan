(async function () {
    const ul = document.getElementById('authors');
    const authors = await api('/api/authors');
    if (!authors || authors.length === 0) {
        ul.innerHTML = '<li>No authors</li>';
        return;
    }
    ul.innerHTML = authors.map(a => `<li>${escapeHtml(a.fullName)}</li>`).join('');
    function escapeHtml(s) {
        if (s === undefined || s === null) return '';
        return String(s)
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;");
    }
})();
