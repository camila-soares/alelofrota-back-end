# Processamento Assíncrono de Análises IA

## Visão Geral

O sistema implementa processamento assíncrono para análises de IA, permitindo que operações demoradas sejam executadas em background sem bloquear requisições HTTP.

## Arquitetura

### Componentes Principais

1. **AsyncConfig**: Configuração de executors para processamento assíncrono
2. **AsyncAIService**: Serviço que executa análises de IA de forma assíncrona
3. **ProcessamentoIA**: Entidade que rastreia o status de cada processamento
4. **NotificationService**: Serviço para notificações quando processamento conclui
5. **ProcessamentoIAController**: Endpoints para consultar status de processamentos

### Thread Pools

- **aiTaskExecutor**: Pool dedicado para processamento de IA
  - Core Pool: 5 threads
  - Max Pool: 10 threads
  - Queue Capacity: 100 tarefas

- **notificationTaskExecutor**: Pool para notificações
  - Core Pool: 2 threads
  - Max Pool: 5 threads
  - Queue Capacity: 50 tarefas

## Tipos de Processamento Assíncrono

### 1. Análise de Candidatura
Processa análise de compatibilidade entre candidato e vaga.

**Endpoint**: `POST /api/candidaturas/{candidaturaId}/processar-analise?usuarioId={id}`

**Fluxo**:
1. Cria registro de processamento com status PENDENTE
2. Atualiza status para PROCESSANDO
3. Calcula score de compatibilidade (progresso: 30%)
4. Gera observações da IA (progresso: 60%)
5. Atualiza candidatura com resultados (progresso: 90%)
6. Finaliza com status CONCLUIDO (progresso: 100%)
7. Envia notificação

### 2. Recomendação de Candidatos
Busca candidatos recomendados para uma vaga.

**Endpoint**: `GET /api/ai/vaga/{vagaId}/candidatos-recomendados?assincrono=true&usuarioId={id}&limite=10`

**Fluxo**:
1. Cria registro de processamento
2. Busca candidatos compatíveis
3. Calcula scores para cada candidato
4. Gera resultado em JSON
5. Finaliza e notifica

### 3. Recomendação de Vagas
Busca vagas recomendadas para um candidato.

**Endpoint**: `GET /api/ai/candidato/{candidatoId}/vagas-recomendadas?assincrono=true&usuarioId={id}&limite=10`

**Fluxo**:
Similar ao de recomendação de candidatos, mas invertido.

### 4. Análise de Perfil Profissional
Analisa perfil profissional do candidato.

**Endpoint**: `POST /api/perfis-profissionais/{perfilId}/processar-analise?usuarioId={id}`

## Status de Processamento

- **PENDENTE**: Processamento criado, aguardando início
- **PROCESSANDO**: Em execução
- **CONCLUIDO**: Finalizado com sucesso
- **ERRO**: Ocorreu erro durante processamento
- **CANCELADO**: Cancelado pelo usuário

## Consultando Status

### Obter Processamento por ID
```bash
GET /api/processamentos-ia/{id}
```

### Obter Processamento por Entidade
```bash
GET /api/processamentos-ia/entidade/{entidadeId}/{entidadeTipo}
```

### Listar Processamentos do Usuário
```bash
GET /api/processamentos-ia/usuario/{usuarioId}
```

### Listar por Status
```bash
GET /api/processamentos-ia/status/{status}
# Status: PENDENTE, PROCESSANDO, CONCLUIDO, ERRO, CANCELADO
```

### Listar por Tipo
```bash
GET /api/processamentos-ia/tipo/{tipo}
# Tipos: ANALISE_CANDIDATURA, RECOMENDACAO_CANDIDATOS, RECOMENDACAO_VAGAS, ANALISE_PERFIL
```

## Exemplos de Uso

### Processamento Assíncrono de Candidatura

```bash
# Criar candidatura com processamento assíncrono
POST /api/candidaturas/candidato/1/vaga/1?processarAssincrono=true

# Ou processar análise depois
POST /api/candidaturas/1/processar-analise?usuarioId=1

# Consultar status
GET /api/processamentos-ia/entidade/1/CANDIDATURA
```

### Recomendação Assíncrona

```bash
# Buscar candidatos recomendados (assíncrono)
GET /api/ai/vaga/1/candidatos-recomendados?assincrono=true&usuarioId=1&limite=10

# Resposta: 202 Accepted
# Location: /api/processamentos-ia/{id}

# Consultar resultado
GET /api/processamentos-ia/{id}
```

## Resposta de Processamento

```json
{
  "id": 1,
  "tipoProcessamento": "ANALISE_CANDIDATURA",
  "status": "CONCLUIDO",
  "entidadeId": 1,
  "entidadeTipo": "CANDIDATURA",
  "scoreCalculado": 85.5,
  "observacoes": "Score de compatibilidade: 85.50%...",
  "progresso": 100,
  "dataInicio": "2024-01-15T10:00:00",
  "dataConclusao": "2024-01-15T10:00:05",
  "tempoProcessamentoMs": 5000,
  "resultado": "{\"score\": 85.5, \"observacoes\": \"...\"}"
}
```

## Notificações

O sistema envia notificações quando:
- Processamento é concluído com sucesso
- Ocorre erro no processamento

**Notificações futuras** (preparado para implementação):
- Email
- Push notifications
- Webhooks
- WebSocket para atualização em tempo real

## Tratamento de Erros

Em caso de erro:
1. Status é atualizado para ERRO
2. Mensagem de erro é armazenada
3. Notificação de erro é enviada
4. Processamento pode ser consultado para diagnóstico

## Performance

### Benefícios
- Requisições HTTP não bloqueiam
- Melhor experiência do usuário
- Escalabilidade melhorada
- Processamento em lote possível

### Métricas Rastreadas
- Tempo de processamento (ms)
- Progresso (0-100%)
- Status em tempo real
- Histórico de processamentos

## Configuração

### application.properties

```properties
# Configuração de threads (já configurado no AsyncConfig)
# Pode ser ajustado conforme necessidade
```

### Ajustando Thread Pools

Edite `AsyncConfig.java` para ajustar:
- Tamanho do pool
- Capacidade da fila
- Política de rejeição

## Melhores Práticas

1. **Sempre consultar status**: Não assumir que processamento terminou
2. **Polling inteligente**: Consultar status periodicamente, não constantemente
3. **Tratamento de erros**: Sempre verificar status ERRO
4. **Timeouts**: Implementar timeout no cliente
5. **Notificações**: Usar notificações quando disponíveis

## Próximos Passos

- [ ] WebSocket para atualizações em tempo real
- [ ] Sistema de filas (RabbitMQ/Kafka) para alta escala
- [ ] Retry automático em caso de erro
- [ ] Processamento em lote
- [ ] Cache de resultados
- [ ] Priorização de processamentos
