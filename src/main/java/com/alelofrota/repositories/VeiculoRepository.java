package com.alelofrota.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alelofrota.domain.Veiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long>{
	
	@Query("FROM Veiculo v WHERE v.plate like %:plate% " )
	Page<Veiculo> seach(
			@Param("plate") String plate,
			Pageable pegeable);

	//Page<Veiculo> search(String lowerCase, PageRequest pageRequest);

}
