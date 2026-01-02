package com.plataformaempregos.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.plataformaempregos.enums.NivelExperiencia;
import com.plataformaempregos.enums.StatusVaga;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VagaDTO {
    private Long id;
    private Long empresaId;
    private String empresaNome;
    private String titulo;
    private String descricao;
    private String requisitos;
    private String beneficios;
    private Double salarioMinimo;
    private Double salarioMaximo;
    private NivelExperiencia nivelExperiencia;
    private String tipoContratacao;
    private String modalidadeTrabalho;
    private String localizacao;
    private StatusVaga status;
    private List<Long> competenciasRequeridasIds;
    private LocalDateTime dataPublicacao;
    private LocalDateTime dataFechamento;
}
