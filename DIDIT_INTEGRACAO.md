# Integração Didit Device & IP Analysis

## 📋 Visão Geral

A integração Didit Device & IP Analysis coleta dados de geolocalização, identificação de dispositivo e análise de risco de IP para cada usuário. Isso permite:

- ✅ **Device Fingerprinting**: Identificar dispositivos únicos
- ✅ **IP Intelligence**: Analisar localização, ASN e detectar VPN/Proxy/Tor
- ✅ **Geolocation**: Obter país, cidade, coordenadas
- ✅ **Fraud Detection**: Detectar dispositivos duplicados, localização impossível, etc
- ✅ **Risk Assessment**: Aplicar políticas de fraude personalizadas

## 🚀 Configuração

### 1. Variáveis de Ambiente

Adicione ao seu `application.properties`:

```properties
# Didit Device & IP Analysis Configuration
didit.enabled=true
didit.api.url=https://verification.didit.me
didit.api.key=YOUR_DIDIT_API_KEY                      # Obtém em Didit Console
didit.workflow.id=YOUR_DIDIT_WORKFLOW_ID              # Obtém em Didit Console
didit.callback.base-url=http://seu-dominio/api/didit

# Políticas de Fraude (MANUAL_REVIEW, DECLINE, APPROVE)
didit.fraud-policy.vpn-detected=MANUAL_REVIEW
didit.fraud-policy.proxy-detected=MANUAL_REVIEW
didit.fraud-policy.tor-detected=DECLINE
didit.fraud-policy.datacenter-ip=MANUAL_REVIEW
didit.fraud-policy.high-risk-country=MANUAL_REVIEW
didit.fraud-policy.location-mismatch=MANUAL_REVIEW
didit.fraud-policy.duplicated-device=MANUAL_REVIEW
didit.fraud-policy.blocklisted-device=DECLINE
```

### 2. Obter Credenciais Didit

1. Acesse [Didit Business Console](https://console.didit.me)
2. Crie um novo Workflow ou use um existente
3. Copie o `WORKFLOW_ID`
4. Gere uma `API_KEY`
5. Configure no `application.properties`

## 📱 Fluxo de Integração

### Passo 1: Criar Sessão Didit

Ao iniciar o processo de verificação (ex: cadastro de candidato), crie uma sessão:

```bash
POST /api/didit/candidatos/{candidatoId}/sessao
```

**Resposta:**
```json
{
  "id": "aeb9b1d....",
  "diditSessionId": "aeb9b1d...",
  "sessionUrl": "https://verification.didit.me/...",
  "status": "NOT_STARTED",
  "dataCriacao": "2024-09-05T10:30:00"
}
```

### Passo 2: Usuário Acessa URL de Coleta

O frontend deve redirecionar o usuário para `sessionUrl` onde ele:
- Permite coleta de fingerprint do dispositivo
- Fornece dados de IP (automático)
- Completa qualquer verificação adicional configurada

### Passo 3: Obter Decisão

Após o usuário completar a coleta, busque a decisão:

```bash
GET /api/didit/sessao/{sessionId}/decisao
```

**Resposta:**
```json
{
  "id": "aeb9b1d...",
  "diditSessionId": "aeb9b1d...",
  "status": "APPROVED",
  "deviceFingerprintId": "d123456",
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
}
```

### Passo 4: Avaliar Risco de Fraude

Verifique se existem warnings e aplique a política configurada:

```bash
GET /api/didit/sessao/{sessionId}/avaliar-risco
```

**Resposta com warnings:**
```json
{
  "action": "MANUAL_REVIEW",
  "warnings": [
    "VPN_DETECTED",
    "LOCATION_MISMATCH_WITH_DOCUMENT"
  ]
}
```

**Resposta sem warnings:**
```json
{
  "action": "APPROVED",
  "warnings": []
}
```

## 🎯 Status da Análise

| Status | Significado |
|--------|-------------|
| `NOT_STARTED` | Sessão criada, aguardando coleta |
| `IN_PROGRESS` | Usuário coletando dados |
| `APPROVED` | Análise completada com sucesso |
| `IN_REVIEW` | Aguardando revisão manual |
| `DECLINED` | Rejeitado pela política de fraude |
| `ABANDONED` | Usuário abandonou o fluxo |
| `KYC_EXPIRED` | KYC expirou |

## ⚠️ Warnings e Risk Codes

Todos os warnings vêm do bucket `LOCATION` do Didit:

| Warning | Descrição | Ação Padrão |
|---------|-----------|-------------|
| `VPN_DETECTED` | VPN detectada | MANUAL_REVIEW |
| `PROXY_DETECTED` | Proxy detectado | MANUAL_REVIEW |
| `TOR_DETECTED` | Tor detectado | DECLINE |
| `DATACENTER_IP` | IP de datacenter | MANUAL_REVIEW |
| `HIGH_RISK_COUNTRY` | País de alto risco | MANUAL_REVIEW |
| `LOCATION_MISMATCH_WITH_DOCUMENT` | IP não corresponde ao documento | MANUAL_REVIEW |
| `DUPLICATED_DEVICE` | Dispositivo já registrado | MANUAL_REVIEW |
| `POSSIBLE_DUPLICATED_DEVICE` | Possível dispositivo duplicado | MANUAL_REVIEW |
| `IMPOSSIBLE_TRAVEL` | Viagem impossível detectada | MANUAL_REVIEW |
| `DEVICE_FINGERPRINT_BLOCKLISTED` | Dispositivo na blocklist | DECLINE |

## 📊 Verificar Dispositivos Duplicados

Se um candidato tenta registrar com um dispositivo já usado:

```bash
GET /api/didit/candidatos/{candidatoId}/dispositivo/{deviceFingerprintId}/duplicado
```

**Resposta (se duplicado):**
```json
{
  "id": 1,
  "diditSessionId": "...",
  "status": "APPROVED",
  "deviceFingerprintId": "d123456",
  "warnings": ["DUPLICATED_DEVICE"]
}
```

## 🔄 Histórico de Análises

Listar todas as análises de um candidato:

```bash
GET /api/didit/candidatos/{candidatoId}/analises
```

**Resposta:**
```json
[
  {
    "id": 1,
    "diditSessionId": "aeb9b1d...",
    "status": "APPROVED",
    "countryCode": "BR",
    "warnings": []
  },
  {
    "id": 2,
    "diditSessionId": "xyz789...",
    "status": "IN_REVIEW",
    "countryCode": "BR",
    "warnings": ["VPN_DETECTED"]
  }
]
```

## 🛠️ Integração no Fluxo de Cadastro

### Modificar CandidatoController

```java
@PostMapping
@ApiOperation("Cadastrar novo candidato com análise Didit")
public ResponseEntity<?> criarCandidato(@RequestBody CandidatoDTO candidatoDTO) {
    try {
        // 1. Criar candidato
        Candidato candidato = candidatoService.criarCandidato(candidatoDTO);
        
        // 2. Criar sessão Didit
        DeviceIPAnalysisDTO analysis = diditService.criarSessaoDidit(candidato);
        
        // 3. Retornar candidato + URL de verificação
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new CandidatroComVerificacao(
                converterParaDTO(candidato),
                analysis.getSessionUrl()
            ));
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
```

### DTO de Resposta com Verificação

```java
@Data
@Builder
public class CandidatoComVerificacao {
    private CandidatoDTO candidato;
    private String verificacaoUrl;
    private String sessionId;
}
```

## 📱 Frontend Integration

### React Example

```jsx
// 1. Criar candidato e obter sessão
const response = await fetch('/api/candidatos', {
  method: 'POST',
  body: JSON.stringify(candidatoData)
});

const { candidato, verificacaoUrl, sessionId } = await response.json();

// 2. Redirecionar para coleta Didit
window.location.href = verificacaoUrl;

// 3. Após retorno, buscar decisão
const decision = await fetch(`/api/didit/sessao/${sessionId}/decisao`);
const analysis = await decision.json();

// 4. Verificar risco
if (analysis.status === 'APPROVED') {
  // Prosseguir
} else if (analysis.status === 'IN_REVIEW') {
  // Mostrar mensagem de análise manual
} else {
  // Rejeitar cadastro
}
```

## 🔐 Políticas de Fraude

### Configuração Recomendada

**Para Candidatos:**
```properties
didit.fraud-policy.vpn-detected=MANUAL_REVIEW
didit.fraud-policy.proxy-detected=MANUAL_REVIEW
didit.fraud-policy.tor-detected=DECLINE
didit.fraud-policy.duplicated-device=MANUAL_REVIEW
didit.fraud-policy.location-mismatch=MANUAL_REVIEW
didit.fraud-policy.blocklisted-device=DECLINE
```

**Para Empresas:**
```properties
didit.fraud-policy.vpn-detected=APPROVE           # Menos restritivo
didit.fraud-policy.proxy-detected=APPROVE
didit.fraud-policy.tor-detected=MANUAL_REVIEW
didit.fraud-policy.duplicated-device=APPROVE
didit.fraud-policy.blocklisted-device=DECLINE     # Sempre rejeita
```

## 🧪 Testing

### Mock Didit para Desenvolvimento

```java
@Configuration
public class DiditMockConfig {
    @Bean
    @Primary
    public DiditService diditServiceMock() {
        return new DiditService() {
            @Override
            public DeviceIPAnalysisDTO criarSessaoDidit(Candidato candidato) {
                return DeviceIPAnalysisDTO.builder()
                    .sessionUrl("https://mock-didit.test/session")
                    .status(IPAnalysisStatus.APPROVED)
                    .build();
            }
        };
    }
}
```

## 📈 Monitoramento

### Queries Úteis

```sql
-- Análises com warnings
SELECT * FROM device_ip_analysis 
WHERE warnings IS NOT NULL AND warnings != '[]'
ORDER BY data_criacao DESC;

-- Candidatos com VPN detectada
SELECT * FROM device_ip_analysis 
WHERE vpn_detected = true
ORDER BY data_criacao DESC;

-- Dispositivos duplicados
SELECT device_fingerprint_id, COUNT(*) as count
FROM device_ip_analysis
WHERE candidato_id IS NOT NULL
GROUP BY device_fingerprint_id
HAVING COUNT(*) > 1;

-- Taxa de aprovação por país
SELECT country_code, status, COUNT(*) as count
FROM device_ip_analysis
GROUP BY country_code, status;
```

## 🐛 Troubleshooting

### Erro: "Didit não está habilitado"
- Verifique se `didit.enabled=true` em `application.properties`

### Erro: "API Key inválida"
- Verifique se a `API_KEY` está correta no console Didit
- Certifique-se que o IP da aplicação está na whitelist Didit

### Erro: "Workflow ID não encontrado"
- Copie o `WORKFLOW_ID` do console Didit
- Certifique-se que o workflow está ativo

### Sessão sem dados de IP
- Verifique se o usuário completou a coleta na URL de sessão
- Pode indicar problemas com scripts de coleta bloqueados no navegador
- Use DevTools do navegador para checar erros de JavaScript

### Callback não recebido
- Verifique se a `callback.base-url` está acessível da internet
- Configure firewall/proxy se necessário
- Use ngrok ou similar para testar localmente

## 📚 Referências

- [Didit Documentation](https://docs.didit.me)
- [IP Analysis Integration Guide](https://docs.didit.me/integration/ip-analysis)
- [Risk Codes and Warnings](https://docs.didit.me/core-technology/ip-analysis/warnings-ip-analysis)
- [Session API](https://docs.didit.me/sessions-api/create-session)

## 📝 Próximos Passos

- [ ] Integrar verificação de análise no fluxo de login
- [ ] Criar dashboard de fraude no admin
- [ ] Implementar re-verificação periódica de KYC
- [ ] Criar webhooks para eventos de fraude
- [ ] Implementar processamento assíncrono de análises
- [ ] Adicionar cache de decisões
- [ ] Criar alertas para padrões suspeitos
