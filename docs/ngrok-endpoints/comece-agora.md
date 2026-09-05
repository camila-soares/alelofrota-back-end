# Comece Agora

## Quickstart - ngrok Agent CLI

Habilite o acesso a um aplicativo da web local na porta `8080` com o ngrok CLI:

```bash
ngrok http 8080
```

Este comando iniciará um Endpoint de Agente que encaminha o tráfego através do agente ngrok CLI para o seu aplicativo em execução local na porta 8080.

### Saída esperada
Após executar o comando, você verá algo como:

```
Forwarding                    https://your-random-domain.ngrok.io -> http://localhost:8080
```

Agora você pode acessar sua aplicação local através da URL gerada pelo ngrok!

## Próximos passos

### 1. Conhecer os protocolos
Você pode usar diferentes protocolos dependendo do seu caso de uso:

```bash
# HTTP/HTTPS
ngrok http 8080

# TCP (para SSH, jogos, etc)
ngrok tcp 22

# TLS (para tráfego criptografado)
ngrok tls localhost:8443
```

### 2. Adicionar segurança
Proteja seu endpoint com autenticação:

```bash
# Com autenticação básica
ngrok http -auth "user:password" 8080

# Com OAuth
ngrok http -oauth=google 8080
```

### 3. Configurar Traffic Policy
Crie políticas para manipular o tráfego, rotear por caminhos, adicionar headers, etc.

### 4. Usar Cloud Endpoints
Para endpoints persistentes, use o Painel ngrok ou a API para criar Cloud Endpoints.

## Recursos

- [ngrok Agent CLI Quickstart](https://docs.ngrok.com/gateway/endpoints/agent-cli-quickstart/)
- [Traffic Policy Documentation](https://docs.ngrok.com/gateway/traffic-policy/)
- [ngrok Dashboard](https://dashboard.ngrok.com/)
