# Exemplos de API - Didit Device & IP Analysis

## 1. Criar Sessão Didit para Candidato

### Request
```bash
curl -X POST http://localhost:8080/api/didit/candidatos/1/sessao \
  -H "Content-Type: application/json"
```

### Response (201 Created)
```json
{
  "id": 1,
  "diditSessionId": "sess_abc123def456",
  "sessionUrl": "https://verification.didit.me/verify?session=sess_abc123def456",
  "status": "NOT_STARTED",
  "deviceFingerprintId": null,
  "countryCode": null,
  "countryName": null,
  "city": null,
  "latitude": null,
  "longitude": null,
  "asn": null,
  "asnOrganization": null,
  "vpnDetected": null,
  "proxyDetected": null,
  "torDetected": null,
  "datacenterIp": null,
  "ipAddress": null,
  "warnings": null,
  "decisionProcessed": false,
  "observations": null,
  "dataCriacao": "2024-09-05T10:30:00",
  "dataAtualizacao": "2024-09-05T10:30:00",
  "kycExpirationDate": null
}
```

**O que fazer a seguir:**
- Salvar o `diditSessionId` no frontend
- Redirecionar usuário para `sessionUrl`
- Usuário completa coleta de fingerprint/IP na sessão Didit
- Usuário retorna para seu app

---

## 2. Obter Decisão de Análise

Após o usuário completar a sessão Didit, busque a decisão:

### Request
```bash
curl -X GET http://localhost:8080/api/didit/sessao/sess_abc123def456/decisao \
  -H "Content-Type: application/json"
```

### Response (200 OK)
```json
{
  "id": 1,
  "diditSessionId": "sess_abc123def456",
  "sessionUrl": "https://verification.didit.me/verify?session=sess_abc123def456",
  "status": "APPROVED",
  "deviceFingerprintId": "dfp_12345abc",
  "countryCode": "BR",
  "countryName": "Brazil",
  "city": "São Paulo",
  "latitude": -23.5505,
  "longitude": -46.6333,
  "asn": "AS28573",
  "asnOrganization": "Claro S.A.",
  "vpnDetected": false,
  "proxyDetected": false,
  "torDetected": false,
  "datacenterIp": false,
  "ipAddress": "200.100.50.25",
  "warnings": [],
  "decisionProcessed": true,
  "observations": null,
  "dataCriacao": "2024-09-05T10:30:00",
  "dataAtualizacao": "2024-09-05T10:35:00",
  "kycExpirationDate": "2025-09-05T10:35:00"
}
```

---

## 3. Avaliar Risco de Fraude

### Request (Sem warnings)
```bash
curl -X GET http://localhost:8080/api/didit/sessao/sess_abc123def456/avaliar-risco \
  -H "Content-Type: application/json"
```

### Response (200 OK)
```json
{
  "action": "APPROVED",
  "warnings": []
}
```

### Request (Com warnings)
```bash
curl -X GET http://localhost:8080/api/didit/sessao/sess_xyz789/avaliar-risco \
  -H "Content-Type: application/json"
```

### Response (200 OK)
```json
{
  "action": "MANUAL_REVIEW",
  "warnings": [
    "VPN_DETECTED",
    "LOCATION_MISMATCH_WITH_DOCUMENT"
  ]
}
```

### Response (Crítico)
```json
{
  "action": "DECLINED",
  "warnings": [
    "TOR_DETECTED",
    "DEVICE_FINGERPRINT_BLOCKLISTED"
  ]
}
```

---

## 4. Listar Análises de um Candidato

### Request
```bash
curl -X GET http://localhost:8080/api/didit/candidatos/1/analises \
  -H "Content-Type: application/json"
```

### Response (200 OK)
```json
[
  {
    "id": 1,
    "diditSessionId": "sess_abc123def456",
    "sessionUrl": "https://verification.didit.me/verify?session=sess_abc123def456",
    "status": "APPROVED",
    "deviceFingerprintId": "dfp_12345abc",
    "countryCode": "BR",
    "countryName": "Brazil",
    "city": "São Paulo",
    "latitude": -23.5505,
    "longitude": -46.6333,
    "asn": "AS28573",
    "asnOrganization": "Claro S.A.",
    "vpnDetected": false,
    "proxyDetected": false,
    "torDetected": false,
    "datacenterIp": false,
    "ipAddress": "200.100.50.25",
    "warnings": [],
    "decisionProcessed": true,
    "dataCriacao": "2024-09-05T10:30:00",
    "dataAtualizacao": "2024-09-05T10:35:00"
  },
  {
    "id": 2,
    "diditSessionId": "sess_xyz789",
    "sessionUrl": "https://verification.didit.me/verify?session=sess_xyz789",
    "status": "IN_REVIEW",
    "deviceFingerprintId": "dfp_67890xyz",
    "countryCode": "US",
    "countryName": "United States",
    "city": "San Francisco",
    "latitude": 37.7749,
    "longitude": -122.4194,
    "asn": "AS8453",
    "asnOrganization": "Tecore Networks",
    "vpnDetected": true,
    "proxyDetected": false,
    "torDetected": false,
    "datacenterIp": false,
    "ipAddress": "192.0.2.50",
    "warnings": ["VPN_DETECTED"],
    "decisionProcessed": true,
    "dataCriacao": "2024-09-04T15:20:00",
    "dataAtualizacao": "2024-09-04T15:25:00"
  }
]
```

---

## 5. Obter Análise Não Processada

Útil para verificar se há análise pendente:

### Request
```bash
curl -X GET http://localhost:8080/api/didit/candidatos/1/analise-pendente \
  -H "Content-Type: application/json"
```

### Response (200 OK - Encontrada)
```json
{
  "id": 3,
  "diditSessionId": "sess_new123",
  "sessionUrl": "https://verification.didit.me/verify?session=sess_new123",
  "status": "NOT_STARTED",
  "deviceFingerprintId": null,
  "countryCode": null,
  "countryName": null,
  "city": null,
  "latitude": null,
  "longitude": null,
  "asn": null,
  "asnOrganization": null,
  "vpnDetected": null,
  "proxyDetected": null,
  "torDetected": null,
  "datacenterIp": null,
  "ipAddress": null,
  "warnings": null,
  "decisionProcessed": false,
  "dataCriacao": "2024-09-05T11:00:00",
  "dataAtualizacao": "2024-09-05T11:00:00"
}
```

### Response (404 Not Found)
```json
{
  "timestamp": 1630314000000,
  "status": 404,
  "error": "Not Found"
}
```

---

## 6. Verificar Dispositivo Duplicado

Para detectar se um dispositivo já foi registrado:

### Request
```bash
curl -X GET http://localhost:8080/api/didit/candidatos/1/dispositivo/dfp_12345abc/duplicado \
  -H "Content-Type: application/json"
```

### Response (200 OK - Duplicado Encontrado)
```json
{
  "id": 1,
  "diditSessionId": "sess_abc123def456",
  "sessionUrl": "https://verification.didit.me/verify?session=sess_abc123def456",
  "status": "APPROVED",
  "deviceFingerprintId": "dfp_12345abc",
  "countryCode": "BR",
  "countryName": "Brazil",
  "city": "São Paulo",
  "latitude": -23.5505,
  "longitude": -46.6333,
  "asn": "AS28573",
  "asnOrganization": "Claro S.A.",
  "vpnDetected": false,
  "proxyDetected": false,
  "torDetected": false,
  "datacenterIp": false,
  "ipAddress": "200.100.50.25",
  "warnings": ["DUPLICATED_DEVICE"],
  "decisionProcessed": true,
  "dataCriacao": "2024-09-05T10:30:00",
  "dataAtualizacao": "2024-09-05T10:35:00"
}
```

### Response (404 - Dispositivo Único)
```json
{
  "timestamp": 1630314000000,
  "status": 404,
  "error": "Not Found"
}
```

---

## 7. Fluxo Completo de Verificação (JavaScript)

```javascript
// ===== PASSO 1: Criar candidato =====
const candidatoResponse = await fetch('/api/candidatos', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    email: 'joao@example.com',
    nome: 'João Silva',
    cpf: '12345678900',
    dataNascimento: '1990-01-15'
  })
});

const candidato = await candidatoResponse.json();
console.log('Candidato criado:', candidato.id);

// ===== PASSO 2: Criar sessão Didit =====
const sessaoResponse = await fetch(
  `/api/didit/candidatos/${candidato.id}/sessao`,
  { method: 'POST' }
);

const sessao = await sessaoResponse.json();
console.log('Sessão criada:', sessao.diditSessionId);
console.log('URL de verificação:', sessao.sessionUrl);

// ===== PASSO 3: Redirecionar para coleta =====
// Aqui o usuário será redirecionado para Didit para coletar fingerprint
// setTimeout(() => {
//   window.location.href = sessao.sessionUrl;
// }, 1000);

// ===== PASSO 4: Após retorno da sessão Didit (callback) =====
// Uma vez que o usuário volte, você pode buscar a decisão

const decisaoResponse = await fetch(
  `/api/didit/sessao/${sessao.diditSessionId}/decisao`
);

const decisao = await decisaoResponse.json();
console.log('Status:', decisao.status);
console.log('País:', decisao.countryCode);
console.log('VPN Detectada:', decisao.vpnDetected);
console.log('Warnings:', decisao.warnings);

// ===== PASSO 5: Avaliar risco =====
const riscoResponse = await fetch(
  `/api/didit/sessao/${sessao.diditSessionId}/avaliar-risco`
);

const risco = await riscoResponse.json();
console.log('Ação recomendada:', risco.action);

// ===== PASSO 6: Tomar decisão =====
if (risco.action === 'APPROVED') {
  console.log('✅ Candidato aprovado');
  // Prosseguir com cadastro
} else if (risco.action === 'MANUAL_REVIEW') {
  console.log('⚠️ Requer revisão manual');
  // Mostrar para análise
} else if (risco.action === 'DECLINED') {
  console.log('❌ Candidato rejeitado');
  // Não permitir cadastro
}
```

---

## 8. Callback do Didit

Quando a sessão é atualizada no Didit, uma chamada será feita para:

```bash
POST http://seu-dominio/api/didit/callback?sessionId=sess_abc123&status=APPROVED
```

Resposta esperada:
```json
{
  "message": "Callback recebido",
  "sessionId": "sess_abc123"
}
```

---

## Casos de Uso Específicos

### Caso 1: Candidato com VPN

**Request:**
```bash
GET /api/didit/sessao/sess_vpn123/avaliar-risco
```

**Response:**
```json
{
  "action": "MANUAL_REVIEW",
  "warnings": ["VPN_DETECTED"]
}
```

**Implementação Backend:**
```java
if ("MANUAL_REVIEW".equals(action)) {
    // Criar ticket para análise manual
    // Enviar email para revisor
    // Ou adicionar candidato a fila de revisão
}
```

### Caso 2: Dispositivo Duplicado

**Request:**
```bash
GET /api/didit/candidatos/1/dispositivo/dfp_abc123/duplicado
```

**Response:**
```json
{
  "id": 1,
  "diditSessionId": "sess_old123",
  "status": "APPROVED",
  "deviceFingerprintId": "dfp_abc123",
  "warnings": ["DUPLICATED_DEVICE"]
}
```

**Implementação Backend:**
```java
// Opção 1: Mesclar contas
candidatoService.mergeAccounts(novoId, existinteId);

// Opção 2: Bloquear
candidatoService.bloquear(novoId, "Dispositivo duplicado");

// Opção 3: Avisar
notificationService.avisarDuplicacao(novoId, existinteId);
```

### Caso 3: Localização Impossível

**Resposta Didit:**
```json
{
  "warnings": ["IMPOSSIBLE_TRAVEL"]
}
```

**Ação Recomendada:**
```java
// Verificar:
// - Última análise conhecido
// - Distância vs tempo decorrido
// - Se velocidade > velocidade máxima de viagem

LocalDateTime ultimaSessao = obterUltimaSessao(candidatoId);
double distancia = calcularDistancia(lat1, lon1, lat2, lon2);
long tempoMs = ChronoUnit.MILLIS.between(ultimaSessao, LocalDateTime.now());
double velocidadeMaxima = 900; // km/h (velocidade avião comercial)

double distanciaMaxima = velocidadeMaxima * (tempoMs / 3600000.0);
if (distancia > distanciaMaxima) {
    acao = "MANUAL_REVIEW"; // Possível fraude
}
```

---

## Tratamento de Erros

### Erro 400 - Bad Request
```json
{
  "error": "Candidato não encontrado"
}
```

### Erro 401 - Unauthorized
```json
{
  "error": "API Key inválida"
}
```

### Erro 500 - Internal Server Error
```json
{
  "error": "Erro ao comunicar com Didit"
}
```

---

## Rates e Limites

- **Rate Limit**: 1000 requests/hora por API Key
- **Timeout**: 30 segundos por request
- **Data Retention**: 7 anos (conforme LGPD/GDPR)
- **Session Duration**: 24 horas (configurável)

---

## Dicas de Performance

1. **Cache de Decisões**: Armazene por 24h se verificado com sucesso
2. **Verificação em Background**: Use fila assíncrona para processar
3. **Batch Processing**: Agrupe callbacks para processar em lote
4. **Índices de Banco**: Crie índices em `deviceFingerprintId` e `candidatoId`

```sql
CREATE INDEX idx_device_analysis_device_fp ON device_ip_analysis(device_fingerprint_id);
CREATE INDEX idx_device_analysis_candidato ON device_ip_analysis(candidato_id);
CREATE INDEX idx_device_analysis_status ON device_ip_analysis(status);
```
