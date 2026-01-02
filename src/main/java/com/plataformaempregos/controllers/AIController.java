package com.plataformaempregos.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plataformaempregos.domain.Candidato;
import com.plataformaempregos.domain.Vaga;
import com.plataformaempregos.dtos.CandidatoRecomendadoDTO;
import com.plataformaempregos.dtos.VagaRecomendadaDTO;
import com.plataformaempregos.services.AIService;
import com.plataformaempregos.services.CandidatoService;
import com.plataformaempregos.services.VagaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/ai")
@Api(tags = "Inteligência Artificial - Recomendações")
public class AIController {
    
    @Autowired
    private AIService aiService;
    
    @Autowired
    private VagaService vagaService;
    
    @Autowired
    private CandidatoService candidatoService;
    
    @GetMapping("/vaga/{vagaId}/candidatos-recomendados")
    @ApiOperation("Buscar candidatos recomendados para uma vaga")
    public ResponseEntity<List<CandidatoRecomendadoDTO>> buscarCandidatosRecomendados(
            @PathVariable Long vagaId,
            @RequestParam(defaultValue = "10") int limite) {
        try {
            Vaga vaga = vagaService.obterPorId(vagaId)
                    .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));
            
            List<Candidato> candidatos = aiService.buscarCandidatosRecomendados(vagaId, limite);
            
            List<CandidatoRecomendadoDTO> recomendados = candidatos.stream()
                    .map(candidato -> {
                        Double score = aiService.calcularCompatibilidade(candidato, vaga);
                        String observacoes = aiService.gerarObservacoesCompatibilidade(candidato, vaga, score);
                        
                        return CandidatoRecomendadoDTO.builder()
                                .candidatoId(candidato.getId())
                                .candidatoNome(candidato.getNome())
                                .candidatoEmail(candidato.getEmail())
                                .scoreCompatibilidade(score)
                                .observacoesIA(observacoes)
                                .build();
                    })
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(recomendados);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/candidato/{candidatoId}/vagas-recomendadas")
    @ApiOperation("Buscar vagas recomendadas para um candidato")
    public ResponseEntity<List<VagaRecomendadaDTO>> buscarVagasRecomendadas(
            @PathVariable Long candidatoId,
            @RequestParam(defaultValue = "10") int limite) {
        try {
            Candidato candidato = candidatoService.obterPorId(candidatoId)
                    .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));
            
            List<Vaga> vagas = aiService.buscarVagasRecomendadas(candidatoId, limite);
            
            List<VagaRecomendadaDTO> recomendadas = vagas.stream()
                    .map(vaga -> {
                        Double score = aiService.calcularCompatibilidade(candidato, vaga);
                        String observacoes = aiService.gerarObservacoesCompatibilidade(candidato, vaga, score);
                        
                        return VagaRecomendadaDTO.builder()
                                .vagaId(vaga.getId())
                                .vagaTitulo(vaga.getTitulo())
                                .empresaNome(vaga.getEmpresa() != null ? vaga.getEmpresa().getNomeFantasia() : null)
                                .scoreCompatibilidade(score)
                                .observacoesIA(observacoes)
                                .build();
                    })
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(recomendadas);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
