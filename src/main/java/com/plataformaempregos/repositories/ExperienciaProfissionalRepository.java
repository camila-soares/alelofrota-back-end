package com.plataformaempregos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.plataformaempregos.domain.ExperienciaProfissional;
import com.plataformaempregos.domain.PerfilProfissional;

@Repository
public interface ExperienciaProfissionalRepository extends JpaRepository<ExperienciaProfissional, Long> {
    List<ExperienciaProfissional> findByPerfilProfissional(PerfilProfissional perfilProfissional);
}
