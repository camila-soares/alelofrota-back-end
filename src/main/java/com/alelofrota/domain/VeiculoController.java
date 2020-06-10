package com.alelofrota.domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alelofrota.exceptions.RegraNegocioException;
import com.alelofrota.services.VeiculoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/veiculo")
@RequiredArgsConstructor
public class VeiculoController {
	
	@Autowired
	private final VeiculoService service;
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody Veiculo veiculo) {
		veiculo = service.salva(veiculo);
		return new ResponseEntity(veiculo, HttpStatus.CREATED);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		return service.obterPorId(id).map(entidade -> {
			service.deletar(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() ->
		new ResponseEntity("Lancamento nao encontrado na bade de dados.", HttpStatus.BAD_REQUEST));
	}
	
	
	@GetMapping
	public ResponseEntity buscar(
			
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
	
	@PutMapping("{id}")
	public ResponseEntity autualizar(@PathVariable("id") Long id, @RequestBody Veiculo veiculo) {
		return service.obterPorId(id).map(entity -> {
			veiculo.setId(entity.getId());
			service.atualizar(veiculo);
			return ResponseEntity.ok(veiculo);
			}).orElseGet(()-> 
		new ResponseEntity("lancamento nao encontrado na base de dados.", HttpStatus.BAD_REQUEST));
	}

}
