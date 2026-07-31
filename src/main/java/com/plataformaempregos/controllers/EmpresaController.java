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

import com.plataformaempregos.domain.Empresa;
import com.plataformaempregos.dtos.EmpresaDTO;
import com.plataformaempregos.services.EmpresaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/empresas")
@Api(tags = "Empresas")
public class EmpresaController {
    
    @Autowired
    private EmpresaService empresaService;
    
    @PostMapping
    @ApiOperation("Cadastrar nova empresa")
    public ResponseEntity<EmpresaDTO> criarEmpresa(@RequestBody EmpresaDTO empresaDTO) {
        try {
            Empresa empresa = empresaService.criarEmpresa(empresaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(empresa));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/{id}")
    @ApiOperation("Obter empresa por ID")
    public ResponseEntity<EmpresaDTO> obterEmpresa(@PathVariable Long id) {
        return empresaService.obterPorId(id)
                .map(empresa -> ResponseEntity.ok(converterParaDTO(empresa)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @ApiOperation("Listar empresas ativas")
    public ResponseEntity<List<EmpresaDTO>> listarEmpresas() {
        List<EmpresaDTO> empresas = empresaService.listarEmpresasAtivas().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(empresas);
    }
    
    @PutMapping("/{id}")
    @ApiOperation("Atualizar empresa")
    public ResponseEntity<EmpresaDTO> atualizarEmpresa(@PathVariable Long id, @RequestBody EmpresaDTO empresaDTO) {
        try {
            Empresa empresa = empresaService.atualizarEmpresa(id, empresaDTO);
            return ResponseEntity.ok(converterParaDTO(empresa));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/plano/{planoId}")
    @ApiOperation("Atualizar plano da empresa")
    public ResponseEntity<Void> atualizarPlano(@PathVariable Long id, @PathVariable Long planoId) {
        try {
            empresaService.atualizarPlano(id, planoId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    private EmpresaDTO converterParaDTO(Empresa empresa) {
        return EmpresaDTO.builder()
                .id(empresa.getId())
                .email(empresa.getEmail())
                .nome(empresa.getNome())
                .cnpj(empresa.getCnpj())
                .razaoSocial(empresa.getRazaoSocial())
                .nomeFantasia(empresa.getNomeFantasia())
                .telefone(empresa.getTelefone())
                .endereco(empresa.getEndereco())
                .cidade(empresa.getCidade())
                .estado(empresa.getEstado())
                .cep(empresa.getCep())
                .descricao(empresa.getDescricao())
                .planoId(empresa.getPlano() != null ? empresa.getPlano().getId() : null)
                .tipoPlano(empresa.getPlano() != null ? empresa.getPlano().getTipoPlano().name() : null)
                .dataInicioPlano(empresa.getDataInicioPlano())
                .dataFimPlano(empresa.getDataFimPlano())
                .build();
    }
}
