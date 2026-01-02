package com.plataformaempregos.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.plataformaempregos.domain.Empresa;
import com.plataformaempregos.domain.Vaga;
import com.plataformaempregos.enums.StatusVaga;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, Long> {
    List<Vaga> findByEmpresa(Empresa empresa);
    Page<Vaga> findByStatus(StatusVaga status, Pageable pageable);
    Page<Vaga> findByEmpresaAndStatus(Empresa empresa, StatusVaga status, Pageable pageable);
    
    @Query("SELECT v FROM Vaga v WHERE v.status = :status AND " +
           "(LOWER(v.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(v.descricao) LIKE LOWER(CONCAT('%', :termo, '%')))")
    Page<Vaga> buscarPorTermo(@Param("termo") String termo, @Param("status") StatusVaga status, Pageable pageable);
}
