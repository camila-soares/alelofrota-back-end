package com.plataformaempregos.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plataformaempregos.domain.Competencia;
import com.plataformaempregos.domain.ExperienciaProfissional;
import com.plataformaempregos.domain.PerfilProfissional;
import com.plataformaempregos.dtos.ExperienciaProfissionalDTO;
import com.plataformaempregos.dtos.PerfilProfissionalDTO;
import com.plataformaempregos.repositories.CompetenciaRepository;
import com.plataformaempregos.repositories.ExperienciaProfissionalRepository;
import com.plataformaempregos.repositories.PerfilProfissionalRepository;

@Service
public class PerfilProfissionalService {
    
    @Autowired
    private PerfilProfissionalRepository perfilRepository;
    
    @Autowired
    private CompetenciaRepository competenciaRepository;
    
    @Autowired
    private ExperienciaProfissionalRepository experienciaRepository;
    
    @Transactional
    public PerfilProfissional criarPerfil(PerfilProfissionalDTO perfilDTO) {
        PerfilProfissional perfil = PerfilProfissional.builder()
                .resumoProfissional(perfilDTO.getResumoProfissional())
                .areaAtuacao(perfilDTO.getAreaAtuacao())
                .pretensaoSalarial(perfilDTO.getPretensaoSalarial())
                .disponibilidade(perfilDTO.getDisponibilidade())
                .build();
        
        if (perfilDTO.getCompetenciasIds() != null && !perfilDTO.getCompetenciasIds().isEmpty()) {
            List<Competencia> competencias = perfilDTO.getCompetenciasIds().stream()
                    .map(id -> competenciaRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Competência não encontrada: " + id)))
                    .collect(Collectors.toList());
            perfil.setCompetencias(competencias);
        }
        
        PerfilProfissional perfilSalvo = perfilRepository.save(perfil);
        
        if (perfilDTO.getExperiencias() != null && !perfilDTO.getExperiencias().isEmpty()) {
            for (ExperienciaProfissionalDTO expDTO : perfilDTO.getExperiencias()) {
                ExperienciaProfissional experiencia = ExperienciaProfissional.builder()
                        .perfilProfissional(perfilSalvo)
                        .empresa(expDTO.getEmpresa())
                        .cargo(expDTO.getCargo())
                        .nivelExperiencia(expDTO.getNivelExperiencia())
                        .dataInicio(expDTO.getDataInicio())
                        .dataFim(expDTO.getDataFim())
                        .empregoAtual(expDTO.getEmpregoAtual())
                        .descricao(expDTO.getDescricao())
                        .build();
                experienciaRepository.save(experiencia);
            }
        }
        
        return perfilSalvo;
    }
    
    public Optional<PerfilProfissional> obterPorId(Long id) {
        return perfilRepository.findById(id);
    }
    
    @Transactional
    public PerfilProfissional atualizarPerfil(Long id, PerfilProfissionalDTO perfilDTO) {
        PerfilProfissional perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));
        
        perfil.setResumoProfissional(perfilDTO.getResumoProfissional());
        perfil.setAreaAtuacao(perfilDTO.getAreaAtuacao());
        perfil.setPretensaoSalarial(perfilDTO.getPretensaoSalarial());
        perfil.setDisponibilidade(perfilDTO.getDisponibilidade());
        
        if (perfilDTO.getCompetenciasIds() != null) {
            List<Competencia> competencias = perfilDTO.getCompetenciasIds().stream()
                    .map(compId -> competenciaRepository.findById(compId)
                            .orElseThrow(() -> new RuntimeException("Competência não encontrada: " + compId)))
                    .collect(Collectors.toList());
            perfil.setCompetencias(competencias);
        }
        
        return perfilRepository.save(perfil);
    }
}
