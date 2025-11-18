(async function () {
    document.getElementById('newBookBtn').addEventListener('click', () => {
        location.href = '/book-new.html';
    });

    async function loadBooks() {
        const books = await api('/api/books');
        const tbody = document.querySelector("#books-table tbody");
        tbody.innerHTML = "";

        if (!books || books.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5">No books</td></tr>';
            return;
        }

        books.forEach(book => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${escapeHtml(book.id)}</td>
                <td>${escapeHtml(book.title)}</td>
                <td>${escapeHtml(book.author?.fullName ?? '')}</td>
                <td>${(book.genres || []).map(g => escapeHtml(g.name)).join(", ")}</td>
                <td>
                    <button data-id="${book.id}" class="edit-btn">Edit</button>
                    <button data-id="${book.id}" class="delete-btn">Delete</button>
                </td>
            `;
            tbody.appendChild(tr);
        });

        tbody.querySelectorAll('.edit-btn').forEach(b =>
            b.addEventListener('click', e => {
                const id = e.currentTarget.getAttribute('data-id');
                location.href = `/book-edit.html?id=${id}`;
            })
        );

        tbody.querySelectorAll('.delete-btn').forEach(b =>
            b.addEventListener('click', async e => {
                const id = e.currentTarget.getAttribute('data-id');
                if (!confirm('Are you sure?')) return;
                await api(`/api/books/${id}`, { method: 'DELETE' });
                await loadBooks();
            })
        );
    }

    function escapeHtml(s) {
        if (s === undefined || s === null) return '';
        return String(s)
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;");
    }

    await loadBooks();
})();
