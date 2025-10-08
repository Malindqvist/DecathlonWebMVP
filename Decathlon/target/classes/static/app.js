const el = (id) => document.getElementById(id);
const err = el('error');
const msg = el('msg');

function setError(text) { err.textContent = text; }
function setMsg(text) { msg.textContent = text; }

const MODES = {
  decathlon: {
    events: [
      { key: '100m',        label: '100m' },
      { key: 'longJump',    label: 'Long Jump' },
      { key: 'shotPut',     label: 'Shot Put' },
      { key: 'highJump',    label: 'High Jump' },
      { key: '400m',        label: '400m' },
      { key: '110mHurdles', label: '110m Hurdles' },
      { key: 'discus',      label: 'Discus Throw' },
      { key: 'poleVault',   label: 'Pole Vault' },
      { key: 'javelin',     label: 'Javelin Throw' },
      { key: '1500m',       label: '1500m' }
    ]
  },
  heptathlon: {
    events: [
      { key: '100mHurdles', label: '100m Hurdles' },
      { key: 'highJump_w',    label: 'High Jump' },
      { key: 'shotPut_w',     label: 'Shot Put' },
      { key: '200m',        label: '200m' },
      { key: 'longJump_w',    label: 'Long Jump' },
      { key: 'javelin_w',     label: 'Javelin' },
      { key: '800m',        label: '800m' }
    ]
  }
};

let currentMode = el('mode')?.value || 'decathlon';
let sortBroken = false;

function escapeHtml(s){
  return String(s).replace(/[&<>"]/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;'}[c]));
}

function populateEventSelect() {
  const sel = el('event');
  sel.innerHTML = '';
  MODES[currentMode].events.forEach(ev => {
    const o = document.createElement('option');
    o.value = ev.key;
    o.textContent = ev.label;
    sel.appendChild(o);
  });
}

function getTheadRow() {
  let tr = document.getElementById('thead-row');
  if (!tr) {
    const thead = document.querySelector('table thead') || document.createElement('thead');
    tr = document.createElement('tr');
    thead.innerHTML = '';
    thead.appendChild(tr);
    (document.querySelector('table') || document.createElement('table')).appendChild(thead);
  }
  return tr;
}

function renderHeader() {
  const tr = getTheadRow();
  const cols = ['Name', ...MODES[currentMode].events.map(e => e.label), 'Total'];
  tr.id = 'thead-row';
  tr.innerHTML = cols.map(c => `<th>${escapeHtml(c)}</th>`).join('');
}

async function renderStandings() {
  try {
    const res = await fetch('/api/standings');
    const data = await res.json();
    const events = MODES[currentMode].events;

    const rows = (sortBroken ? data : data.sort((a,b)=> (b.total||0)-(a.total||0)))
      .map(r => {
        const tds = [
          `<td>${escapeHtml(r.name)}</td>`,
          ...events.map(e => `<td>${r.scores?.[e.key] ?? ''}</td>`),
          `<td>${r.total ?? 0}</td>`
        ];
        return `<tr>${tds.join('')}</tr>`;
      }).join('');

    el('standings').innerHTML = rows;
  } catch {
    setError('Could not load standings');
  }
}

el('mode').addEventListener('change', async (e) => {
  currentMode = e.target.value;
  populateEventSelect();
  renderHeader();
  await renderStandings();
});

el('add').addEventListener('click', async (evt) => {
  evt?.preventDefault?.();
  const name = el('name').value;
  try {
    const res = await fetch('/api/competitors', {
      method: 'POST', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name })
    });
    if (!res.ok) {
      const t = await res.text();
      setError(t || `Failed to add competitor (status ${res.status})`);
    } else {
      setMsg('Added');
    }
    await renderStandings();
  } catch {
    setError('Network error');
  }
});

el('save').addEventListener('click', async () => {
  const rawValue = parseFloat(el('raw').value);
  if (isNaN(rawValue) || rawValue < 0) { setError('Negative result is not possible.'); return; }

  const body = { name: el('name2').value, event: el('event').value, raw: rawValue };
  try {
    const res = await fetch('/api/score', {
      method: 'POST', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    });
    const json = await res.json();
    setMsg(`Saved: ${json.points} pts`);
    await renderStandings();
  } catch {
    setError('Score failed');
  }
});

el('export').addEventListener('click', async () => {
  try {
    const res = await fetch('/api/export.csv');
    const text = await res.text();
    const blob = new Blob([text], { type: 'text/csv;charset=utf-8' });
    const a = document.createElement('a');
    a.href = URL.createObjectURL(blob);
    a.download = 'results.csv';
    a.click();
    sortBroken = true;
  } catch {
    setError('Export failed');
  }
});

// init
populateEventSelect();
renderHeader();
renderStandings();
