package com.plataformaempregos.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.plataformaempregos.domain.Competencia;
import com.plataformaempregos.domain.Empresa;
import com.plataformaempregos.domain.Vaga;
import com.plataformaempregos.dtos.VagaDTO;
import com.plataformaempregos.enums.StatusVaga;
import com.plataformaempregos.repositories.CompetenciaRepository;
import com.plataformaempregos.repositories.EmpresaRepository;
import com.plataformaempregos.repositories.VagaRepository;

@Service
public class VagaService {
    
    @Autowired
    private VagaRepository vagaRepository;
    
    @Autowired
    private EmpresaRepository empresaRepository;
    
    @Autowired
    private CompetenciaRepository competenciaRepository;
    
    @Autowired
    private AIService aiService;
    
    @Transactional
    public Vaga criarVaga(Long empresaId, VagaDTO vagaDTO) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
        
        Vaga vaga = Vaga.builder()
                .empresa(empresa)
                .titulo(vagaDTO.getTitulo())
                .descricao(vagaDTO.getDescricao())
                .requisitos(vagaDTO.getRequisitos())
                .beneficios(vagaDTO.getBeneficios())
                .salarioMinimo(vagaDTO.getSalarioMinimo())
                .salarioMaximo(vagaDTO.getSalarioMaximo())
                .nivelExperiencia(vagaDTO.getNivelExperiencia())
                .tipoContratacao(vagaDTO.getTipoContratacao())
                .modalidadeTrabalho(vagaDTO.getModalidadeTrabalho())
                .localizacao(vagaDTO.getLocalizacao())
                .status(StatusVaga.ABERTA)
                .dataPublicacao(LocalDateTime.now())
                .build();
        
        if (vagaDTO.getCompetenciasRequeridasIds() != null && !vagaDTO.getCompetenciasRequeridasIds().isEmpty()) {
            List<Competencia> competencias = vagaDTO.getCompetenciasRequeridasIds().stream()
                    .map(id -> competenciaRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Competência não encontrada: " + id)))
                    .collect(Collectors.toList());
            vaga.setCompetenciasRequeridas(competencias);
        }
        
        // Processar requisitos com IA
        String requisitosIA = aiService.processarRequisitosVaga(vaga);
        vaga.setRequisitosIA(requisitosIA);
        
        return vagaRepository.save(vaga);
    }
    
    public Optional<Vaga> obterPorId(Long id) {
        return vagaRepository.findById(id);
    }
    
    public Page<Vaga> listarVagasAbertas(Pageable pageable) {
        return vagaRepository.findByStatus(StatusVaga.ABERTA, pageable);
    }
    
    public Page<Vaga> buscarVagasPorTermo(String termo, Pageable pageable) {
        return vagaRepository.buscarPorTermo(termo, StatusVaga.ABERTA, pageable);
    }
    
    public List<Vaga> listarVagasPorEmpresa(Long empresaId) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
        return vagaRepository.findByEmpresa(empresa);
    }
    
    @Transactional
    public Vaga atualizarVaga(Long id, VagaDTO vagaDTO) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));
        
        vaga.setTitulo(vagaDTO.getTitulo());
        vaga.setDescricao(vagaDTO.getDescricao());
        vaga.setRequisitos(vagaDTO.getRequisitos());
        vaga.setBeneficios(vagaDTO.getBeneficios());
        vaga.setSalarioMinimo(vagaDTO.getSalarioMinimo());
        vaga.setSalarioMaximo(vagaDTO.getSalarioMaximo());
        vaga.setNivelExperiencia(vagaDTO.getNivelExperiencia());
        vaga.setTipoContratacao(vagaDTO.getTipoContratacao());
        vaga.setModalidadeTrabalho(vagaDTO.getModalidadeTrabalho());
        vaga.setLocalizacao(vagaDTO.getLocalizacao());
        vaga.setStatus(vagaDTO.getStatus());
        
        if (vagaDTO.getCompetenciasRequeridasIds() != null) {
            List<Competencia> competencias = vagaDTO.getCompetenciasRequeridasIds().stream()
                    .map(compId -> competenciaRepository.findById(compId)
                            .orElseThrow(() -> new RuntimeException("Competência não encontrada: " + compId)))
                    .collect(Collectors.toList());
            vaga.setCompetenciasRequeridas(competencias);
        }
        
        // Reprocessar requisitos com IA
        String requisitosIA = aiService.processarRequisitosVaga(vaga);
        vaga.setRequisitosIA(requisitosIA);
        
        return vagaRepository.save(vaga);
    }
    
    @Transactional
    public void fecharVaga(Long id) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));
        vaga.setStatus(StatusVaga.FECHADA);
        vaga.setDataFechamento(LocalDateTime.now());
        vagaRepository.save(vaga);
    }
}
