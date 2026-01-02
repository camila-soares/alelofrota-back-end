package com.plataformaempregos.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidatoRecomendadoDTO {
    private Long candidatoId;
    private String candidatoNome;
    private String candidatoEmail;
    private Double scoreCompatibilidade;
    private String observacoesIA;
    private PerfilProfissionalDTO perfilProfissional;
}
