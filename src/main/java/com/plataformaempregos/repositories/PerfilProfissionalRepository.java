package com.plataformaempregos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.plataformaempregos.domain.PerfilProfissional;

@Repository
public interface PerfilProfissionalRepository extends JpaRepository<PerfilProfissional, Long> {
}
