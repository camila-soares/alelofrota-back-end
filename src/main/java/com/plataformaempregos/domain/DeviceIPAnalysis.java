package com.plataformaempregos.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.plataformaempregos.enums.IPAnalysisStatus;
import com.plataformaempregos.enums.IPAnalysisWarning;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que armazena os dados da análise de IP e Device coletados pelo Didit
 * Inclui informações de geolocalização, ASN, VPN/Proxy/Tor detection,
 * device fingerprint e warnings de risco
 */
@Entity
@Table(name = "device_ip_analysis")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceIPAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    @Column(name = "id")
    private Long id;

    /**
     * ID da sessão Didit que gerou esta análise
     */
    @Column(name = "didit_session_id", unique = true, nullable = false)
    private String diditSessionId;

    /**
     * Candidato associado (pode ser nulo se a análise foi coletada mas não vinculada)
     */
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "candidato_id")
    private Candidato candidato;

    /**
     * Empresa associada (para análises em fluxo de empresas)
     */
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    /**
     * URL da sessão Didit para o usuário acessar e coletar fingerprint
     */
    @Column(name = "session_url")
    private String sessionUrl;

    /**
     * Status da análise
     */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private IPAnalysisStatus status;

    /**
     * Fingerprint ID do dispositivo fornecido pelo Didit
     */
    @Column(name = "device_fingerprint_id")
    private String deviceFingerprintId;

    /**
     * País detectado pelo IP (código ISO 2 letras)
     */
    @Column(name = "country_code")
    private String countryCode;

    /**
     * País detectado pelo IP (nome completo)
     */
    @Column(name = "country_name")
    private String countryName;

    /**
     * Cidade detectada pelo IP
     */
    @Column(name = "city")
    private String city;

    /**
     * Latitude da geolocalização
     */
    @Column(name = "latitude")
    private Double latitude;

    /**
     * Longitude da geolocalização
     */
    @Column(name = "longitude")
    private Double longitude;

    /**
     * ASN (Autonomous System Number) do IP
     */
    @Column(name = "asn")
    private String asn;

    /**
     * Organização responsável pelo ASN
     */
    @Column(name = "asn_organization")
    private String asnOrganization;

    /**
     * Flag indicando se VPN foi detectada
     */
    @Column(name = "vpn_detected")
    private Boolean vpnDetected;

    /**
     * Flag indicando se Proxy foi detectado
     */
    @Column(name = "proxy_detected")
    private Boolean proxyDetected;

    /**
     * Flag indicando se Tor foi detectado
     */
    @Column(name = "tor_detected")
    private Boolean torDetected;

    /**
     * Flag indicando se o IP é de um datacenter
     */
    @Column(name = "datacenter_ip")
    private Boolean datacenterIp;

    /**
     * Enderço IP que foi analisado
     */
    @Column(name = "ip_address")
    private String ipAddress;

    /**
     * Lista de warnings/risk codes detectados na análise
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "warning")
    @Enumerated(EnumType.STRING)
    private List<IPAnalysisWarning> warnings;

    /**
     * Dados adicionais em JSON (para extensibilidade futura)
     */
    @Column(name = "raw_data", columnDefinition = "TEXT")
    private String rawData;

    /**
     * Flag indicando se a análise foi processada e decisão tomada
     */
    @Column(name = "decision_processed")
    private Boolean decisionProcessed;

    /**
     * Observações sobre a análise (para auditoria)
     */
    @Column(name = "observations", columnDefinition = "TEXT")
    private String observations;

    /**
     * Data de criação do registro
     */
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    /**
     * Data da última atualização
     */
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    /**
     * Data de expiração da análise KYC
     */
    @Column(name = "kyc_expiration_date")
    private LocalDateTime kycExpirationDate;
}
