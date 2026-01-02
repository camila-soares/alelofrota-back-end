package com.plataformaempregos.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plataformaempregos.domain.ProcessamentoIA;
import com.plataformaempregos.dtos.ProcessamentoIADTO;
import com.plataformaempregos.enums.StatusProcessamentoIA;
import com.plataformaempregos.enums.TipoProcessamentoIA;
import com.plataformaempregos.repositories.ProcessamentoIARepository;

@Service
public class ProcessamentoIAService {
    
    @Autowired
    private ProcessamentoIARepository processamentoRepository;
    
    public Optional<ProcessamentoIA> obterPorId(Long id) {
        return processamentoRepository.findById(id);
    }
    
    public List<ProcessamentoIA> listarPorUsuario(Long usuarioId) {
        return processamentoRepository.findByUsuarioId(usuarioId);
    }
    
    public List<ProcessamentoIA> listarPorStatus(StatusProcessamentoIA status) {
        return processamentoRepository.findByStatus(status);
    }
    
    public List<ProcessamentoIA> listarPorTipo(TipoProcessamentoIA tipo) {
        return processamentoRepository.findByTipoProcessamento(tipo);
    }
    
    public Optional<ProcessamentoIA> obterPorEntidade(Long entidadeId, String entidadeTipo) {
        return processamentoRepository.findByEntidadeIdAndEntidadeTipo(entidadeId, entidadeTipo);
    }
    
    public ProcessamentoIADTO converterParaDTO(ProcessamentoIA processamento) {
        return ProcessamentoIADTO.builder()
                .id(processamento.getId())
                .tipoProcessamento(processamento.getTipoProcessamento())
                .status(processamento.getStatus())
                .entidadeId(processamento.getEntidadeId())
                .entidadeTipo(processamento.getEntidadeTipo())
                .resultado(processamento.getResultado())
                .scoreCalculado(processamento.getScoreCalculado())
                .observacoes(processamento.getObservacoes())
                .erro(processamento.getErro())
                .progresso(processamento.getProgresso())
                .dataInicio(processamento.getDataInicio())
                .dataConclusao(processamento.getDataConclusao())
                .tempoProcessamentoMs(processamento.getTempoProcessamentoMs())
                .usuarioId(processamento.getUsuarioId())
                .build();
    }
}
