package com.plataformaempregos.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.plataformaempregos.enums.IPAnalysisStatus;
import com.plataformaempregos.enums.IPAnalysisWarning;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resposta de análise de Device & IP
 * Usada para retornar dados ao frontend
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceIPAnalysisDTO {

    private Long id;

    /**
     * ID da sessão Didit
     */
    private String diditSessionId;

    /**
     * URL da sessão para o usuário coletar fingerprint
     */
    private String sessionUrl;

    /**
     * Status da análise
     */
    private IPAnalysisStatus status;

    /**
     * ID do device fingerprint
     */
    private String deviceFingerprintId;

    /**
     * Informações de localização
     */
    private String countryCode;
    private String countryName;
    private String city;
    private Double latitude;
    private Double longitude;

    /**
     * Informações de ASN/Rede
     */
    private String asn;
    private String asnOrganization;

    /**
     * Flags de detecção
     */
    private Boolean vpnDetected;
    private Boolean proxyDetected;
    private Boolean torDetected;
    private Boolean datacenterIp;

    /**
     * Endereço IP analisado
     */
    private String ipAddress;

    /**
     * Warnings detectados
     */
    private List<IPAnalysisWarning> warnings;

    /**
     * Flag indicando se a decisão foi processada
     */
    private Boolean decisionProcessed;

    /**
     * Observações (para auditoria)
     */
    private String observations;

    /**
     * Data de criação
     */
    private LocalDateTime dataCriacao;

    /**
     * Data de atualização
     */
    private LocalDateTime dataAtualizacao;

    /**
     * Data de expiração do KYC
     */
    private LocalDateTime kycExpirationDate;
}
