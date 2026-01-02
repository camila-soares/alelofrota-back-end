package com.plataformaempregos.controllers;

import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

import com.plataformaempregos.domain.Candidato;
import com.plataformaempregos.dtos.CandidatoDTO;
import com.plataformaempregos.services.CandidatoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/candidatos")
@Api(tags = "Candidatos")
public class CandidatoController {
    
    @Autowired
    private CandidatoService candidatoService;
    
    @PostMapping
    @ApiOperation("Cadastrar novo candidato")
    public ResponseEntity<CandidatoDTO> criarCandidato(@RequestBody CandidatoDTO candidatoDTO) {
        try {
            Candidato candidato = candidatoService.criarCandidato(candidatoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(candidato));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/{id}")
    @ApiOperation("Obter candidato por ID")
    public ResponseEntity<CandidatoDTO> obterCandidato(@PathVariable Long id) {
        return candidatoService.obterPorId(id)
                .map(candidato -> ResponseEntity.ok(converterParaDTO(candidato)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @ApiOperation("Listar candidatos ativos")
    public ResponseEntity<List<CandidatoDTO>> listarCandidatos() {
        List<CandidatoDTO> candidatos = candidatoService.listarCandidatosAtivos().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(candidatos);
    }
    
    @PutMapping("/{id}")
    @ApiOperation("Atualizar candidato")
    public ResponseEntity<CandidatoDTO> atualizarCandidato(@PathVariable Long id, @RequestBody CandidatoDTO candidatoDTO) {
        try {
            Candidato candidato = candidatoService.atualizarCandidato(id, candidatoDTO);
            return ResponseEntity.ok(converterParaDTO(candidato));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/curriculo")
    @ApiOperation("Atualizar currículo do candidato")
    public ResponseEntity<CandidatoDTO> atualizarCurriculo(@PathVariable Long id, @RequestBody String caminhoCurriculo) {
        try {
            Candidato candidato = candidatoService.atualizarCurriculo(id, caminhoCurriculo);
            return ResponseEntity.ok(converterParaDTO(candidato));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    private CandidatoDTO converterParaDTO(Candidato candidato) {
        return CandidatoDTO.builder()
                .id(candidato.getId())
                .email(candidato.getEmail())
                .nome(candidato.getNome())
                .cpf(candidato.getCpf())
                .dataNascimento(candidato.getDataNascimento())
                .telefone(candidato.getTelefone())
                .endereco(candidato.getEndereco())
                .cidade(candidato.getCidade())
                .estado(candidato.getEstado())
                .cep(candidato.getCep())
                .caminhoCurriculo(candidato.getCaminhoCurriculo())
                .perfilProfissionalId(candidato.getPerfilProfissional() != null ? candidato.getPerfilProfissional().getId() : null)
                .build();
    }
}
