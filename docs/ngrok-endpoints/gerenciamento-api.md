# Gerenciamento de Endpoints com APIs do ngrok

## Cloud Endpoints

Os Cloud Endpoints são gerenciados programaticamente via:

### `/endpoints` API Resource
A API REST do ngrok fornece endpoints para gerenciar seus Cloud Endpoints:

```
GET    /endpoints           - Listar endpoints
POST   /endpoints           - Criar novo endpoint
GET    /endpoints/{id}      - Obter detalhes de um endpoint
PATCH  /endpoints/{id}      - Atualizar endpoint
DELETE /endpoints/{id}      - Deletar endpoint
```

**Documentação:** [API Reference - Endpoints](https://docs.ngrok.com/api-reference/endpoints/list)

### Kubernetes CRD
Use o `CloudEndpoint` Custom Resource Definition com o Operador Kubernetes:

```yaml
apiVersion: ngrok.com/v1alpha1
kind: CloudEndpoint
metadata:
  name: my-endpoint
spec:
  url: https://my-app.ngrok.io
  backends:
    - targetBackend: my-backend
```

**Documentação:** [Kubernetes CloudEndpoint CRD](https://docs.ngrok.com/gateway/k8s/crds/)

## Agent Endpoints

Os Agent Endpoints são gerenciados programaticamente via:

### SDKs do Agente
SDKs disponíveis para várias linguagens:

- [Python SDK](https://docs.ngrok.com/agent-sdks/python/)
- [Node.js SDK](https://docs.ngrok.com/agent-sdks/nodejs/)
- [Go SDK](https://docs.ngrok.com/agent-sdks/go/)
- [Java SDK](https://docs.ngrok.com/agent-sdks/java/)
- [Rust SDK](https://docs.ngrok.com/agent-sdks/rust/)

**Exemplo em Python:**
```python
from ngrok import connect

# Criar um endpoint HTTP
url = connect(8080)
print(f"Endpoint: {url}")
```

### API do Agente
Controle o agente via API REST:

```
GET    /api/tunnels         - Listar tunnels (endpoints)
POST   /api/tunnels         - Criar novo tunnel
GET    /api/tunnels/{name}  - Obter detalhes de um tunnel
DELETE /api/tunnels/{name}  - Deletar tunnel
```

**Documentação:** [Agent API Documentation](https://docs.ngrok.com/gateway/agent/api/)

### Operador Kubernetes
Implante agents ngrok em seu cluster Kubernetes:

```yaml
apiVersion: ngrok.com/v1alpha1
kind: Tunnel
metadata:
  name: my-tunnel
spec:
  backend:
    backend: http
    config:
      addr: "8080"
```

**Documentação:** [Kubernetes Installation & Helm](https://docs.ngrok.com/gateway/k8s/installation/helm)

## Acesso somente leitura via API

Os Agent Endpoints também são acessíveis somente leitura através da:

### `/endpoints` API Resource (Read-only)
```
GET    /endpoints           - Listar todos os endpoints (incluindo Agent)
GET    /endpoints/{id}      - Obter detalhes de um endpoint
```

Isso permite que você liste e inspecione Agent Endpoints usando a API do ngrok, mas não pode modificá-los através dela.

## Preços

### Planos gratuitos e Hobbyist
Permitem que você use seu crédito incluído para iniciar terminais.

### Plano Pay-as-you-go
* Sem limite para o número de terminais que você pode criar
* Um ponto final que transmite dados em uma hora de relógio é contado como um ponto final ativo para essa hora
* Cobrado uma hora de ponto final

Veja [Pricing](https://ngrok.com/pricing) para mais informações.

## Boas práticas

1. **Use Cloud Endpoints para configurações persistentes** - URLs permanentes e roteamento centralizado
2. **Use Agent Endpoints para desenvolvimento local** - Rápido de iniciar e destruir
3. **Implemente Traffic Policy** - Adicione segurança e lógica sem alterar código
4. **Monitore com Traffic Inspector** - Observe o tráfego em tempo real
5. **Use Endpoint Pooling** - Para balanceamento de carga de múltiplos endpoints

## Recursos adicionais

- [ngrok API Documentation](https://docs.ngrok.com/api/)
- [Traffic Policy Documentation](https://docs.ngrok.com/gateway/traffic-policy/)
- [Kubernetes Operator Guide](https://docs.ngrok.com/gateway/k8s/)
- [Support Portal](https://support.ngrok.com/)
