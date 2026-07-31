# Arquitetura da Plataforma de Empregos com IA

## Visão Geral

A plataforma foi desenvolvida seguindo os princípios de arquitetura em camadas e padrões de design modernos, garantindo escalabilidade, manutenibilidade e extensibilidade.

## Arquitetura em Camadas

### 1. Camada de Apresentação (Controllers)
- **Responsabilidade**: Receber requisições HTTP e retornar respostas
- **Tecnologia**: Spring MVC REST Controllers
- **Localização**: `com.plataformaempregos.controllers`
- **Padrões**: RESTful API, DTO Pattern

### 2. Camada de Serviço (Services)
- **Responsabilidade**: Lógica de negócio e orquestração
- **Tecnologia**: Spring Service Beans
- **Localização**: `com.plataformaempregos.services`
- **Princípios**: Single Responsibility, Dependency Injection

### 3. Camada de Persistência (Repositories)
- **Responsabilidade**: Acesso a dados
- **Tecnologia**: Spring Data JPA
- **Localização**: `com.plataformaempregos.repositories`
- **Padrões**: Repository Pattern, JPA Specifications

### 4. Camada de Domínio (Entities)
- **Responsabilidade**: Modelagem do domínio de negócio
- **Tecnologia**: JPA Entities
- **Localização**: `com.plataformaempregos.domain`
- **Padrões**: Domain Model, Inheritance (Table per Subclass)

## Estrutura de Dados

### Hierarquia de Usuários
```
Usuario (classe base)
├── Empresa
└── Candidato
```

### Relacionamentos Principais
- **Empresa** → **Vaga** (1:N)
- **Candidato** → **PerfilProfissional** (1:1)
- **Candidato** → **Candidatura** (1:N)
- **Vaga** → **Candidatura** (1:N)
- **PerfilProfissional** → **ExperienciaProfissional** (1:N)
- **PerfilProfissional** ↔ **Competencia** (N:N)
- **Vaga** ↔ **Competencia** (N:N)
- **Empresa** → **Plano** (N:1)

## Serviço de IA

### Arquitetura do AIService

O serviço de IA foi projetado para ser extensível e permitir múltiplas implementações:

1. **Cálculo Básico** (implementado)
   - Algoritmo próprio de matching
   - Baseado em regras e pesos configuráveis
   - Não requer dependências externas

2. **Integração Externa** (preparado)
   - Estrutura pronta para APIs de IA (OpenAI, etc)
   - Configurável via `application.properties`
   - Fallback automático para cálculo básico

### Algoritmo de Matching

O score de compatibilidade é calculado considerando:

```
Score Total = Σ (Peso × Fator)

Fatores:
- Competências: 40%
- Nível de Experiência: 20%
- Pretensão Salarial: 15%
- Modalidade de Trabalho: 15%
- Área de Atuação: 10%
```

## Segurança

### Autenticação
- **Método**: JWT (JSON Web Tokens)
- **Algoritmo**: HS512
- **Expiração**: Configurável (padrão: 24h)

### Autorização
- Spring Security com filtros JWT
- Controle de acesso baseado em roles
- CORS configurado para desenvolvimento

## Padrões de Design Utilizados

### 1. Repository Pattern
- Abstração da camada de dados
- Facilita testes e manutenção

### 2. DTO Pattern
- Separação entre modelo de domínio e modelo de API
- Reduz acoplamento e melhora performance

### 3. Builder Pattern
- Utilizado via Lombok
- Facilita criação de objetos complexos

### 4. Strategy Pattern
- Implementado no AIService
- Permite trocar algoritmos de matching

### 5. Dependency Injection
- Spring Framework
- Facilita testes e manutenção

## Escalabilidade

### Horizontal
- Stateless API (JWT)
- Banco de dados pode ser replicado
- Load balancer compatível

### Vertical
- Otimizações de queries
- Cache de recomendações (futuro)
- Processamento assíncrono (futuro)

## Extensibilidade

### Pontos de Extensão

1. **Serviço de IA**
   - Interface preparada para múltiplas implementações
   - Configuração externa

2. **Sistema de Notificações**
   - Estrutura preparada para integração
   - Eventos de domínio podem ser adicionados

3. **Upload de Arquivos**
   - Estrutura preparada
   - Integração com serviços de storage (S3, etc)

4. **Sistema de Pagamentos**
   - Modelo de planos já implementado
   - Pronto para integração com gateways

## Performance

### Otimizações Implementadas
- Lazy Loading em relacionamentos
- Paginação em listagens
- Índices em campos de busca

### Otimizações Futuras
- Cache de recomendações
- Processamento assíncrono de análises IA
- CDN para arquivos estáticos

## Testabilidade

### Estrutura Preparada Para
- Testes unitários (Services)
- Testes de integração (Controllers)
- Testes de repositório (JPA)
- Mock de serviços externos

## Monitoramento

### Preparado Para
- Logging estruturado
- Métricas de performance
- Health checks
- Tracing distribuído

## Deploy

### Ambientes
- **Desenvolvimento**: H2 in-memory
- **Produção**: PostgreSQL recomendado

### Configurações
- Variáveis de ambiente para secrets
- Profiles do Spring Boot
- Docker-ready (futuro)
