package com.plataformaempregos.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plataformaempregos.domain.ProcessamentoIA;
import com.plataformaempregos.dtos.ProcessamentoIADTO;
import com.plataformaempregos.enums.StatusProcessamentoIA;
import com.plataformaempregos.enums.TipoProcessamentoIA;
import com.plataformaempregos.services.ProcessamentoIAService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/processamentos-ia")
@Api(tags = "Processamento Assíncrono de IA")
public class ProcessamentoIAController {
    
    @Autowired
    private ProcessamentoIAService processamentoService;
    
    @GetMapping("/{id}")
    @ApiOperation("Obter status de processamento por ID")
    public ResponseEntity<ProcessamentoIADTO> obterProcessamento(@PathVariable Long id) {
        return processamentoService.obterPorId(id)
                .map(p -> ResponseEntity.ok(processamentoService.converterParaDTO(p)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/entidade/{entidadeId}/{entidadeTipo}")
    @ApiOperation("Obter processamento por entidade")
    public ResponseEntity<ProcessamentoIADTO> obterPorEntidade(
            @PathVariable Long entidadeId,
            @PathVariable String entidadeTipo) {
        return processamentoService.obterPorEntidade(entidadeId, entidadeTipo)
                .map(p -> ResponseEntity.ok(processamentoService.converterParaDTO(p)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/usuario/{usuarioId}")
    @ApiOperation("Listar processamentos de um usuário")
    public ResponseEntity<List<ProcessamentoIADTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<ProcessamentoIADTO> processamentos = processamentoService.listarPorUsuario(usuarioId).stream()
                .map(processamentoService::converterParaDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(processamentos);
    }
    
    @GetMapping("/status/{status}")
    @ApiOperation("Listar processamentos por status")
    public ResponseEntity<List<ProcessamentoIADTO>> listarPorStatus(@PathVariable StatusProcessamentoIA status) {
        List<ProcessamentoIADTO> processamentos = processamentoService.listarPorStatus(status).stream()
                .map(processamentoService::converterParaDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(processamentos);
    }
    
    @GetMapping("/tipo/{tipo}")
    @ApiOperation("Listar processamentos por tipo")
    public ResponseEntity<List<ProcessamentoIADTO>> listarPorTipo(@PathVariable TipoProcessamentoIA tipo) {
        List<ProcessamentoIADTO> processamentos = processamentoService.listarPorTipo(tipo).stream()
                .map(processamentoService::converterParaDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(processamentos);
    }
}
