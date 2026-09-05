# Visão Geral dos Endpoints ngrok

> Crie e gerencie ngrok Endpoints para entregar e gerenciar o tráfego para seus serviços.

Um **ngrok Endpoint** é uma URL que permite que o tráfego de rede chegue aos seus serviços.

Você pode pensar em endpoints como um gateway para qualquer coisa que você queira habilitar o acesso - seja um servidor de desenvolvimento local em seu laptop, um cluster Kubernetes de produção, um banco de dados por trás de um firewall corporativo ou uma API de nuvem. Os endpoints preenchem a lacuna entre seus recursos e o tráfego que precisa alcançá-los.

## Por que os pontos finais são importantes

Endpoints alimentam o Gateway do ngrok, permitindo que você:

* **Vá ao vivo instantaneamente**: Habilite o acesso a protótipos locais, ferramentas internas ou serviços Kubernetes em segundos.

* **Fale com qualquer coisa**: APIs, clusters Kubernetes, servidores de jogos, ferramentas de terceiros, dispositivos IoT, aplicativos legados - os endpoints suportam todos eles.

* **Acesso seguro**: Bloqueio sem esforço o acesso com criptografia, autenticação ou restrições de IP para o que você precisar sem alterações de código.

* **Observabilidade**: Obtenha informações instantâneas sobre como o tráfego está fluindo através dos terminais com o Inspetor de Tráfego e a Exportação de Log.

* **Simplize sua pilha**: Reduza a expansão da infraestrutura encadeando terminais e incorporando lógica diretamente no fluxo de tráfego - limitação de taxa, respostas simuladas ou políticas de segurança, executadas na borda sem necessidade de código.

## Documentação

- [Como os endpoints funcionam](./como-funcionam.md)
- [Cloud Endpoints vs Agent Endpoints](./tipos-endpoints.md)
- [Comece agora](./comece-agora.md)
- [Gerenciamento via APIs](./gerenciamento-api.md)

## Recursos adicionais

- [ngrok Cloud Endpoints](/docs/gateway/endpoints/cloud-endpoints)
- [ngrok Agent Endpoints](/docs/gateway/endpoints/agent-endpoints)
- [Traffic Policy](/docs/gateway/traffic-policy)
- [Endpoint Pooling](/docs/gateway/endpoints/endpoint-pooling)
