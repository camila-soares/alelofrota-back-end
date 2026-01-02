package com.plataformaempregos.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plataformaempregos.domain.Candidato;
import com.plataformaempregos.domain.Candidatura;
import com.plataformaempregos.domain.Vaga;
import com.plataformaempregos.enums.StatusCandidatura;
import com.plataformaempregos.enums.StatusVaga;
import com.plataformaempregos.repositories.CandidatoRepository;
import com.plataformaempregos.repositories.CandidaturaRepository;
import com.plataformaempregos.repositories.VagaRepository;

@Service
public class CandidaturaService {
    
    @Autowired
    private CandidaturaRepository candidaturaRepository;
    
    @Autowired
    private CandidatoRepository candidatoRepository;
    
    @Autowired
    private VagaRepository vagaRepository;
    
    @Autowired
    private AIService aiService;
    
    @Transactional
    public Candidatura criarCandidatura(Long candidatoId, Long vagaId) {
        Candidato candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));
        
        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));
        
        if (vaga.getStatus() != StatusVaga.ABERTA) {
            throw new RuntimeException("Vaga não está aberta para candidaturas");
        }
        
        if (candidaturaRepository.existsByCandidatoAndVaga(candidato, vaga)) {
            throw new RuntimeException("Candidato já se candidatou para esta vaga");
        }
        
        Candidatura candidatura = Candidatura.builder()
                .candidato(candidato)
                .vaga(vaga)
                .status(StatusCandidatura.PENDENTE)
                .dataCandidatura(LocalDateTime.now())
                .build();
        
        // Calcular compatibilidade com IA
        Double score = aiService.calcularCompatibilidade(candidato, vaga);
        String observacoes = aiService.gerarObservacoesCompatibilidade(candidato, vaga, score);
        
        candidatura.setScoreCompatibilidade(score);
        candidatura.setObservacoesIA(observacoes);
        
        return candidaturaRepository.save(candidatura);
    }
    
    public Optional<Candidatura> obterPorId(Long id) {
        return candidaturaRepository.findById(id);
    }
    
    public List<Candidatura> listarCandidaturasPorCandidato(Long candidatoId) {
        Candidato candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));
        return candidaturaRepository.findByCandidato(candidato);
    }
    
    public List<Candidatura> listarCandidaturasPorVaga(Long vagaId) {
        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));
        return candidaturaRepository.findByVaga(vaga);
    }
    
    @Transactional
    public Candidatura atualizarStatus(Long id, StatusCandidatura status, String feedback) {
        Candidatura candidatura = candidaturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidatura não encontrada"));
        
        candidatura.setStatus(status);
        candidatura.setFeedbackEmpresa(feedback);
        candidatura.setDataAtualizacao(LocalDateTime.now());
        
        return candidaturaRepository.save(candidatura);
    }
}
