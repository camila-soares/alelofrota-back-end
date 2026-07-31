package com.plataformaempregos.services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.plataformaempregos.domain.Candidato;
import com.plataformaempregos.domain.Competencia;
import com.plataformaempregos.domain.PerfilProfissional;
import com.plataformaempregos.domain.Vaga;
import com.plataformaempregos.repositories.CandidatoRepository;
import com.plataformaempregos.repositories.VagaRepository;

@Service
public class AIService {
    
    @Autowired
    private CandidatoRepository candidatoRepository;
    
    @Autowired
    private VagaRepository vagaRepository;
    
    @Value("${ai.enabled:true}")
    private boolean aiEnabled;
    
    @Value("${ai.api.url:}")
    private String aiApiUrl;
    
    @Value("${ai.api.key:}")
    private String aiApiKey;
    
    /**
     * Calcula a compatibilidade entre um candidato e uma vaga
     * Retorna um score de 0.0 a 100.0
     */
    public Double calcularCompatibilidade(Candidato candidato, Vaga vaga) {
        if (!aiEnabled) {
            return calcularCompatibilidadeBasica(candidato, vaga);
        }
        
        try {
            return calcularCompatibilidadeComIA(candidato, vaga);
        } catch (Exception e) {
            // Fallback para cálculo básico em caso de erro
            return calcularCompatibilidadeBasica(candidato, vaga);
        }
    }
    
    /**
     * Cálculo básico de compatibilidade sem IA
     */
    private Double calcularCompatibilidadeBasica(Candidato candidato, Vaga vaga) {
        double score = 0.0;
        int fatores = 0;
        
        PerfilProfissional perfil = candidato.getPerfilProfissional();
        if (perfil == null) {
            return 0.0;
        }
        
        // Comparação de competências (peso: 40%)
        if (vaga.getCompetenciasRequeridas() != null && !vaga.getCompetenciasRequeridas().isEmpty()) {
            if (perfil.getCompetencias() != null && !perfil.getCompetencias().isEmpty()) {
                List<Long> competenciasVaga = vaga.getCompetenciasRequeridas().stream()
                        .map(Competencia::getId)
                        .collect(Collectors.toList());
                
                List<Long> competenciasCandidato = perfil.getCompetencias().stream()
                        .map(Competencia::getId)
                        .collect(Collectors.toList());
                
                long competenciasMatch = competenciasCandidato.stream()
                        .filter(competenciasVaga::contains)
                        .count();
                
                double matchPercent = (double) competenciasMatch / competenciasVaga.size();
                score += matchPercent * 40.0;
            }
            fatores++;
        }
        
        // Comparação de nível de experiência (peso: 20%)
        if (vaga.getNivelExperiencia() != null && perfil.getExperiencias() != null) {
            boolean nivelCompativel = perfil.getExperiencias().stream()
                    .anyMatch(exp -> exp.getNivelExperiencia() == vaga.getNivelExperiencia() ||
                                   (exp.getNivelExperiencia().ordinal() >= vaga.getNivelExperiencia().ordinal()));
            
            if (nivelCompativel) {
                score += 20.0;
            }
            fatores++;
        }
        
        // Comparação de pretensão salarial (peso: 15%)
        if (vaga.getSalarioMaximo() != null && perfil.getPretensaoSalarial() != null) {
            if (perfil.getPretensaoSalarial() <= vaga.getSalarioMaximo()) {
                double percentual = 1.0 - ((perfil.getPretensaoSalarial() - vaga.getSalarioMinimo()) / 
                                          (vaga.getSalarioMaximo() - vaga.getSalarioMinimo()));
                score += percentual * 15.0;
            }
            fatores++;
        }
        
        // Comparação de modalidade de trabalho (peso: 15%)
        if (vaga.getModalidadeTrabalho() != null && perfil.getDisponibilidade() != null) {
            if (vaga.getModalidadeTrabalho().equalsIgnoreCase(perfil.getDisponibilidade()) ||
                vaga.getModalidadeTrabalho().equalsIgnoreCase("HIBRIDO") ||
                perfil.getDisponibilidade().equalsIgnoreCase("HIBRIDO")) {
                score += 15.0;
            }
            fatores++;
        }
        
        // Área de atuação (peso: 10%)
        if (vaga.getDescricao() != null && perfil.getAreaAtuacao() != null) {
            if (vaga.getDescricao().toLowerCase().contains(perfil.getAreaAtuacao().toLowerCase()) ||
                vaga.getTitulo().toLowerCase().contains(perfil.getAreaAtuacao().toLowerCase())) {
                score += 10.0;
            }
            fatores++;
        }
        
        return Math.min(100.0, Math.max(0.0, score));
    }
    
    /**
     * Cálculo de compatibilidade usando IA externa (OpenAI, etc)
     */
    private Double calcularCompatibilidadeComIA(Candidato candidato, Vaga vaga) {
        // TODO: Implementar integração com API de IA externa
        // Por enquanto, usa o cálculo básico
        return calcularCompatibilidadeBasica(candidato, vaga);
    }
    
    /**
     * Gera observações sobre a compatibilidade usando IA
     */
    public String gerarObservacoesCompatibilidade(Candidato candidato, Vaga vaga, Double score) {
        StringBuilder observacoes = new StringBuilder();
        
        PerfilProfissional perfil = candidato.getPerfilProfissional();
        if (perfil == null) {
            return "Perfil profissional incompleto.";
        }
        
        observacoes.append(String.format("Score de compatibilidade: %.2f%%\n\n", score));
        
        // Análise de competências
        if (vaga.getCompetenciasRequeridas() != null && !vaga.getCompetenciasRequeridas().isEmpty()) {
            if (perfil.getCompetencias() != null && !perfil.getCompetencias().isEmpty()) {
                List<String> competenciasMatch = vaga.getCompetenciasRequeridas().stream()
                        .filter(comp -> perfil.getCompetencias().contains(comp))
                        .map(Competencia::getNome)
                        .collect(Collectors.toList());
                
                List<String> competenciasFaltantes = vaga.getCompetenciasRequeridas().stream()
                        .filter(comp -> !perfil.getCompetencias().contains(comp))
                        .map(Competencia::getNome)
                        .collect(Collectors.toList());
                
                if (!competenciasMatch.isEmpty()) {
                    observacoes.append("Competências compatíveis: ").append(String.join(", ", competenciasMatch)).append("\n");
                }
                
                if (!competenciasFaltantes.isEmpty()) {
                    observacoes.append("Competências em falta: ").append(String.join(", ", competenciasFaltantes)).append("\n");
                }
            }
        }
        
        // Análise de experiência
        if (perfil.getExperiencias() != null && !perfil.getExperiencias().isEmpty()) {
            observacoes.append("\nExperiência profissional relevante encontrada.\n");
        }
        
        // Análise salarial
        if (vaga.getSalarioMaximo() != null && perfil.getPretensaoSalarial() != null) {
            if (perfil.getPretensaoSalarial() <= vaga.getSalarioMaximo()) {
                observacoes.append("Pretensão salarial compatível com a faixa oferecida.\n");
            } else {
                observacoes.append("Pretensão salarial acima da faixa oferecida.\n");
            }
        }
        
        return observacoes.toString();
    }
    
    /**
     * Processa os requisitos da vaga usando IA para facilitar o matching
     */
    public String processarRequisitosVaga(Vaga vaga) {
        // Extrai informações estruturadas dos requisitos para facilitar o matching
        StringBuilder requisitosProcessados = new StringBuilder();
        
        requisitosProcessados.append("Título: ").append(vaga.getTitulo()).append("\n");
        requisitosProcessados.append("Descrição: ").append(vaga.getDescricao()).append("\n");
        requisitosProcessados.append("Requisitos: ").append(vaga.getRequisitos()).append("\n");
        
        if (vaga.getNivelExperiencia() != null) {
            requisitosProcessados.append("Nível: ").append(vaga.getNivelExperiencia()).append("\n");
        }
        
        if (vaga.getCompetenciasRequeridas() != null && !vaga.getCompetenciasRequeridas().isEmpty()) {
            requisitosProcessados.append("Competências: ");
            requisitosProcessados.append(vaga.getCompetenciasRequeridas().stream()
                    .map(Competencia::getNome)
                    .collect(Collectors.joining(", ")));
        }
        
        return requisitosProcessados.toString();
    }
    
    /**
     * Busca candidatos recomendados para uma vaga
     */
    public List<Candidato> buscarCandidatosRecomendados(Long vagaId, int limite) {
        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));
        
        List<Candidato> candidatosAtivos = candidatoRepository.findByAtivoTrue();
        
        return candidatosAtivos.stream()
                .filter(c -> c.getPerfilProfissional() != null)
                .sorted((c1, c2) -> {
                    Double score1 = calcularCompatibilidade(c1, vaga);
                    Double score2 = calcularCompatibilidade(c2, vaga);
                    return score2.compareTo(score1); // Ordena do maior para o menor
                })
                .limit(limite)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca vagas recomendadas para um candidato
     */
    public List<Vaga> buscarVagasRecomendadas(Long candidatoId, int limite) {
        Candidato candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));
        
        if (candidato.getPerfilProfissional() == null) {
            return Collections.emptyList();
        }
        
        // Busca todas as vagas abertas e calcula compatibilidade
        List<Vaga> vagasAbertas = vagaRepository.findAll().stream()
                .filter(v -> v.getStatus().equals(com.plataformaempregos.enums.StatusVaga.ABERTA))
                .sorted((v1, v2) -> {
                    Double score1 = calcularCompatibilidade(candidato, v1);
                    Double score2 = calcularCompatibilidade(candidato, v2);
                    return score2.compareTo(score1); // Ordena do maior para o menor
                })
                .limit(limite)
                .collect(Collectors.toList());
        
        return vagasAbertas;
    }
}
