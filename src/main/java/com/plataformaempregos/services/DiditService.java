package com.plataformaempregos.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plataformaempregos.domain.Candidato;
import com.plataformaempregos.domain.DeviceIPAnalysis;
import com.plataformaempregos.domain.Empresa;
import com.plataformaempregos.dtos.DiditDecisionResponseDTO;
import com.plataformaempregos.dtos.DiditSessionRequestDTO;
import com.plataformaempregos.dtos.DiditSessionResponseDTO;
import com.plataformaempregos.dtos.DeviceIPAnalysisDTO;
import com.plataformaempregos.enums.IPAnalysisStatus;
import com.plataformaempregos.enums.IPAnalysisWarning;
import com.plataformaempregos.repositories.DeviceIPAnalysisRepository;

/**
 * Serviço para integração com Didit Device & IP Analysis
 * Responsável por:
 * - Criar sessões Didit
 * - Buscar decisões e dados de análise
 * - Processar warnings e aplicar políticas de fraude
 * - Armazenar dados de análise
 */
@Service
public class DiditService {

    @Autowired
    private DeviceIPAnalysisRepository deviceIPAnalysisRepository;

    @Value("${didit.enabled:false}")
    private boolean diditEnabled;

    @Value("${didit.api.url}")
    private String diditApiUrl;

    @Value("${didit.api.key}")
    private String diditApiKey;

    @Value("${didit.workflow.id}")
    private String diditWorkflowId;

    @Value("${didit.callback.base-url}")
    private String diditCallbackUrl;

    @Value("${didit.fraud-policy.vpn-detected:MANUAL_REVIEW}")
    private String vpnPolicy;

    @Value("${didit.fraud-policy.proxy-detected:MANUAL_REVIEW}")
    private String proxyPolicy;

    @Value("${didit.fraud-policy.tor-detected:DECLINE}")
    private String torPolicy;

    @Value("${didit.fraud-policy.duplicated-device:MANUAL_REVIEW}")
    private String duplicatedDevicePolicy;

    @Value("${didit.fraud-policy.location-mismatch:MANUAL_REVIEW}")
    private String locationMismatchPolicy;

    @Value("${didit.fraud-policy.blocklisted-device:DECLINE}")
    private String blockllistedDevicePolicy;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Cria uma nova sessão Didit para coleta de dados de device e IP
     * Deve ser chamado no início do fluxo de verificação
     */
    @Transactional
    public DeviceIPAnalysisDTO criarSessaoDidit(Candidato candidato) {
        if (!diditEnabled) {
            throw new RuntimeException("Didit não está habilitado");
        }

        // Criar requisição para Didit
        DiditSessionRequestDTO sessionRequest = DiditSessionRequestDTO.builder()
                .workflow_id(diditWorkflowId)
                .vendor_data("candidato-" + candidato.getId())
                .callback(diditCallbackUrl + "/callback")
                .build();

        // Chamar API Didit
        DiditSessionResponseDTO sessionResponse = criarSessaoNoDidit(sessionRequest);

        // Salvar análise no banco de dados
        DeviceIPAnalysis analysis = DeviceIPAnalysis.builder()
                .diditSessionId(sessionResponse.getId())
                .sessionUrl(sessionResponse.getUrl())
                .candidato(candidato)
                .status(IPAnalysisStatus.NOT_STARTED)
                .dataCriacao(LocalDateTime.now())
                .dataAtualizacao(LocalDateTime.now())
                .decisionProcessed(false)
                .build();

        DeviceIPAnalysis saved = deviceIPAnalysisRepository.save(analysis);

        return converterParaDTO(saved);
    }

    /**
     * Cria uma nova sessão Didit para uma empresa
     */
    @Transactional
    public DeviceIPAnalysisDTO criarSessaoDiditEmpresa(Empresa empresa) {
        if (!diditEnabled) {
            throw new RuntimeException("Didit não está habilitado");
        }

        DiditSessionRequestDTO sessionRequest = DiditSessionRequestDTO.builder()
                .workflow_id(diditWorkflowId)
                .vendor_data("empresa-" + empresa.getId())
                .callback(diditCallbackUrl + "/callback")
                .build();

        DiditSessionResponseDTO sessionResponse = criarSessaoNoDidit(sessionRequest);

        DeviceIPAnalysis analysis = DeviceIPAnalysis.builder()
                .diditSessionId(sessionResponse.getId())
                .sessionUrl(sessionResponse.getUrl())
                .empresa(empresa)
                .status(IPAnalysisStatus.NOT_STARTED)
                .dataCriacao(LocalDateTime.now())
                .dataAtualizacao(LocalDateTime.now())
                .decisionProcessed(false)
                .build();

        DeviceIPAnalysis saved = deviceIPAnalysisRepository.save(analysis);

        return converterParaDTO(saved);
    }

    /**
     * Busca a decisão de um análise do Didit
     */
    @Transactional
    public DeviceIPAnalysisDTO obterDecisaoDidit(String sessionId) {
        // Buscar análise no banco
        Optional<DeviceIPAnalysis> analysisOpt = deviceIPAnalysisRepository.findByDiditSessionId(sessionId);
        if (!analysisOpt.isPresent()) {
            throw new RuntimeException("Análise não encontrada para sessão: " + sessionId);
        }

        DeviceIPAnalysis analysis = analysisOpt.get();

        // Buscar decisão do Didit
        DiditDecisionResponseDTO decision = obterDecisaoNoDidit(sessionId);

        // Processar a resposta e atualizar análise
        processarDecisaoDidit(analysis, decision);

        // Salvar
        analysis = deviceIPAnalysisRepository.save(analysis);

        return converterParaDTO(analysis);
    }

    /**
     * Busca análise por ID da sessão Didit
     */
    public Optional<DeviceIPAnalysisDTO> obterPorSessionId(String sessionId) {
        return deviceIPAnalysisRepository.findByDiditSessionId(sessionId)
                .map(this::converterParaDTO);
    }

    /**
     * Lista análises de um candidato
     */
    public List<DeviceIPAnalysisDTO> listarAnalisesCandidato(Long candidatoId) {
        return deviceIPAnalysisRepository.findByCandidatoId(candidatoId)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtém a análise mais recente não processada de um candidato
     */
    public Optional<DeviceIPAnalysisDTO> obterAnaliseNaoProcessadaCandidato(Long candidatoId) {
        return deviceIPAnalysisRepository.findFirstByCandidatoIdAndDecisionProcessedFalseOrderByDataCriacaoDesc(candidatoId)
                .map(this::converterParaDTO);
    }

    /**
     * Verifica se um candidato possui um dispositivo duplicado
     */
    public Optional<DeviceIPAnalysisDTO> verificarDispositivoDuplicado(Long candidatoId, String deviceFingerprintId) {
        return deviceIPAnalysisRepository.findByCandidatoIdAndDeviceFingerprintId(candidatoId, deviceFingerprintId)
                .map(this::converterParaDTO);
    }

    /**
     * Processa warnings de fraude conforme a política configurada
     * Retorna a ação a ser tomada (APPROVED, MANUAL_REVIEW, DECLINED)
     */
    public String avaliarRiscoDeFraude(DeviceIPAnalysisDTO analysis) {
        List<IPAnalysisWarning> warnings = analysis.getWarnings();
        if (warnings == null || warnings.isEmpty()) {
            return "APPROVED";
        }

        // Verificar cada warning e aplicar política
        for (IPAnalysisWarning warning : warnings) {
            String action = getPolicyForWarning(warning);
            if ("DECLINE".equals(action)) {
                return "DECLINED"; // Política de declinação tem prioridade
            }
        }

        // Se nenhum foi declinado, verificar se algum é manual review
        for (IPAnalysisWarning warning : warnings) {
            String action = getPolicyForWarning(warning);
            if ("MANUAL_REVIEW".equals(action)) {
                return "MANUAL_REVIEW";
            }
        }

        return "APPROVED";
    }

    /**
     * Retorna a política para um warning específico
     */
    private String getPolicyForWarning(IPAnalysisWarning warning) {
        switch (warning) {
            case VPN_DETECTED:
                return vpnPolicy;
            case PROXY_DETECTED:
                return proxyPolicy;
            case TOR_DETECTED:
                return torPolicy;
            case DUPLICATED_DEVICE:
            case POSSIBLE_DUPLICATED_DEVICE:
                return duplicatedDevicePolicy;
            case LOCATION_MISMATCH_WITH_DOCUMENT:
                return locationMismatchPolicy;
            case DEVICE_FINGERPRINT_BLOCKLISTED:
                return blockllistedDevicePolicy;
            default:
                return "MANUAL_REVIEW";
        }
    }

    // ========== PRIVATE METHODS ==========

    /**
     * Chama a API Didit para criar uma sessão
     */
    private DiditSessionResponseDTO criarSessaoNoDidit(DiditSessionRequestDTO request) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(diditApiUrl + "/v3/session/");
            httpPost.setHeader("x-api-key", diditApiKey);
            httpPost.setHeader("Content-Type", "application/json");

            String requestBody = objectMapper.writeValueAsString(request);
            httpPost.setEntity(new StringEntity(requestBody));

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode >= 200 && statusCode < 300) {
                    return objectMapper.readValue(responseBody, DiditSessionResponseDTO.class);
                } else {
                    throw new RuntimeException("Erro ao criar sessão Didit: " + statusCode + " - " + responseBody);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao comunicar com Didit: " + e.getMessage(), e);
        }
    }

    /**
     * Chama a API Didit para obter a decisão
     */
    private DiditDecisionResponseDTO obterDecisaoNoDidit(String sessionId) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(diditApiUrl + "/v3/session/" + sessionId + "/decision/");
            httpGet.setHeader("x-api-key", diditApiKey);
            httpGet.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = client.execute(httpGet)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode >= 200 && statusCode < 300) {
                    return objectMapper.readValue(responseBody, DiditDecisionResponseDTO.class);
                } else {
                    throw new RuntimeException("Erro ao obter decisão Didit: " + statusCode + " - " + responseBody);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao comunicar com Didit: " + e.getMessage(), e);
        }
    }

    /**
     * Processa a resposta de decisão do Didit e atualiza a entidade
     */
    private void processarDecisaoDidit(DeviceIPAnalysis analysis, DiditDecisionResponseDTO decision) {
        analysis.setStatus(IPAnalysisStatus.fromValue(decision.getStatus()));
        analysis.setDataAtualizacao(LocalDateTime.now());

        // Processar data de expiração KYC
        if (decision.getKyc_expiration_date() != null) {
            try {
                // Tentar fazer parsing da data ISO-8601
                ZonedDateTime zdt = ZonedDateTime.parse(decision.getKyc_expiration_date());
                analysis.setKycExpirationDate(zdt.toLocalDateTime());
            } catch (Exception e) {
                // Tentar formato alternativo se necessário
                try {
                    LocalDateTime ldt = LocalDateTime.parse(decision.getKyc_expiration_date(),
                            DateTimeFormatter.ISO_DATE_TIME);
                    analysis.setKycExpirationDate(ldt);
                } catch (Exception e2) {
                    // Log do erro
                    System.err.println("Erro ao fazer parsing da data KYC: " + decision.getKyc_expiration_date());
                }
            }
        }

        // Processar análises de IP (pode haver múltiplas)
        if (decision.getIp_analyses() != null && !decision.getIp_analyses().isEmpty()) {
            DiditDecisionResponseDTO.IPAnalysisDto ipAnalysis = decision.getIp_analyses().get(0);

            analysis.setDeviceFingerprintId(ipAnalysis.getDevice_fingerprint_id());
            analysis.setCountryCode(ipAnalysis.getCountry());
            analysis.setCity(ipAnalysis.getCity());
            analysis.setLatitude(ipAnalysis.getLatitude());
            analysis.setLongitude(ipAnalysis.getLongitude());
            analysis.setAsn(ipAnalysis.getAsn());
            analysis.setAsnOrganization(ipAnalysis.getAsn_organization());
            analysis.setVpnDetected(ipAnalysis.getVpn() != null ? ipAnalysis.getVpn() : false);
            analysis.setProxyDetected(ipAnalysis.getProxy() != null ? ipAnalysis.getProxy() : false);
            analysis.setTorDetected(ipAnalysis.getTor() != null ? ipAnalysis.getTor() : false);
            analysis.setDatacenterIp(ipAnalysis.getDatacenter() != null ? ipAnalysis.getDatacenter() : false);
            analysis.setIpAddress(ipAnalysis.getIp_address());

            // Processar warnings
            if (ipAnalysis.getWarnings() != null && !ipAnalysis.getWarnings().isEmpty()) {
                List<IPAnalysisWarning> warnings = new ArrayList<>();
                for (String warningCode : ipAnalysis.getWarnings()) {
                    IPAnalysisWarning warning = IPAnalysisWarning.fromCode(warningCode);
                    if (warning != null) {
                        warnings.add(warning);
                    }
                }
                analysis.setWarnings(warnings);
            }

            analysis.setStatus(IPAnalysisStatus.fromValue(ipAnalysis.getStatus()));
        }

        analysis.setDecisionProcessed(true);

        // Armazenar dados brutos para auditoria
        try {
            analysis.setRawData(objectMapper.writeValueAsString(decision));
        } catch (Exception e) {
            System.err.println("Erro ao serializar dados brutos: " + e.getMessage());
        }
    }

    /**
     * Converte entidade para DTO
     */
    private DeviceIPAnalysisDTO converterParaDTO(DeviceIPAnalysis analysis) {
        return DeviceIPAnalysisDTO.builder()
                .id(analysis.getId())
                .diditSessionId(analysis.getDiditSessionId())
                .sessionUrl(analysis.getSessionUrl())
                .status(analysis.getStatus())
                .deviceFingerprintId(analysis.getDeviceFingerprintId())
                .countryCode(analysis.getCountryCode())
                .countryName(analysis.getCountryName())
                .city(analysis.getCity())
                .latitude(analysis.getLatitude())
                .longitude(analysis.getLongitude())
                .asn(analysis.getAsn())
                .asnOrganization(analysis.getAsnOrganization())
                .vpnDetected(analysis.getVpnDetected())
                .proxyDetected(analysis.getProxyDetected())
                .torDetected(analysis.getTorDetected())
                .datacenterIp(analysis.getDatacenterIp())
                .ipAddress(analysis.getIpAddress())
                .warnings(analysis.getWarnings())
                .decisionProcessed(analysis.getDecisionProcessed())
                .observations(analysis.getObservations())
                .dataCriacao(analysis.getDataCriacao())
                .dataAtualizacao(analysis.getDataAtualizacao())
                .kycExpirationDate(analysis.getKycExpirationDate())
                .build();
    }
}
