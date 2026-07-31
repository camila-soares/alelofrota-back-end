package com.plataformaempregos.controllers;

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

import com.plataformaempregos.domain.PerfilProfissional;
import com.plataformaempregos.dtos.PerfilProfissionalDTO;
import com.plataformaempregos.services.PerfilProfissionalService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/perfis-profissionais")
@Api(tags = "Perfis Profissionais")
public class PerfilProfissionalController {
    
    @Autowired
    private PerfilProfissionalService perfilService;
    
    @PostMapping
    @ApiOperation("Criar perfil profissional")
    public ResponseEntity<PerfilProfissionalDTO> criarPerfil(@RequestBody PerfilProfissionalDTO perfilDTO) {
        try {
            PerfilProfissional perfil = perfilService.criarPerfil(perfilDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(perfil));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/{id}")
    @ApiOperation("Obter perfil profissional por ID")
    public ResponseEntity<PerfilProfissionalDTO> obterPerfil(@PathVariable Long id) {
        return perfilService.obterPorId(id)
                .map(perfil -> ResponseEntity.ok(converterParaDTO(perfil)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    @ApiOperation("Atualizar perfil profissional")
    public ResponseEntity<PerfilProfissionalDTO> atualizarPerfil(
            @PathVariable Long id,
            @RequestBody PerfilProfissionalDTO perfilDTO) {
        try {
            PerfilProfissional perfil = perfilService.atualizarPerfil(id, perfilDTO);
            return ResponseEntity.ok(converterParaDTO(perfil));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    private PerfilProfissionalDTO converterParaDTO(PerfilProfissional perfil) {
        // Implementação simplificada - pode ser expandida conforme necessário
        return PerfilProfissionalDTO.builder()
                .id(perfil.getId())
                .resumoProfissional(perfil.getResumoProfissional())
                .areaAtuacao(perfil.getAreaAtuacao())
                .pretensaoSalarial(perfil.getPretensaoSalarial())
                .disponibilidade(perfil.getDisponibilidade())
                .scoreIA(perfil.getScoreIA())
                .competenciasIds(perfil.getCompetencias() != null ?
                        perfil.getCompetencias().stream()
                                .map(comp -> comp.getId())
                                .collect(java.util.stream.Collectors.toList()) : null)
                .build();
    }
}
