#!/usr/bin/env python3
"""
ngrok Agent CLI Quickstart - Servidor HTTP Simples
Expose this app via: ngrok http 8080
"""

from http.server import HTTPServer, SimpleHTTPRequestHandler
from urllib.parse import urlparse, parse_qs
import json
import os

class QuickstartHandler(SimpleHTTPRequestHandler):
    """Handler para o quickstart do ngrok"""

    def do_GET(self):
        """Responder a requisições GET"""
        path = urlparse(self.path).path

        # Rota raiz
        if path == '/':
            self.send_response(200)
            self.send_header('Content-type', 'text/html; charset=utf-8')
            self.end_headers()

            html = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>ngrok Agent CLI Quickstart</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }
                    .container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    h1 { color: #333; border-bottom: 3px solid #007bff; padding-bottom: 10px; }
                    .status { background: #d4edda; padding: 15px; border-radius: 5px; margin: 20px 0; border-left: 4px solid #28a745; }
                    .code { background: #f8f9fa; padding: 10px; border-radius: 5px; font-family: monospace; margin: 10px 0; }
                    .endpoint { color: #007bff; font-weight: bold; }
                    a { color: #007bff; text-decoration: none; }
                    a:hover { text-decoration: underline; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>🚀 ngrok Agent CLI Quickstart</h1>

                    <div class="status">
                        <strong>✅ Servidor HTTP rodando na porta 8080</strong>
                    </div>

                    <h2>Próximos passos:</h2>
                    <ol>
                        <li><strong>Abra outro terminal</strong> e execute:
                            <div class="code">ngrok http 8080</div>
                        </li>
                        <li>Você verá uma URL como:
                            <div class="code">https://seu-domain.ngrok.io</div>
                        </li>
                        <li>Acesse essa URL para ver este servidor publicamente</li>
                    </ol>

                    <h2>Com Traffic Policy (OAuth):</h2>
                    <div class="code">ngrok start cli-quickstart</div>

                    <hr>

                    <h3>Endpoints de teste:</h3>
                    <ul>
                        <li><a href="/api/status">/api/status</a> - Verificar status</li>
                        <li><a href="/api/info">/api/info</a> - Informações da aplicação</li>
                    </ul>
                </div>
            </body>
            </html>
            """
            self.wfile.write(html.encode())

        # API: Status
        elif path == '/api/status':
            self.send_response(200)
            self.send_header('Content-type', 'application/json')
            self.end_headers()

            response = {
                "status": "ok",
                "message": "ngrok Quickstart Server is running",
                "timestamp": __import__('datetime').datetime.now().isoformat()
            }
            self.wfile.write(json.dumps(response, indent=2).encode())

        # API: Info
        elif path == '/api/info':
            self.send_response(200)
            self.send_header('Content-type', 'application/json')
            self.end_headers()

            response = {
                "app": "ngrok Agent CLI Quickstart",
                "port": 8080,
                "documentation": "https://docs.ngrok.com/gateway/endpoints/agent-cli-quickstart/",
                "features": [
                    "Public HTTP endpoint via ngrok",
                    "Traffic Policy with OAuth support",
                    "SSL/TLS encryption",
                    "Traffic Inspector integration"
                ]
            }
            self.wfile.write(json.dumps(response, indent=2).encode())

        else:
            self.send_response(404)
            self.send_header('Content-type', 'text/plain')
            self.end_headers()
            self.wfile.write(b'404 - Not Found')

    def log_message(self, format, *args):
        """Log customizado"""
        print(f"[{self.log_date_time_string()}] {format % args}")


def main():
    """Iniciar o servidor HTTP"""
    PORT = 8080
    server_address = ('', PORT)
    httpd = HTTPServer(server_address, QuickstartHandler)

    print(f"""
╔══════════════════════════════════════════════════════════════╗
║                 ngrok Agent CLI Quickstart                   ║
╠══════════════════════════════════════════════════════════════╣
║                                                              ║
║  ✅ Servidor HTTP rodando na porta {PORT}                      ║
║                                                              ║
║  📍 Acesso local: http://localhost:{PORT}/                    ║
║                                                              ║
║  🌐 Para expor publicamente, abra outro terminal e execute: ║
║                                                              ║
║     ngrok http {PORT}                                          ║
║                                                              ║
║  📖 Documentação: https://docs.ngrok.com/                   ║
║                                                              ║
╚══════════════════════════════════════════════════════════════╝
""")

    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        print("\n🛑 Servidor interrompido.")
        httpd.server_close()


if __name__ == '__main__':
    main()
