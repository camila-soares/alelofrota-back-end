package com.alelofrota.controllers;

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

import com.alelofrota.domain.Veiculo;
import com.alelofrota.dtos.VeiculoDTO;
import com.alelofrota.exceptions.RegraNegocioException;
import com.alelofrota.services.VeiculoService;
import lombok.RequiredArgsConstructor;

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
	
	private Veiculo converter(VeiculoDTO dto) {
		
			Veiculo veiculo = new Veiculo();
			veiculo.setId(dto.getId());
			veiculo.setPlaca(dto.getMarca());
			veiculo.setModelo(dto.getModelo());
			veiculo.setMarca(dto.getMarca());
			veiculo.setStatus(dto.getStatus());
			
			return veiculo;
		
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
		return service.obterPorId(id).map(entidade -> {
			service.deletar(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() ->
		new ResponseEntity("Lancamento nao encontrado na bade de dados.", HttpStatus.BAD_REQUEST));
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
	
	@PutMapping("{id}")
	public ResponseEntity autualizar(@PathVariable("id") Long id, @RequestBody VeiculoDTO dto) {
		return service.obterPorId(id).map(entity -> {
			try {
				Veiculo veiculo = converter(dto);
				veiculo.setId(entity.getId());
				service.atualizar(veiculo);
				return ResponseEntity.ok(veiculo);
			}catch (RegraNegocioException e) {
				return ResponseEntity.badRequest()
						.body(e.getMessage());
			}
			}).orElseGet(()-> 
		new ResponseEntity("veiculo nao encontrado na base de dados.", HttpStatus.BAD_REQUEST));
	}

}
