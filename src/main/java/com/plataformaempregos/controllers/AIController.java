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

import java.util.concurrent.CompletableFuture;

import com.plataformaempregos.domain.Candidato;
import com.plataformaempregos.domain.ProcessamentoIA;
import com.plataformaempregos.domain.Vaga;
import com.plataformaempregos.dtos.CandidatoRecomendadoDTO;
import com.plataformaempregos.dtos.VagaRecomendadaDTO;
import com.plataformaempregos.services.AsyncAIService;
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
    
    @Autowired
    private AsyncAIService asyncAIService;
    
    @GetMapping("/vaga/{vagaId}/candidatos-recomendados")
    @ApiOperation("Buscar candidatos recomendados para uma vaga")
    public ResponseEntity<List<CandidatoRecomendadoDTO>> buscarCandidatosRecomendados(
            @PathVariable Long vagaId,
            @RequestParam(defaultValue = "10") int limite,
            @RequestParam(required = false, defaultValue = "false") boolean assincrono,
            @RequestParam(required = false) Long usuarioId) {
        try {
            // Se processamento assíncrono solicitado
            if (assincrono && usuarioId != null) {
                // Criar processamento e iniciar assíncrono
                ProcessamentoIA processamento = asyncAIService.criarProcessamentoRecomendacaoCandidatos(
                        vagaId, limite, usuarioId);
                asyncAIService.processarRecomendacaoCandidatosAsync(processamento.getId());
                // Retorna 202 Accepted com o processamento criado
                return ResponseEntity.accepted()
                        .header("Location", "/api/processamentos-ia/" + processamento.getId())
                        .body(processamento);
            }
            
            // Processamento síncrono (padrão)
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
        } catch (Exception e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/candidato/{candidatoId}/vagas-recomendadas")
    @ApiOperation("Buscar vagas recomendadas para um candidato")
    public ResponseEntity<List<VagaRecomendadaDTO>> buscarVagasRecomendadas(
            @PathVariable Long candidatoId,
            @RequestParam(defaultValue = "10") int limite,
            @RequestParam(required = false, defaultValue = "false") boolean assincrono,
            @RequestParam(required = false) Long usuarioId) {
        try {
            // Se processamento assíncrono solicitado
            if (assincrono && usuarioId != null) {
                // Criar processamento e iniciar assíncrono
                ProcessamentoIA processamento = asyncAIService.criarProcessamentoRecomendacaoVagas(
                        candidatoId, limite, usuarioId);
                asyncAIService.processarRecomendacaoVagasAsync(processamento.getId());
                // Retorna 202 Accepted com o processamento criado
                return ResponseEntity.accepted()
                        .header("Location", "/api/processamentos-ia/" + processamento.getId())
                        .body(processamento);
            }
            
            // Processamento síncrono (padrão)
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
        } catch (Exception e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
