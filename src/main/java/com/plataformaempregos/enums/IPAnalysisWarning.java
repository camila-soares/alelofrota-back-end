package com.plataformaempregos.enums;

/**
 * Warnings e risk codes da análise de IP e Device do Didit
 * Baseado no enum LogWarningChoices.LOCATION do Didit
 */
public enum IPAnalysisWarning {
    VPN_DETECTED("VPN_DETECTED", "VPN detectada"),
    PROXY_DETECTED("PROXY_DETECTED", "Proxy detectado"),
    TOR_DETECTED("TOR_DETECTED", "Tor detectado"),
    DATACENTER_IP("DATACENTER_IP", "IP de datacenter"),
    HIGH_RISK_COUNTRY("HIGH_RISK_COUNTRY", "País de alto risco"),
    LOCATION_MISMATCH_WITH_DOCUMENT("LOCATION_MISMATCH_WITH_DOCUMENT", "Localização não corresponde ao documento"),
    DUPLICATED_DEVICE("DUPLICATED_DEVICE", "Dispositivo duplicado"),
    POSSIBLE_DUPLICATED_DEVICE("POSSIBLE_DUPLICATED_DEVICE", "Possível dispositivo duplicado"),
    IMPOSSIBLE_TRAVEL("IMPOSSIBLE_TRAVEL", "Viagem impossível detectada"),
    DEVICE_FINGERPRINT_BLOCKLISTED("DEVICE_FINGERPRINT_BLOCKLISTED", "Dispositivo bloqueado");

    private final String code;
    private final String description;

    IPAnalysisWarning(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static IPAnalysisWarning fromCode(String code) {
        for (IPAnalysisWarning warning : IPAnalysisWarning.values()) {
            if (warning.code.equals(code)) {
                return warning;
            }
        }
        return null;
    }
}
