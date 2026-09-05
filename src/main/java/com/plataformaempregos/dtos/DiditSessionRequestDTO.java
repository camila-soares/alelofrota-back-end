package com.plataformaempregos.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para requisição de criação de sessão Didit
 * Corresponde ao payload enviado para POST /v3/session/
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiditSessionRequestDTO {

    /**
     * ID do workflow Didit (fornecido pelo Didit)
     */
    private String workflow_id;

    /**
     * Dados do vendor - identificador único do usuário na sua plataforma
     * Ex: "user-123", "candidato-456"
     */
    private String vendor_data;

    /**
     * URL de callback que será chamada quando a sessão for atualizada
     */
    private String callback;
}
