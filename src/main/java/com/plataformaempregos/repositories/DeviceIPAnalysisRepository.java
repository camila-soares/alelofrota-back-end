package com.plataformaempregos.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.plataformaempregos.domain.DeviceIPAnalysis;
import com.plataformaempregos.enums.IPAnalysisStatus;

/**
 * Repositório para entidade DeviceIPAnalysis
 */
@Repository
public interface DeviceIPAnalysisRepository extends JpaRepository<DeviceIPAnalysis, Long> {

    /**
     * Busca análise por ID da sessão Didit
     */
    Optional<DeviceIPAnalysis> findByDiditSessionId(String diditSessionId);

    /**
     * Lista análises de um candidato
     */
    List<DeviceIPAnalysis> findByCandidatoId(Long candidatoId);

    /**
     * Lista análises de uma empresa
     */
    List<DeviceIPAnalysis> findByEmpresaId(Long empresaId);

    /**
     * Lista análises por status
     */
    List<DeviceIPAnalysis> findByStatus(IPAnalysisStatus status);

    /**
     * Verifica se um candidato possui análise de um dispositivo específico
     */
    Optional<DeviceIPAnalysis> findByCandidatoIdAndDeviceFingerprintId(Long candidatoId, String deviceFingerprintId);

    /**
     * Lista análises pendentes de processamento de decisão
     */
    List<DeviceIPAnalysis> findByDecisionProcessedFalse();

    /**
     * Busca análises que ainda não foram processadas para um candidato
     */
    Optional<DeviceIPAnalysis> findFirstByCandidatoIdAndDecisionProcessedFalseOrderByDataCriacaoDesc(Long candidatoId);
}
