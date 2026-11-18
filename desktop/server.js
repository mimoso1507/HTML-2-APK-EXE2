// Servidor local de la interfaz. Uso: node server.js  ->  http://localhost:3000
const http = require('http'), fs = require('fs'), path = require('path');
const { generate } = require('./generator');
const OUT = path.join(__dirname, 'salida');
const PORT = process.env.PORT || 3000;
http.createServer((req, res) => {
  if (req.method === 'GET' && (req.url === '/' || req.url === '/index.html')) {
    res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
    return res.end(fs.readFileSync(path.join(__dirname, 'ui', 'index.html')));
  }
  if (req.method === 'POST' && req.url === '/api/generate') {
    const chunks = []; req.on('data', c => chunks.push(c));
    req.on('end', () => {
      try {
        const r = generate(JSON.parse(Buffer.concat(chunks).toString()), OUT);
        res.writeHead(200, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ ok: true, dir: r.dir, projects: r.projects }));
      } catch (e) { res.writeHead(400, { 'Content-Type': 'application/json' }); res.end(JSON.stringify({ ok: false, error: e.message })); }
    });
    return;
  }
  res.writeHead(404); res.end('No encontrado');
}).listen(PORT, '127.0.0.1', () => console.log('Convertidor listo en http://localhost:' + PORT));
