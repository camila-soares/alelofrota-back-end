package com.plataformaempregos.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.plataformaempregos.domain.Plano;
import com.plataformaempregos.enums.TipoPlano;

@Repository
public interface PlanoRepository extends JpaRepository<Plano, Long> {
    Optional<Plano> findByTipoPlano(TipoPlano tipoPlano);
}
