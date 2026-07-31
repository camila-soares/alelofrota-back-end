package com.plataformaempregos.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plataformaempregos.domain.Plano;
import com.plataformaempregos.dtos.PlanoDTO;
import com.plataformaempregos.services.PlanoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/planos")
@Api(tags = "Planos")
public class PlanoController {
    
    @Autowired
    private PlanoService planoService;
    
    @PostMapping
    @ApiOperation("Criar novo plano")
    public ResponseEntity<PlanoDTO> criarPlano(@RequestBody PlanoDTO planoDTO) {
        Plano plano = converterParaEntidade(planoDTO);
        plano = planoService.criarPlano(plano);
        return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(plano));
    }
    
    @GetMapping("/{id}")
    @ApiOperation("Obter plano por ID")
    public ResponseEntity<PlanoDTO> obterPlano(@PathVariable Long id) {
        return planoService.obterPorId(id)
                .map(plano -> ResponseEntity.ok(converterParaDTO(plano)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @ApiOperation("Listar todos os planos")
    public ResponseEntity<List<PlanoDTO>> listarPlanos() {
        List<PlanoDTO> planos = planoService.listarTodos().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(planos);
    }
    
    private PlanoDTO converterParaDTO(Plano plano) {
        return PlanoDTO.builder()
                .id(plano.getId())
                .nome(plano.getNome())
                .tipoPlano(plano.getTipoPlano())
                .valorMensal(plano.getValorMensal())
                .limiteVagas(plano.getLimiteVagas())
                .limiteCandidatosVisualizacao(plano.getLimiteCandidatosVisualizacao())
                .suportePrioritario(plano.getSuportePrioritario())
                .analiseIAAvancada(plano.getAnaliseIAAvancada())
                .relatoriosAvancados(plano.getRelatoriosAvancados())
                .descricao(plano.getDescricao())
                .build();
    }
    
    private Plano converterParaEntidade(PlanoDTO planoDTO) {
        return Plano.builder()
                .nome(planoDTO.getNome())
                .tipoPlano(planoDTO.getTipoPlano())
                .valorMensal(planoDTO.getValorMensal())
                .limiteVagas(planoDTO.getLimiteVagas())
                .limiteCandidatosVisualizacao(planoDTO.getLimiteCandidatosVisualizacao())
                .suportePrioritario(planoDTO.getSuportePrioritario())
                .analiseIAAvancada(planoDTO.getAnaliseIAAvancada())
                .relatoriosAvancados(planoDTO.getRelatoriosAvancados())
                .descricao(planoDTO.getDescricao())
                .build();
    }
}
