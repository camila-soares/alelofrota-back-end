package com.plataformaempregos.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VagaRecomendadaDTO {
    private Long vagaId;
    private String vagaTitulo;
    private String empresaNome;
    private Double scoreCompatibilidade;
    private String observacoesIA;
    private VagaDTO vaga;
}
