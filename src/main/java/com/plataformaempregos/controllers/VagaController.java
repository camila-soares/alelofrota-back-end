package com.plataformaempregos.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.plataformaempregos.domain.Vaga;
import com.plataformaempregos.dtos.VagaDTO;
import com.plataformaempregos.services.VagaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/vagas")
@Api(tags = "Vagas")
public class VagaController {
    
    @Autowired
    private VagaService vagaService;
    
    @PostMapping("/empresa/{empresaId}")
    @ApiOperation("Criar nova vaga")
    public ResponseEntity<VagaDTO> criarVaga(@PathVariable Long empresaId, @RequestBody VagaDTO vagaDTO) {
        try {
            Vaga vaga = vagaService.criarVaga(empresaId, vagaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(vaga));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/{id}")
    @ApiOperation("Obter vaga por ID")
    public ResponseEntity<VagaDTO> obterVaga(@PathVariable Long id) {
        return vagaService.obterPorId(id)
                .map(vaga -> ResponseEntity.ok(converterParaDTO(vaga)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @ApiOperation("Listar vagas abertas")
    public ResponseEntity<Page<VagaDTO>> listarVagas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Vaga> vagas = vagaService.listarVagasAbertas(pageable);
        Page<VagaDTO> vagasDTO = vagas.map(this::converterParaDTO);
        return ResponseEntity.ok(vagasDTO);
    }
    
    @GetMapping("/buscar")
    @ApiOperation("Buscar vagas por termo")
    public ResponseEntity<Page<VagaDTO>> buscarVagas(
            @RequestParam String termo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Vaga> vagas = vagaService.buscarVagasPorTermo(termo, pageable);
        Page<VagaDTO> vagasDTO = vagas.map(this::converterParaDTO);
        return ResponseEntity.ok(vagasDTO);
    }
    
    @GetMapping("/empresa/{empresaId}")
    @ApiOperation("Listar vagas de uma empresa")
    public ResponseEntity<List<VagaDTO>> listarVagasPorEmpresa(@PathVariable Long empresaId) {
        try {
            List<VagaDTO> vagas = vagaService.listarVagasPorEmpresa(empresaId).stream()
                    .map(this::converterParaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(vagas);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    @ApiOperation("Atualizar vaga")
    public ResponseEntity<VagaDTO> atualizarVaga(@PathVariable Long id, @RequestBody VagaDTO vagaDTO) {
        try {
            Vaga vaga = vagaService.atualizarVaga(id, vagaDTO);
            return ResponseEntity.ok(converterParaDTO(vaga));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/fechar")
    @ApiOperation("Fechar vaga")
    public ResponseEntity<Void> fecharVaga(@PathVariable Long id) {
        try {
            vagaService.fecharVaga(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    private VagaDTO converterParaDTO(Vaga vaga) {
        return VagaDTO.builder()
                .id(vaga.getId())
                .empresaId(vaga.getEmpresa() != null ? vaga.getEmpresa().getId() : null)
                .empresaNome(vaga.getEmpresa() != null ? vaga.getEmpresa().getNomeFantasia() : null)
                .titulo(vaga.getTitulo())
                .descricao(vaga.getDescricao())
                .requisitos(vaga.getRequisitos())
                .beneficios(vaga.getBeneficios())
                .salarioMinimo(vaga.getSalarioMinimo())
                .salarioMaximo(vaga.getSalarioMaximo())
                .nivelExperiencia(vaga.getNivelExperiencia())
                .tipoContratacao(vaga.getTipoContratacao())
                .modalidadeTrabalho(vaga.getModalidadeTrabalho())
                .localizacao(vaga.getLocalizacao())
                .status(vaga.getStatus())
                .competenciasRequeridasIds(vaga.getCompetenciasRequeridas() != null ?
                        vaga.getCompetenciasRequeridas().stream()
                                .map(comp -> comp.getId())
                                .collect(Collectors.toList()) : null)
                .dataPublicacao(vaga.getDataPublicacao())
                .dataFechamento(vaga.getDataFechamento())
                .build();
    }
}
