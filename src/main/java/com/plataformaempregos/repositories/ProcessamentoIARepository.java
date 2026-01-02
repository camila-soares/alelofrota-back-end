package com.plataformaempregos.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.plataformaempregos.domain.ProcessamentoIA;
import com.plataformaempregos.enums.StatusProcessamentoIA;
import com.plataformaempregos.enums.TipoProcessamentoIA;

@Repository
public interface ProcessamentoIARepository extends JpaRepository<ProcessamentoIA, Long> {
    Optional<ProcessamentoIA> findByEntidadeIdAndEntidadeTipo(Long entidadeId, String entidadeTipo);
    List<ProcessamentoIA> findByStatus(StatusProcessamentoIA status);
    List<ProcessamentoIA> findByTipoProcessamento(TipoProcessamentoIA tipoProcessamento);
    List<ProcessamentoIA> findByUsuarioId(Long usuarioId);
    List<ProcessamentoIA> findByUsuarioIdAndStatus(Long usuarioId, StatusProcessamentoIA status);
}
