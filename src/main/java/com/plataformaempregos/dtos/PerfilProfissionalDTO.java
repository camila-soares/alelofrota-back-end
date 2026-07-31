package com.plataformaempregos.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilProfissionalDTO {
    private Long id;
    private String resumoProfissional;
    private String areaAtuacao;
    private Double pretensaoSalarial;
    private String disponibilidade;
    private List<ExperienciaProfissionalDTO> experiencias;
    private List<Long> competenciasIds;
    private Double scoreIA;
}
