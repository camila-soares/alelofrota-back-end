package com.plataformaempregos.dtos;

import com.plataformaempregos.enums.TipoPlano;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanoDTO {
    private Long id;
    private String nome;
    private TipoPlano tipoPlano;
    private Double valorMensal;
    private Integer limiteVagas;
    private Integer limiteCandidatosVisualizacao;
    private Boolean suportePrioritario;
    private Boolean analiseIAAvancada;
    private Boolean relatoriosAvancados;
    private String descricao;
}
