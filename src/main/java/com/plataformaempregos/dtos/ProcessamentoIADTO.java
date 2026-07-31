package com.plataformaempregos.dtos;

import java.time.LocalDateTime;

import com.plataformaempregos.enums.StatusProcessamentoIA;
import com.plataformaempregos.enums.TipoProcessamentoIA;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessamentoIADTO {
    private Long id;
    private TipoProcessamentoIA tipoProcessamento;
    private StatusProcessamentoIA status;
    private Long entidadeId;
    private String entidadeTipo;
    private String resultado;
    private Double scoreCalculado;
    private String observacoes;
    private String erro;
    private Integer progresso;
    private LocalDateTime dataInicio;
    private LocalDateTime dataConclusao;
    private Long tempoProcessamentoMs;
    private Long usuarioId;
}
