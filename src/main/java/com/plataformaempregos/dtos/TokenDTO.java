package com.plataformaempregos.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    private String token;
    private String tipo;
    private Long usuarioId;
    private String tipoUsuario;
}
