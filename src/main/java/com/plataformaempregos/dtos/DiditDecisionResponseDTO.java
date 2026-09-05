package com.plataformaempregos.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resposta de decisão Didit
 * Corresponde ao payload retornado por GET /v3/session/{sessionId}/decision/
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiditDecisionResponseDTO {

    /**
     * ID da sessão
     */
    private String id;

    /**
     * Status geral da sessão
     */
    private String status;

    /**
     * Data de expiração do KYC
     */
    private String kyc_expiration_date;

    /**
     * Análises de IP - array contendo uma entrada por ponto de coleta de IP/device
     */
    private List<IPAnalysisDto> ip_analyses;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IPAnalysisDto {

        /**
         * Status da análise
         */
        private String status;

        /**
         * ID do device fingerprint
         */
        private String device_fingerprint_id;

        /**
         * País detectado (código ISO 2 letras)
         */
        private String country;

        /**
         * Cidade detectada
         */
        private String city;

        /**
         * Latitude
         */
        private Double latitude;

        /**
         * Longitude
         */
        private Double longitude;

        /**
         * ASN (Autonomous System Number)
         */
        private String asn;

        /**
         * Organização do ASN
         */
        private String asn_organization;

        /**
         * Flag VPN detectada
         */
        private Boolean vpn;

        /**
         * Flag Proxy detectado
         */
        private Boolean proxy;

        /**
         * Flag Tor detectado
         */
        private Boolean tor;

        /**
         * Flag datacenter IP
         */
        private Boolean datacenter;

        /**
         * Endereço IP analisado
         */
        private String ip_address;

        /**
         * Array de warnings/risk codes
         * Ex: ["VPN_DETECTED", "LOCATION_MISMATCH_WITH_DOCUMENT"]
         */
        private List<String> warnings;

        /**
         * Dados adicionais
         */
        private Object metadata;
    }
}
