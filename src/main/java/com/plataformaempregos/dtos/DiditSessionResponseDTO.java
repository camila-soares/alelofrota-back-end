package com.plataformaempregos.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resposta de criação de sessão Didit
 * Corresponde ao payload retornado por POST /v3/session/
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiditSessionResponseDTO {

    /**
     * ID único da sessão criada
     */
    private String id;

    /**
     * URL que deve ser aberta pelo usuário para coletar o fingerprint do dispositivo
     */
    private String url;

    /**
     * Status inicial da sessão
     */
    private String status;

    /**
     * Dados adicionais da sessão
     */
    private Object metadata;
}
