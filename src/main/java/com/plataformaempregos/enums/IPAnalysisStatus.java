package com.plataformaempregos.enums;

/**
 * Status da análise de IP e Device do Didit
 * Baseado no enum status da resposta de decisão do Didit
 */
public enum IPAnalysisStatus {
    NOT_STARTED("Not Started"),
    IN_PROGRESS("In Progress"),
    APPROVED("Approved"),
    IN_REVIEW("In Review"),
    DECLINED("Declined"),
    ABANDONED("Abandoned"),
    KYC_EXPIRED("Kyc Expired");

    private final String value;

    IPAnalysisStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static IPAnalysisStatus fromValue(String value) {
        for (IPAnalysisStatus status : IPAnalysisStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return NOT_STARTED;
    }
}
