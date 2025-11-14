(async function () {
    const params = new URLSearchParams(location.search);
    const bookId = params.get('id');
    if (!bookId) {
        window.location = '/books.html';
        return;
    }

    const titleInput = document.getElementById('title');
    const authorSelect = document.getElementById('authorId');
    const genresSelect = document.getElementById('genresIds');
    const form = document.getElementById('bookForm');
    const cancelBtn = document.getElementById('cancelBtn');

    cancelBtn.addEventListener('click', () => location.href = '/books.html');

    const [authors, genres, book] = await Promise.all([
        api('/api/authors'),
        api('/api/genres'),
        api(`/api/books/${bookId}`)
    ]);

    if (authors) {
        authorSelect.innerHTML = authors.map(a => `<option value="${a.id}">${escapeHtml(a.fullName)}</option>`).join('');
    }
    if (genres) {
        genresSelect.innerHTML = genres.map(g => `<option value="${g.id}">${escapeHtml(g.name)}</option>`).join('');
    }

    if (book) {
        titleInput.value = book.title || '';
        if (book.author) authorSelect.value = book.author.id;
        const selectedIds = (book.genres || []).map(g => String(g.id));
        Array.from(genresSelect.options).forEach(opt => {
            opt.selected = selectedIds.includes(opt.value);
        });
    }

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const title = titleInput.value.trim();
        const authorId = Number(authorSelect.value);
        const genresIds = Array.from(genresSelect.selectedOptions).map(o => Number(o.value));

        const result = await api(`/api/books/${bookId}`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ title, authorId, genresIds })
        });
        if (result === false) return;

        location.href = '/books.html';
    });

    const commentsTbody = document.querySelector('#comments-table tbody');
    const addCommentForm = document.getElementById('addCommentForm');
    const commentTextInput = document.getElementById('commentText');

    async function loadComments() {
        const comments = await api(`/api/books/${bookId}/comments`);
        commentsTbody.innerHTML = '';

        if (!comments || comments.length === 0) {
            commentsTbody.innerHTML = '<tr><td colspan="3">No comments</td></tr>';
            return;
        }

        comments.forEach(c => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${escapeHtml(c.id)}</td>
                <td>
                  <input type="text" data-id="${c.id}" class="comment-input" value="${escapeHtml(c.text)}" />
                </td>
                <td>
                  <button data-id="${c.id}" class="save-comment">Save</button>
                  <button data-id="${c.id}" class="delete-comment">Delete</button>
                </td>
            `;
            commentsTbody.appendChild(tr);
        });

        commentsTbody.querySelectorAll('.save-comment').forEach(btn => {
            btn.addEventListener('click', async (e) => {
                const id = e.currentTarget.getAttribute('data-id');
                const input = commentsTbody.querySelector(`input.comment-input[data-id="${id}"]`);
                const text = input.value.trim();
                const formData = new URLSearchParams();
                formData.append('text', text);
                await api(`/api/books/${bookId}/comments/${id}`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: formData
                });
                await loadComments();
            });
        });

        commentsTbody.querySelectorAll('.delete-comment').forEach(btn => {
            btn.addEventListener('click', async (e) => {
                const id = e.currentTarget.getAttribute('data-id');
                if (!confirm('Are you sure?')) return;
                await api(`/api/books/${bookId}/comments/${id}`, { method: 'DELETE' });
                await loadComments();
            });
        });
    }

    addCommentForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const text = commentTextInput.value.trim();
        const formData = new URLSearchParams();
        formData.append('text', text);

        if (!text) return;
        await api(`/api/books/${bookId}/comments`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: formData
        });
        commentTextInput.value = '';
        await loadComments();
    });

    await loadComments();

    function escapeHtml(s) {
        if (s === undefined || s === null) return '';
        return String(s)
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;");
    }
})();
