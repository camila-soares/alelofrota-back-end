package com.alelofrota.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alelofrota.domain.Veiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long>{
	
	 @Query("FROM Veiculo v " +
	           "WHERE LOWER(v.marca) like %:searchTerm% " +
	           "OR LOWER(v.placa) like %:searchTerm%" )
	Page<Veiculo> seach(
			@Param("searchTerm") String searchTerm,
			Pageable pegeable);

	//Page<Veiculo> search(String lowerCase, PageRequest pageRequest);

}
