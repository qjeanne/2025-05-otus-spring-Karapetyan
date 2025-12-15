async function api(url, options = {}) {
    const defaultHeaders = { 'Accept': 'application/json' };
    options.headers = Object.assign({}, defaultHeaders, options.headers || {});

    let response;
    try {
        response = await fetch(url, options);
    } catch (e) {
        const msg = encodeURIComponent(e.message || 'Network error');
        window.location = `/error.html?msg=${msg}`;
        return false;
    }

    if (!response.ok) {
        let details = response.statusText;
        const body = await response.json();
        if (body && (body.details || body.message)) {
            details = body.details || body.message;
        }
        window.location = `/error.html?msg=${encodeURIComponent(details)}`;
        return false;
    }

    if (response.status === 204) return null;

    const ct = response.headers.get('Content-Type') || '';
    if (ct.includes('application/json')) {
        return response.json();
    }
    return response.text();
}
