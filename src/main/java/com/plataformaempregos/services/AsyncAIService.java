package com.plataformaempregos.services;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plataformaempregos.domain.Candidato;
import com.plataformaempregos.domain.Candidatura;
import com.plataformaempregos.domain.ProcessamentoIA;
import com.plataformaempregos.domain.Vaga;
import com.plataformaempregos.enums.StatusProcessamentoIA;
import com.plataformaempregos.enums.TipoProcessamentoIA;
import com.plataformaempregos.repositories.CandidaturaRepository;
import com.plataformaempregos.repositories.ProcessamentoIARepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AsyncAIService {
    
    @Autowired
    private AIService aiService;
    
    @Autowired
    private ProcessamentoIARepository processamentoRepository;
    
    @Autowired
    private CandidaturaService candidaturaService;
    
    @Autowired
    private CandidaturaRepository candidaturaRepository;
    
    @Autowired
    private VagaService vagaService;
    
    @Autowired
    private CandidatoService candidatoService;
    
    @Autowired
    private NotificationService notificationService;
    
    /**
     * Processa análise de compatibilidade de candidatura de forma assíncrona
     */
    /**
     * Cria registro de processamento e retorna imediatamente
     */
    @Transactional
    public ProcessamentoIA criarProcessamentoAnaliseCandidatura(Long candidaturaId, Long usuarioId) {
        ProcessamentoIA processamento = ProcessamentoIA.builder()
                .tipoProcessamento(TipoProcessamentoIA.ANALISE_CANDIDATURA)
                .status(StatusProcessamentoIA.PENDENTE)
                .entidadeId(candidaturaId)
                .entidadeTipo("CANDIDATURA")
                .usuarioId(usuarioId)
                .progresso(0)
                .dataInicio(LocalDateTime.now())
                .build();
        return processamentoRepository.save(processamento);
    }
    
    @Async("aiTaskExecutor")
    @Transactional
    public CompletableFuture<ProcessamentoIA> processarAnaliseCandidaturaAsync(
            Long processamentoId) {
        
        LocalDateTime inicio = LocalDateTime.now();
        ProcessamentoIA processamento = processamentoRepository.findById(processamentoId)
                .orElseThrow(() -> new RuntimeException("Processamento não encontrado"));
        
        try {
            // Atualizar status para PROCESSANDO
            processamento.setStatus(StatusProcessamentoIA.PROCESSANDO);
            processamento.setDataInicio(inicio);
            processamento = processamentoRepository.save(processamento);
            
            Long candidaturaId = processamento.getEntidadeId();
            
            // Obter candidatura
            Candidatura candidatura = candidaturaRepository.findById(candidaturaId)
                    .orElseThrow(() -> new RuntimeException("Candidatura não encontrada"));
            
            // Atualizar progresso
            atualizarProgresso(processamento.getId(), 30);
            
            // Calcular compatibilidade
            Double score = aiService.calcularCompatibilidade(
                    candidatura.getCandidato(), 
                    candidatura.getVaga());
            
            atualizarProgresso(processamento.getId(), 60);
            
            // Gerar observações
            String observacoes = aiService.gerarObservacoesCompatibilidade(
                    candidatura.getCandidato(), 
                    candidatura.getVaga(), 
                    score);
            
            atualizarProgresso(processamento.getId(), 90);
            
            // Atualizar candidatura com score e observações
            candidatura.setScoreCompatibilidade(score);
            candidatura.setObservacoesIA(observacoes);
            candidaturaRepository.save(candidatura);
            
            // Finalizar processamento
            LocalDateTime fim = LocalDateTime.now();
            long tempoProcessamento = java.time.Duration.between(inicio, fim).toMillis();
            
            processamento.setStatus(StatusProcessamentoIA.CONCLUIDO);
            processamento.setScoreCalculado(score);
            processamento.setObservacoes(observacoes);
            processamento.setProgresso(100);
            processamento.setDataConclusao(fim);
            processamento.setTempoProcessamentoMs(tempoProcessamento);
            processamento.setResultado(String.format(
                    "{\"score\": %.2f, \"observacoes\": \"%s\"}", 
                    score, observacoes.replace("\"", "\\\"")));
            
            processamento = processamentoRepository.save(processamento);
            
            // Notificar conclusão
            notificationService.notificarProcessamentoConcluido(processamento);
            
            log.info("Processamento de análise de candidatura {} concluído em {}ms", 
                    candidaturaId, tempoProcessamento);
            
            return CompletableFuture.completedFuture(processamento);
            
        } catch (Exception e) {
            log.error("Erro ao processar análise de candidatura {}", candidaturaId, e);
            
            if (processamento != null) {
                processamento.setStatus(StatusProcessamentoIA.ERRO);
                processamento.setErro(e.getMessage());
                processamento.setDataConclusao(LocalDateTime.now());
                processamentoRepository.save(processamento);
                
                notificationService.notificarErroProcessamento(processamento, e);
            }
            
            return CompletableFuture.failedFuture(e);
        }
    }
    
    /**
     * Cria registro de processamento de recomendação de candidatos
     */
    @Transactional
    public ProcessamentoIA criarProcessamentoRecomendacaoCandidatos(Long vagaId, int limite, Long usuarioId) {
        ProcessamentoIA processamento = ProcessamentoIA.builder()
                .tipoProcessamento(TipoProcessamentoIA.RECOMENDACAO_CANDIDATOS)
                .status(StatusProcessamentoIA.PENDENTE)
                .entidadeId(vagaId)
                .entidadeTipo("VAGA")
                .usuarioId(usuarioId)
                .progresso(0)
                .dataInicio(LocalDateTime.now())
                .build();
        return processamentoRepository.save(processamento);
    }
    
    /**
     * Processa recomendação de candidatos para uma vaga de forma assíncrona
     */
    @Async("aiTaskExecutor")
    @Transactional
    public CompletableFuture<ProcessamentoIA> processarRecomendacaoCandidatosAsync(Long processamentoId) {
        
        LocalDateTime inicio = LocalDateTime.now();
        ProcessamentoIA processamento = processamentoRepository.findById(processamentoId)
                .orElseThrow(() -> new RuntimeException("Processamento não encontrado"));
        
        try {
            processamento.setStatus(StatusProcessamentoIA.PROCESSANDO);
            processamento.setDataInicio(inicio);
            processamento = processamentoRepository.save(processamento);
            
            Long vagaId = processamento.getEntidadeId();
            
            atualizarProgresso(processamento.getId(), 20);
            
            // Buscar candidatos recomendados (assumindo limite padrão de 10)
            int limite = 10; // Pode ser armazenado no processamento se necessário
            var candidatos = aiService.buscarCandidatosRecomendados(vagaId, limite);
            
            atualizarProgresso(processamento.getId(), 80);
            
            // Preparar resultado
            Vaga vaga = vagaService.obterPorId(vagaId).orElse(null);
            StringBuilder resultado = new StringBuilder("{\"candidatos\":[");
            for (int i = 0; i < candidatos.size(); i++) {
                var candidato = candidatos.get(i);
                if (vaga != null) {
                    Double score = aiService.calcularCompatibilidade(candidato, vaga);
                    if (i > 0) resultado.append(",");
                    resultado.append(String.format(
                            "{\"id\":%d,\"nome\":\"%s\",\"score\":%.2f}",
                            candidato.getId(), candidato.getNome(), score));
                }
            }
            resultado.append("]}");
            
            LocalDateTime fim = LocalDateTime.now();
            long tempoProcessamento = java.time.Duration.between(inicio, fim).toMillis();
            
            processamento.setStatus(StatusProcessamentoIA.CONCLUIDO);
            processamento.setProgresso(100);
            processamento.setDataConclusao(fim);
            processamento.setTempoProcessamentoMs(tempoProcessamento);
            processamento.setResultado(resultado.toString());
            
            processamento = processamentoRepository.save(processamento);
            
            notificationService.notificarProcessamentoConcluido(processamento);
            
            log.info("Processamento de recomendação de candidatos para vaga {} concluído em {}ms", 
                    vagaId, tempoProcessamento);
            
            return CompletableFuture.completedFuture(processamento);
            
        } catch (Exception e) {
            log.error("Erro ao processar recomendação de candidatos para vaga {}", vagaId, e);
            
            if (processamento != null) {
                processamento.setStatus(StatusProcessamentoIA.ERRO);
                processamento.setErro(e.getMessage());
                processamento.setDataConclusao(LocalDateTime.now());
                processamentoRepository.save(processamento);
            }
            
            return CompletableFuture.failedFuture(e);
        }
    }
    
    /**
     * Cria registro de processamento de recomendação de vagas
     */
    @Transactional
    public ProcessamentoIA criarProcessamentoRecomendacaoVagas(Long candidatoId, int limite, Long usuarioId) {
        ProcessamentoIA processamento = ProcessamentoIA.builder()
                .tipoProcessamento(TipoProcessamentoIA.RECOMENDACAO_VAGAS)
                .status(StatusProcessamentoIA.PENDENTE)
                .entidadeId(candidatoId)
                .entidadeTipo("CANDIDATO")
                .usuarioId(usuarioId)
                .progresso(0)
                .dataInicio(LocalDateTime.now())
                .build();
        return processamentoRepository.save(processamento);
    }
    
    /**
     * Processa recomendação de vagas para um candidato de forma assíncrona
     */
    @Async("aiTaskExecutor")
    @Transactional
    public CompletableFuture<ProcessamentoIA> processarRecomendacaoVagasAsync(Long processamentoId) {
        
        LocalDateTime inicio = LocalDateTime.now();
        ProcessamentoIA processamento = processamentoRepository.findById(processamentoId)
                .orElseThrow(() -> new RuntimeException("Processamento não encontrado"));
        
        try {
            processamento.setStatus(StatusProcessamentoIA.PROCESSANDO);
            processamento.setDataInicio(inicio);
            processamento = processamentoRepository.save(processamento);
            
            Long candidatoId = processamento.getEntidadeId();
            
            atualizarProgresso(processamento.getId(), 20);
            
            // Buscar vagas recomendadas (assumindo limite padrão de 10)
            int limite = 10; // Pode ser armazenado no processamento se necessário
            var vagas = aiService.buscarVagasRecomendadas(candidatoId, limite);
            
            atualizarProgresso(processamento.getId(), 80);
            
            // Preparar resultado
            StringBuilder resultado = new StringBuilder("{\"vagas\":[");
            Candidato candidato = candidatoService.obterPorId(candidatoId).orElse(null);
            if (candidato != null) {
                for (int i = 0; i < vagas.size(); i++) {
                    var vaga = vagas.get(i);
                    Double score = aiService.calcularCompatibilidade(candidato, vaga);
                    if (i > 0) resultado.append(",");
                    resultado.append(String.format(
                            "{\"id\":%d,\"titulo\":\"%s\",\"score\":%.2f}",
                            vaga.getId(), vaga.getTitulo(), score));
                }
            }
            resultado.append("]}");
            
            LocalDateTime fim = LocalDateTime.now();
            long tempoProcessamento = java.time.Duration.between(inicio, fim).toMillis();
            
            processamento.setStatus(StatusProcessamentoIA.CONCLUIDO);
            processamento.setProgresso(100);
            processamento.setDataConclusao(fim);
            processamento.setTempoProcessamentoMs(tempoProcessamento);
            processamento.setResultado(resultado.toString());
            
            processamento = processamentoRepository.save(processamento);
            
            notificationService.notificarProcessamentoConcluido(processamento);
            
            log.info("Processamento de recomendação de vagas para candidato {} concluído em {}ms", 
                    candidatoId, tempoProcessamento);
            
            return CompletableFuture.completedFuture(processamento);
            
        } catch (Exception e) {
            log.error("Erro ao processar recomendação de vagas para candidato {}", candidatoId, e);
            
            if (processamento != null) {
                processamento.setStatus(StatusProcessamentoIA.ERRO);
                processamento.setErro(e.getMessage());
                processamento.setDataConclusao(LocalDateTime.now());
                processamentoRepository.save(processamento);
            }
            
            return CompletableFuture.failedFuture(e);
        }
    }
    
    /**
     * Processa análise de perfil profissional de forma assíncrona
     */
    @Async("aiTaskExecutor")
    @Transactional
    public CompletableFuture<ProcessamentoIA> processarAnalisePerfilAsync(
            Long perfilId, Long usuarioId) {
        
        LocalDateTime inicio = LocalDateTime.now();
        ProcessamentoIA processamento = null;
        
        try {
            processamento = ProcessamentoIA.builder()
                    .tipoProcessamento(TipoProcessamentoIA.ANALISE_PERFIL)
                    .status(StatusProcessamentoIA.PROCESSANDO)
                    .entidadeId(perfilId)
                    .entidadeTipo("PERFIL_PROFISSIONAL")
                    .usuarioId(usuarioId)
                    .progresso(0)
                    .dataInicio(inicio)
                    .build();
            processamento = processamentoRepository.save(processamento);
            
            // TODO: Implementar análise detalhada do perfil
            // Por enquanto, apenas simula processamento
            
            Thread.sleep(1000); // Simula processamento
            
            LocalDateTime fim = LocalDateTime.now();
            long tempoProcessamento = java.time.Duration.between(inicio, fim).toMillis();
            
            processamento.setStatus(StatusProcessamentoIA.CONCLUIDO);
            processamento.setProgresso(100);
            processamento.setDataConclusao(fim);
            processamento.setTempoProcessamentoMs(tempoProcessamento);
            processamento.setResultado("{\"status\":\"analisado\"}");
            
            processamento = processamentoRepository.save(processamento);
            
            notificationService.notificarProcessamentoConcluido(processamento);
            
            return CompletableFuture.completedFuture(processamento);
            
        } catch (Exception e) {
            log.error("Erro ao processar análise de perfil {}", perfilId, e);
            
            if (processamento != null) {
                processamento.setStatus(StatusProcessamentoIA.ERRO);
                processamento.setErro(e.getMessage());
                processamento.setDataConclusao(LocalDateTime.now());
                processamentoRepository.save(processamento);
            }
            
            return CompletableFuture.failedFuture(e);
        }
    }
    
    /**
     * Atualiza progresso do processamento
     */
    @Transactional
    public void atualizarProgresso(Long processamentoId, Integer progresso) {
        processamentoRepository.findById(processamentoId).ifPresent(p -> {
            p.setProgresso(progresso);
            processamentoRepository.save(p);
        });
    }
}
