# Como os Endpoints Funcionam

Os terminais permitem o fluxo e o gerenciamento de tráfego por meio de três recursos principais:

## Componentes principais

### URL
O ponto de entrada para o tráfego chegar aos seus serviços. Pode ser qualquer URL, como:
- `https://your-app.ngrok.io`
- `tcp://your-api.internal`

### Binding
A fonte ou origem do tráfego de entrada.

### Traffic Policy
Configuração para gerenciar, manipular e proteger o tráfego.

## Controle o acesso com Bindings

O binding de um ponto final define *onde o tráfego se origina*:

### Público
Aceita tráfego da internet aberta (por exemplo, `https://your-app.ngrok.io`).

### Interno
Restrito para aceitar tráfego apenas através da ação de Política de Tráfego `forward-internal` (por exemplo, `http://your-api.internal`).

### Kubernetes
Restrito para aceitar tráfego apenas de clusters Kubernetes usando o Operador Kubernetes ngrok.

## Definir protocolo via esquema de URL

Você pode especificar o protocolo de URL do seu endpoint para indicar o tipo de tráfego que ele deve lidar. Isso permite que você configure seu endpoint para corresponder aos seus serviços e tipo de aplicativo:

### HTTP/HTTPS
Melhor para Webhooks, APIs, aplicativos

### TCP
Melhor para fluxos brutos (SSH, jogos, IoT)

### TLS
Melhor para cargas de trabalho criptografadas personalizadas

## Gerenciar, manipular e proteger o tráfego

Adicione lógica de negócios e roteie o tráfego durante o ciclo de vida da solicitação com [Traffic Policy](/docs/gateway/traffic-policy/):

### Rotear o tráfego
Caminhos, cabeçalhos ou qualquer outra variável disponível.

### Manipular o tráfego
Através de actions: Redirecione URLs, injete cabeçalhos ou envie respostas personalizadas.

### Seguro
Adicione OAuth, restrições de IP ou mutual TLS.

## Entregar o tráfego em qualquer lugar

Você pode hospedar terminais em sua máquina local com o ngrok Agent ou na nuvem com ngrok Cloud Endpoints.
