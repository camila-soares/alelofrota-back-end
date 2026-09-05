# Cloud Endpoints vs Agent Endpoints

## Cloud Endpoints

### Características
* Persistente e globalmente disponível
* Comece com o Painel ou API
* Ótimo para configuração centralizada, roteamento multisserviço e failover de serviço

### Quando usar
- Configuração centralizada
- Roteamento de múltiplos serviços
- Failover de serviço
- URLs persistentes

### Gerenciamento
São gerenciados programaticamente via:
* [`/endpoints` API Resource](https://docs.ngrok.com/api-reference/endpoints/list)
* [`CloudEndpoint` CRD](https://docs.ngrok.com/gateway/k8s/crds/) do Operador Kubernetes

## Agent Endpoints

### Características
* Efêmero e ambientalmente agnóstico
* Comece com a CLI, SDKs de linguagem ou através do Kubernetes
* Ótimo para cargas de trabalho efêmeras, desenvolvimento local, serviços de dimensionamento e gerenciamento de dispositivos

### Quando usar
- Desenvolvimento local
- Cargas de trabalho efêmeras
- Gerenciamento de dispositivos
- Balanceamento de carga individual

### Gerenciamento
São gerenciados programaticamente via:
* [SDKs do Agente](https://docs.ngrok.com/agent-sdks/)
* [API do Agente](https://docs.ngrok.com/gateway/agent/api/)
* [Operador Kubernetes](https://docs.ngrok.com/gateway/k8s/installation/helm)

Os Endpoints do Agente também são acessíveis somente leitura via:
* [`/endpoints` Recurso API](https://docs.ngrok.com/api-reference/endpoints/list)

## Comparação rápida

| Aspecto | Cloud Endpoints | Agent Endpoints |
|--------|-----------------|-----------------|
| Disponibilidade | Persistente | Efêmero |
| Configuração | API/Painel | CLI/SDK |
| Melhor para | Roteamento centralizado | Desenvolvimento local |
| Escopo | Global | Ambiental |
| URLs | Sempre ativas | Enquanto agente roda |

## Balanceamento de carga com Endpoint Pooling

[Endpoint Pooling](https://docs.ngrok.com/gateway/endpoints/endpoint-pooling) permite que você crie vários endpoints com a mesma URL. O tráfego enviado para uma URL com um Endpoint Pool é load-balanced entre os Endpoints no pool.

Consulte o [Endpoint Pooling quickstart](https://docs.ngrok.com/gateway/endpoints/endpoint-pooling/#quickstart) para saber mais.
