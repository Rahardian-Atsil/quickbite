const API = '/api/v1/checkout';

let menus = [];
const qty = {}; // { idMenu: jumlah }

const $ = (id) => document.getElementById(id);

function rupiah(n) {
    return 'Rp ' + Number(n).toLocaleString('id-ID');
}

function esc(text) {
    const d = document.createElement('div');
    d.textContent = text;
    return d.innerHTML;
}

/* ---------- Menu ---------- */

async function loadMenus() {
    try {
        const res = await fetch(API + '/menus');
        const json = await res.json();
        menus = json.data || [];
        menus.forEach((m) => {
            if (qty[m.idMenu] === undefined) qty[m.idMenu] = 0;
            if (qty[m.idMenu] > m.stok) qty[m.idMenu] = m.stok;
        });
        renderMenus();
        renderSummary();
    } catch (e) {
        $('menuList').innerHTML = '<li class="muted">Gagal memuat menu dari server.</li>';
    }
}

function renderMenus() {
    const list = $('menuList');
    if (menus.length === 0) {
        list.innerHTML = '<li class="muted">Belum ada menu.</li>';
        return;
    }
    list.innerHTML = menus.map((m) => `
        <li class="menu-item">
            <div>
                <div class="menu-name">${esc(m.namaMenu)}</div>
                <div class="menu-price">${rupiah(m.harga)}</div>
                <div class="menu-meta ${m.stok <= 3 ? 'stock-low' : ''}">
                    ${esc(m.idMenu)} &middot; Stok ${m.stok}
                </div>
            </div>
            <div class="stepper">
                <button type="button" data-act="minus" data-id="${esc(m.idMenu)}">&minus;</button>
                <input type="number" min="0" value="${qty[m.idMenu]}" data-id="${esc(m.idMenu)}" aria-label="Jumlah ${esc(m.namaMenu)}">
                <button type="button" data-act="plus" data-id="${esc(m.idMenu)}">+</button>
            </div>
        </li>
    `).join('');
}

function setQty(id, value) {
    const v = parseInt(value, 10);
    qty[id] = isNaN(v) || v < 0 ? 0 : v;
    renderSummary();
}

$('menuList').addEventListener('click', (e) => {
    const btn = e.target.closest('button[data-act]');
    if (!btn) return;
    const id = btn.dataset.id;
    const delta = btn.dataset.act === 'plus' ? 1 : -1;
    setQty(id, (qty[id] || 0) + delta);
    const input = $('menuList').querySelector(`input[data-id="${id}"]`);
    if (input) input.value = qty[id];
});

$('menuList').addEventListener('input', (e) => {
    if (e.target.matches('input[data-id]')) {
        setQty(e.target.dataset.id, e.target.value);
    }
});

/* ---------- Ringkasan ---------- */

function renderSummary() {
    const chosen = menus.filter((m) => qty[m.idMenu] > 0);
    const list = $('summaryList');

    if (chosen.length === 0) {
        list.innerHTML = '<li class="muted">Belum ada item dipilih.</li>';
        $('totalText').textContent = rupiah(0);
        return;
    }

    let total = 0;
    list.innerHTML = chosen.map((m) => {
        const sub = m.harga * qty[m.idMenu];
        total += sub;
        return `<li><span>${esc(m.namaMenu)} x ${qty[m.idMenu]}</span><span>${rupiah(sub)}</span></li>`;
    }).join('');
    $('totalText').textContent = rupiah(total);
}

/* ---------- Checkout ---------- */

function hideResult() {
    $('checkoutError').classList.add('hidden');
    $('receipt').classList.add('hidden');
}

function showError(message, errors) {
    const box = $('checkoutError');
    let html = esc(message || 'Terjadi kesalahan.');
    if (errors && typeof errors === 'object') {
        const items = Object.values(errors).map((v) => `<li>${esc(v)}</li>`).join('');
        if (items) html += `<ul>${items}</ul>`;
    }
    box.innerHTML = html;
    box.classList.remove('hidden');
}

function showReceipt(d) {
    $('rcOrder').textContent = d.idPesanan;
    $('rcPayment').textContent = d.idPembayaran;
    $('rcRef').textContent = d.kodeReferensi;
    $('rcDate').textContent = new Date(d.tanggal).toLocaleString('id-ID');
    $('rcStatus').textContent = d.statusPesanan;
    $('rcTotal').textContent = rupiah(d.totalBiaya);
    $('receipt').classList.remove('hidden');
    $('batalIdPesanan').value = d.idPesanan;
}

function setRaw(status, data) {
    $('rawResponse').textContent = 'HTTP ' + status + '\n' + JSON.stringify(data, null, 2);
}

async function kirimCheckout() {
    hideResult();

    const keranjangData = menus
        .filter((m) => qty[m.idMenu] > 0)
        .map((m) => ({ idMenu: m.idMenu, jumlah: qty[m.idMenu] }));

    const payload = {
        idPelanggan: $('idPelanggan').value.trim(),
        metodePembayaran: $('metodePembayaran').value,
        tokenKartu: $('tokenKartu').value.trim(),
        keranjangData: keranjangData
    };

    const btn = $('btnCheckout');
    btn.disabled = true;
    btn.textContent = 'Memproses...';

    try {
        const res = await fetch(API, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const data = await res.json();
        setRaw(res.status, data);

        if (res.ok && data.success) {
            showReceipt(data.data);
            keranjangData.forEach((i) => { qty[i.idMenu] = 0; });
        } else {
            showError(data.message, data.errors);
        }
    } catch (err) {
        showError('Tidak dapat terhubung ke server.');
    } finally {
        btn.disabled = false;
        btn.textContent = 'Bayar Sekarang';
        loadMenus();
    }
}

/* ---------- Pembatalan ---------- */

async function kirimBatal() {
    const box = $('batalResult');
    const idPesanan = $('batalIdPesanan').value.trim();
    box.classList.add('hidden');

    if (!idPesanan) {
        box.className = 'alert alert-error';
        box.textContent = 'ID pesanan harus diisi.';
        return;
    }

    try {
        const res = await fetch(`${API}/${encodeURIComponent(idPesanan)}/batal`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ alasan: $('batalAlasan').value.trim() })
        });
        const data = await res.json();
        setRaw(res.status, data);

        if (res.ok && data.success) {
            box.className = 'alert alert-success';
            box.textContent = 'Pesanan ' + idPesanan + ' berhasil dibatalkan. Stok telah dikembalikan.';
        } else {
            box.className = 'alert alert-error';
            box.textContent = data.message || 'Gagal membatalkan pesanan.';
        }
    } catch (err) {
        box.className = 'alert alert-error';
        box.textContent = 'Tidak dapat terhubung ke server.';
    }
    loadMenus();
}

/* ---------- Skenario uji ---------- */

function isiPreset(no) {
    hideResult();
    $('idPelanggan').value = 'CUST-001';
    $('metodePembayaran').value = 'CREDIT_CARD';
    $('tokenKartu').value = no === 3 ? 'invalid_card' : 'tok_visa_valid_999';

    menus.forEach((m) => { qty[m.idMenu] = 0; });
    if (menus.length >= 2) {
        if (no === 1) { qty[menus[0].idMenu] = 2; qty[menus[1].idMenu] = 1; }
        if (no === 2) { qty[menus[1].idMenu] = 100; }
        if (no === 3) { qty[menus[0].idMenu] = 1; }
    }
    renderMenus();
    renderSummary();
}

document.querySelectorAll('[data-preset]').forEach((b) => {
    b.addEventListener('click', () => isiPreset(parseInt(b.dataset.preset, 10)));
});

/* ---------- Init ---------- */

$('btnCheckout').addEventListener('click', kirimCheckout);
$('btnBatal').addEventListener('click', kirimBatal);
$('btnRefresh').addEventListener('click', loadMenus);
document.addEventListener('DOMContentLoaded', loadMenus);