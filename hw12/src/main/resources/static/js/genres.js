(async function () {
    const ul = document.getElementById('genres');
    const genres = await api('/api/genres');
    if (!genres || genres.length === 0) {
        ul.innerHTML = '<li>No genres</li>';
        return;
    }
    ul.innerHTML = genres.map(g => `<li>${escapeHtml(g.name)}</li>`).join('');
    function escapeHtml(s) {
        if (s === undefined || s === null) return '';
        return String(s)
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;");
    }
})();
