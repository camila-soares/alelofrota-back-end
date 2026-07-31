package com.plataformaempregos.dtos;

import com.plataformaempregos.enums.StatusCandidatura;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarStatusCandidaturaDTO {
    private StatusCandidatura status;
    private String feedback;
}
