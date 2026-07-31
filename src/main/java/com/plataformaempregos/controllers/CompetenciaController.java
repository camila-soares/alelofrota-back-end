package com.plataformaempregos.controllers;

import java.util.List;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plataformaempregos.domain.Competencia;
import com.plataformaempregos.dtos.CompetenciaDTO;
import com.plataformaempregos.repositories.CompetenciaRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/competencias")
@Api(tags = "Competências")
public class CompetenciaController {
    
    @Autowired
    private CompetenciaRepository competenciaRepository;
    
    @PostMapping
    @ApiOperation("Criar nova competência")
    public ResponseEntity<CompetenciaDTO> criarCompetencia(@RequestBody CompetenciaDTO competenciaDTO) {
        if (competenciaRepository.existsByNome(competenciaDTO.getNome())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        Competencia competencia = Competencia.builder()
                .nome(competenciaDTO.getNome())
                .categoria(competenciaDTO.getCategoria())
                .descricao(competenciaDTO.getDescricao())
                .build();
        
        competencia = competenciaRepository.save(competencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(competencia));
    }
    
    @GetMapping("/{id}")
    @ApiOperation("Obter competência por ID")
    public ResponseEntity<CompetenciaDTO> obterCompetencia(@PathVariable Long id) {
        return competenciaRepository.findById(id)
                .map(competencia -> ResponseEntity.ok(converterParaDTO(competencia)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @ApiOperation("Listar competências")
    public ResponseEntity<List<CompetenciaDTO>> listarCompetencias(
            @RequestParam(required = false) String categoria) {
        List<Competencia> competencias;
        
        if (categoria != null && !categoria.isEmpty()) {
            competencias = competenciaRepository.findByCategoria(categoria);
        } else {
            competencias = competenciaRepository.findAll();
        }
        
        List<CompetenciaDTO> competenciasDTO = competencias.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(competenciasDTO);
    }
    
    @PutMapping("/{id}")
    @ApiOperation("Atualizar competência")
    public ResponseEntity<CompetenciaDTO> atualizarCompetencia(
            @PathVariable Long id,
            @RequestBody CompetenciaDTO competenciaDTO) {
        return competenciaRepository.findById(id)
                .map(competencia -> {
                    competencia.setNome(competenciaDTO.getNome());
                    competencia.setCategoria(competenciaDTO.getCategoria());
                    competencia.setDescricao(competenciaDTO.getDescricao());
                    competencia = competenciaRepository.save(competencia);
                    return ResponseEntity.ok(converterParaDTO(competencia));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    private CompetenciaDTO converterParaDTO(Competencia competencia) {
        return CompetenciaDTO.builder()
                .id(competencia.getId())
                .nome(competencia.getNome())
                .categoria(competencia.getCategoria())
                .descricao(competencia.getDescricao())
                .build();
    }
}
