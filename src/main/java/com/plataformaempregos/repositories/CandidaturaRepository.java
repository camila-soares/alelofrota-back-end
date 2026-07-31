package com.plataformaempregos.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.plataformaempregos.domain.Candidato;
import com.plataformaempregos.domain.Candidatura;
import com.plataformaempregos.domain.Vaga;
import com.plataformaempregos.enums.StatusCandidatura;

@Repository
public interface CandidaturaRepository extends JpaRepository<Candidatura, Long> {
    List<Candidatura> findByCandidato(Candidato candidato);
    List<Candidatura> findByVaga(Vaga vaga);
    Optional<Candidatura> findByCandidatoAndVaga(Candidato candidato, Vaga vaga);
    List<Candidatura> findByVagaAndStatus(Vaga vaga, StatusCandidatura status);
    List<Candidatura> findByCandidatoAndStatus(Candidato candidato, StatusCandidatura status);
    boolean existsByCandidatoAndVaga(Candidato candidato, Vaga vaga);
}
