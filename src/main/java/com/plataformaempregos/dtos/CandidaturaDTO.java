package com.plataformaempregos.dtos;

import java.time.LocalDateTime;

import com.plataformaempregos.enums.StatusCandidatura;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidaturaDTO {
    private Long id;
    private Long candidatoId;
    private String candidatoNome;
    private Long vagaId;
    private String vagaTitulo;
    private StatusCandidatura status;
    private Double scoreCompatibilidade;
    private String observacoesIA;
    private LocalDateTime dataCandidatura;
    private LocalDateTime dataAtualizacao;
    private String feedbackEmpresa;
}
