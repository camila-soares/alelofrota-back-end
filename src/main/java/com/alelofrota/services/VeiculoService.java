package com.alelofrota.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.alelofrota.domain.Veiculo;
import com.alelofrota.repositories.VeiculoRepository;

@Service
public class VeiculoService {

	@Autowired
	private VeiculoRepository repository;


	@Transactional
	public Veiculo salva(Veiculo veiculo) {
		return repository.save(veiculo);

	}

	public Optional<Veiculo> obterPorId(Long id) {
		return repository.findById(id);
	}

	public Page<Veiculo> buscarTodos() {
		int page = 0;
		int size = 10;
		PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "name");
		return new PageImpl<>(
				repository.findAll(),
				pageRequest, size
				);
	}

	public Page<Veiculo> search(String searchTerm,int page,int size) {
	
		return repository.seach(searchTerm.toLowerCase(), null);
	}



	@Transactional
	public Veiculo atualizar(Veiculo veiculo) {
		Objects.requireNonNull(veiculo.getId());
		return repository.save(veiculo);
	}

	@Transactional
	public void deletar(Veiculo veiculo) {
		Objects.requireNonNull(veiculo.getId());
		repository.delete(veiculo);
	}

}
