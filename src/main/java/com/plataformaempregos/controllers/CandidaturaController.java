package com.plataformaempregos.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plataformaempregos.domain.Candidatura;
import com.plataformaempregos.dtos.AtualizarStatusCandidaturaDTO;
import com.plataformaempregos.dtos.CandidaturaDTO;
import com.plataformaempregos.services.CandidaturaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/candidaturas")
@Api(tags = "Candidaturas")
public class CandidaturaController {
    
    @Autowired
    private CandidaturaService candidaturaService;
    
    @PostMapping("/candidato/{candidatoId}/vaga/{vagaId}")
    @ApiOperation("Criar nova candidatura")
    public ResponseEntity<CandidaturaDTO> criarCandidatura(
            @PathVariable Long candidatoId,
            @PathVariable Long vagaId) {
        try {
            Candidatura candidatura = candidaturaService.criarCandidatura(candidatoId, vagaId);
            return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(candidatura));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/{id}")
    @ApiOperation("Obter candidatura por ID")
    public ResponseEntity<CandidaturaDTO> obterCandidatura(@PathVariable Long id) {
        return candidaturaService.obterPorId(id)
                .map(candidatura -> ResponseEntity.ok(converterParaDTO(candidatura)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/candidato/{candidatoId}")
    @ApiOperation("Listar candidaturas de um candidato")
    public ResponseEntity<List<CandidaturaDTO>> listarCandidaturasPorCandidato(@PathVariable Long candidatoId) {
        try {
            List<CandidaturaDTO> candidaturas = candidaturaService.listarCandidaturasPorCandidato(candidatoId).stream()
                    .map(this::converterParaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(candidaturas);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/vaga/{vagaId}")
    @ApiOperation("Listar candidaturas de uma vaga")
    public ResponseEntity<List<CandidaturaDTO>> listarCandidaturasPorVaga(@PathVariable Long vagaId) {
        try {
            List<CandidaturaDTO> candidaturas = candidaturaService.listarCandidaturasPorVaga(vagaId).stream()
                    .map(this::converterParaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(candidaturas);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/status")
    @ApiOperation("Atualizar status da candidatura")
    public ResponseEntity<CandidaturaDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestBody AtualizarStatusCandidaturaDTO atualizarDTO) {
        try {
            Candidatura candidatura = candidaturaService.atualizarStatus(
                    id, 
                    atualizarDTO.getStatus(), 
                    atualizarDTO.getFeedback());
            return ResponseEntity.ok(converterParaDTO(candidatura));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    private CandidaturaDTO converterParaDTO(Candidatura candidatura) {
        return CandidaturaDTO.builder()
                .id(candidatura.getId())
                .candidatoId(candidatura.getCandidato() != null ? candidatura.getCandidato().getId() : null)
                .candidatoNome(candidatura.getCandidato() != null ? candidatura.getCandidato().getNome() : null)
                .vagaId(candidatura.getVaga() != null ? candidatura.getVaga().getId() : null)
                .vagaTitulo(candidatura.getVaga() != null ? candidatura.getVaga().getTitulo() : null)
                .status(candidatura.getStatus())
                .scoreCompatibilidade(candidatura.getScoreCompatibilidade())
                .observacoesIA(candidatura.getObservacoesIA())
                .dataCandidatura(candidatura.getDataCandidatura())
                .dataAtualizacao(candidatura.getDataAtualizacao())
                .feedbackEmpresa(candidatura.getFeedbackEmpresa())
                .build();
    }
}
