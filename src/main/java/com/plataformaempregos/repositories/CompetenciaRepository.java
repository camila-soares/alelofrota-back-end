package com.plataformaempregos.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.plataformaempregos.domain.Competencia;

@Repository
public interface CompetenciaRepository extends JpaRepository<Competencia, Long> {
    Optional<Competencia> findByNome(String nome);
    List<Competencia> findByCategoria(String categoria);
    boolean existsByNome(String nome);
}
