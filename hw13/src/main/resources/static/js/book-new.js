(async function () {
    const form = document.getElementById("bookForm");
    const authorSelect = document.getElementById("authorId");
    const genresSelect = document.getElementById("genresIds");
    const cancelBtn = document.getElementById("cancelBtn");

    cancelBtn.addEventListener('click', () => location.href = '/books.html');

    const [authors, genres] = await Promise.all([
        api('/api/authors'),
        api('/api/genres')
    ]);

    if (authors && Array.isArray(authors)) {
        authorSelect.innerHTML = authors.map(a => `<option value="${a.id}">${escapeHtml(a.fullName)}</option>`).join('');
    }
    if (genres && Array.isArray(genres)) {
        genresSelect.innerHTML = genres.map(g => `<option value="${g.id}">${escapeHtml(g.name)}</option>`).join('');
    }

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const title = document.getElementById('title').value.trim();
        const authorId = Number(authorSelect.value);
        const genresIds = Array.from(genresSelect.selectedOptions).map(o => Number(o.value));

        const result = await api('/api/books', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ title, authorId, genresIds })
        });
        if (result === false) return;

        location.href = '/books.html';
    });

    function escapeHtml(s) {
        if (s === undefined || s === null) return '';
        return String(s)
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;");
    }
})();
