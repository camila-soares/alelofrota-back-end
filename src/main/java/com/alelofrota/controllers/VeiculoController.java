package com.alelofrota.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;		

import com.alelofrota.domain.Veiculo;
import com.alelofrota.dtos.VeiculoDTO;
import com.alelofrota.exceptions.RegraNegocioException;
import com.alelofrota.services.VeiculoService;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins= "http://localhost:4200")
@RestController
@RequestMapping("/veiculo")
@RequiredArgsConstructor
public class VeiculoController {
	
	@Autowired
	private final VeiculoService service;
	
	@PostMapping
	public ResponseEntity<String> salvar(@RequestBody VeiculoDTO dto) {
		try {
		Veiculo entidade = converter(dto);
		entidade = service.salva(entidade);
		return new ResponseEntity(entidade, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest()
					.body(e.getMessage());
		}
	}
	
	public Veiculo converter(VeiculoDTO objDTO) {
		return new Veiculo(objDTO.getId(), objDTO.getMarca(), objDTO.getModelo(), 
				objDTO.getPlaca(), objDTO.getStatus());
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
		return service.obterPorId(id).map(entidade -> {
			service.deletar(entidade);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}).orElseGet(() ->
		new ResponseEntity("veiculo nao encontrado na bade de dados.", HttpStatus.BAD_REQUEST));
	}
	
	
	@GetMapping
	public ResponseEntity<List<Veiculo>> buscar(
			
	@RequestParam(value = "marca", required = false)String marca,
	@RequestParam(value = "placa", required = false)String placa, 
	@RequestParam(value = "modelo", required = false) String modelo) {
		
		Veiculo veiculoFiltro = new Veiculo();
		veiculoFiltro.setMarca(marca);
		veiculoFiltro.setPlaca(placa);
		veiculoFiltro.setModelo(modelo);
		
		
		List<Veiculo> veiculos = service.buscar(veiculoFiltro);
		return ResponseEntity.ok(veiculos);
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Optional<Veiculo>> find(@PathVariable Long id) {
		Optional<Veiculo> veiculo = service.obterPorId(id);
		return ResponseEntity.ok().body(veiculo);
	}
	
	@PutMapping("{id}")
	public ResponseEntity<Veiculo> autualizar(@PathVariable("id") Long id, @RequestBody Veiculo veiculo) {
		return service.obterPorId(id).map(entity -> {
			entity.setMarca(veiculo.getMarca());
			entity.setModelo(veiculo.getModelo());
			entity.setStatus(veiculo.getStatus());
			service.atualizar(entity);
			return ResponseEntity.ok(entity);
			}).orElseGet(()-> 
		new ResponseEntity("veiculo nao encontrado na base de dados.", HttpStatus.BAD_REQUEST));
	}

}
