# ngrok Agent CLI Quickstart Guide

Guia completo para completar o ngrok Agent CLI quickstart com traffic policy e Google OAuth.

## 📋 Pré-requisitos

- Token ngrok: `3IU7zmxRvf4yw7MfrsweBCGKRAS_2ZYJoG9MBexS9AK2Ga2Fb`
- Domínio reservado: `mustiness-bootleg-subzero.ngrok-free.dev`
- Python 3.6+ ou Node.js 12+ (para o servidor HTTP)

---

## 🚀 Passo 1: Instalar ngrok CLI

### No macOS
```bash
brew install ngrok/ngrok/ngrok
```

### No Linux (Ubuntu/Debian)
```bash
# Adicionar repositório
curl -sSL https://ngrok-agent.s3.amazonaws.com/ngrok.asc | sudo tee /etc/apt/trusted.gpg.d/ngrok.asc >/dev/null
echo "deb https://ngrok-agent.s3.amazonaws.com buster main" | sudo tee /etc/apt/sources.list.d/ngrok.list

# Instalar
sudo apt-get update && sudo apt-get install ngrok
```

### No Windows
```powershell
choco install ngrok
# ou
winget install ngrok
```

Verifique a instalação:
```bash
ngrok version
```

---

## 🔑 Passo 2: Configurar Autenticação

```bash
ngrok config add-authtoken 3IU7zmxRvf4yw7MfrsweBCGKRAS_2ZYJoG9MBexS9AK2Ga2Fb
```

Isso criará um arquivo `~/.ngrok2/ngrok.yml` (ou `~/.config/ngrok/ngrok.yml` no Linux).

Verifique:
```bash
ngrok config check
```

---

## 💻 Passo 3: Iniciar o Servidor HTTP (Porta 8080)

### Opção A: Python (recomendado)
```bash
python3 ngrok-quickstart-app.py
```

Você verá:
```
╔══════════════════════════════════════════════════════════════╗
║                 ngrok Agent CLI Quickstart                   ║
╠══════════════════════════════════════════════════════════════╣
║                                                              ║
║  ✅ Servidor HTTP rodando na porta 8080                     ║
║                                                              ║
║  📍 Acesso local: http://localhost:8080/                    ║
║                                                              ║
║  🌐 Para expor publicamente, abra outro terminal e execute: ║
║                                                              ║
║     ngrok http 8080                                         ║
║                                                              ║
║  📖 Documentação: https://docs.ngrok.com/                  ║
║                                                              ║
╚══════════════════════════════════════════════════════════════╝
```

### Opção B: Node.js
```bash
npx http-server -p 8080
```

### Opção C: Python simples
```bash
python3 -m http.server 8080
```

---

## 🌐 Passo 4: Expor com ngrok (Sem OAuth primeiro)

Abra **outro terminal** e execute:

```bash
ngrok http 8080
```

Você verá algo como:
```
Session Status                online
Account                       <seu-email>
Version                       3.x.x
Region                        United States (us)
Latency                       30ms
Web Interface                 http://127.0.0.1:4040
Forwarding                    https://mustiness-bootleg-subzero.ngrok-free.dev -> http://localhost:8080

Connections                   ttl     opn     rt1     rt5     p50     p90
                              0       0       0.00    0.00    0.00    0.00
```

✅ **Seu servidor está público!** Acesse: `https://mustiness-bootleg-subzero.ngrok-free.dev`

---

## 🔐 Passo 5: Configurar Google OAuth (Traffic Policy)

### 5.1 Criar Google OAuth Credentials

1. Acesse [Google Cloud Console](https://console.cloud.google.com/)
2. Crie um novo projeto (ou selecione um existente)
3. Vá para **APIs & Services** → **Credentials**
4. Clique **Create Credentials** → **OAuth client ID**
5. Escolha **Web application**
6. Adicione em **Authorized redirect URIs**:
   ```
   https://mustiness-bootleg-subzero.ngrok-free.dev/oauth/callback
   ```
7. Copie o **Client ID** e **Client Secret**

### 5.2 Configurar arquivo ngrok.yml

Edite o arquivo `ngrok.yml` com as credenciais:

```yaml
version: "2"
authtoken: 3IU7zmxRvf4yw7MfrsweBCGKRAS_2ZYJoG9MBexS9AK2Ga2Fb

tunnels:
  cli-quickstart:
    proto: http
    addr: 8080
    domain: mustiness-bootleg-subzero.ngrok-free.dev
    
    traffic_policy:
      enabled: true
      value:
        inbound:
          - name: "enforce-oauth"
            expressions:
              - "true"
            actions:
              - type: "oauth"
                config:
                  provider: "google"
                  client_id: "YOUR_CLIENT_ID.apps.googleusercontent.com"
                  client_secret: "YOUR_CLIENT_SECRET"
                  scope: "openid email profile"
                  inbound_cookie_prefix: "ngrok-oauth"
                  cookie_duration: 86400
```

---

## 🚀 Passo 6: Iniciar ngrok com Traffic Policy

Com o servidor HTTP ainda rodando no primeiro terminal:

```bash
ngrok start cli-quickstart
```

Você verá:
```
Session Status                online
Account                       <seu-email>
Version                       3.x.x
Region                        United States (us)

Tunnel "cli-quickstart"       https://mustiness-bootleg-subzero.ngrok-free.dev

Connections                   ttl     opn     rt1     rt5     p50     p90
                              0       0       0.00    0.00    0.00    0.00
```

---

## ✅ Passo 7: Testar Google OAuth

1. Abra: `https://mustiness-bootleg-subzero.ngrok-free.dev`
2. Você será redirecionado para o login do Google
3. Após autenticar, voltará ao seu aplicativo
4. Verifique a aba **Cookies** do DevTools (F12) - deve ter cookie `ngrok-oauth`

---

## 📊 Monitorar com Traffic Inspector

ngrok tem um **Traffic Inspector** web:

1. Abra: `http://127.0.0.1:4040`
2. Veja todas as requisições em tempo real
3. Inspecione headers, body, respostas
4. Simule requisições

---

## 🔧 Troubleshooting

### Erro: "Tunnel already exists"
Você já tem um tunnel ativo com esse domínio. Encerre o outro processo ou use outro domínio.

### Erro: "Invalid OAuth credentials"
- Verifique Client ID e Client Secret
- Confirme que o redirect URI está correto
- Tente regenerar as credenciais no Google Cloud

### Erro: "Connection refused"
- Certifique-se que o servidor HTTP está rodando na porta 8080
- Teste com: `curl http://localhost:8080`

### Traffic Policy não está ativo
- Edite `ngrok.yml` corretamente
- Use `ngrok start` em vez de `ngrok http`
- Verifique com: `ngrok config check`

---

## 📚 Recursos Adicionais

- [ngrok Documentation](https://docs.ngrok.com/)
- [Traffic Policy Guide](https://docs.ngrok.com/gateway/traffic-policy/)
- [OAuth Actions](https://docs.ngrok.com/gateway/traffic-policy/actions/oauth/)
- [ngrok Dashboard](https://dashboard.ngrok.com/)
- [ngrok Agent CLI](https://docs.ngrok.com/gateway/endpoints/agent-cli-quickstart/)

---

## 📝 Resumo dos Comandos

```bash
# 1. Instalar ngrok
brew install ngrok/ngrok/ngrok  # macOS
sudo apt-get install ngrok      # Linux

# 2. Configurar token
ngrok config add-authtoken 3IU7zmxRvf4yw7MfrsweBCGKRAS_2ZYJoG9MBexS9AK2Ga2Fb

# 3. Iniciar servidor (Terminal 1)
python3 ngrok-quickstart-app.py

# 4. Iniciar ngrok básico (Terminal 2)
ngrok http 8080

# 5. Iniciar ngrok com OAuth (Terminal 2, após configurar credentials)
ngrok start cli-quickstart

# 6. Monitorar tráfego
# Abra: http://127.0.0.1:4040
```

---

**Implementação criada:** 2026-09-05  
**Status:** ✅ Pronta para uso
