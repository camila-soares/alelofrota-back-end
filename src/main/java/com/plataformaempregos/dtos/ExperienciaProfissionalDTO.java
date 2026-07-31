package com.plataformaempregos.dtos;

import java.time.LocalDate;

import com.plataformaempregos.enums.NivelExperiencia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienciaProfissionalDTO {
    private Long id;
    private String empresa;
    private String cargo;
    private NivelExperiencia nivelExperiencia;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Boolean empregoAtual;
    private String descricao;
}
