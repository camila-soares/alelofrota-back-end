package com.alelofrota.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
	public ResponseEntity<String> salvar(@RequestBody @Valid VeiculoDTO dto) {
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
		return new Veiculo(objDTO.getId(),objDTO.getPlate(), objDTO.getModel(), objDTO.getManufacturer(), objDTO.getColor(), 
				  objDTO.getStatus());
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
		return service.obterPorId(id).map(entidade -> {
			service.deletar(entidade);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}).orElseGet(() ->
		new ResponseEntity("veiculo nao encontrado na bade de dados.", HttpStatus.BAD_REQUEST));
	}
	
	
	
    @GetMapping("/search")
    public Page<Veiculo> search(
            @RequestParam(value = "plate", required = false) String plate,
            @RequestParam(value = "page",required = false,defaultValue = "0") int page,
            @RequestParam(value = "size",required = false,defaultValue = "10") int size){
    		
    	
        return service.search(plate, page, size);

    }

    @GetMapping
    public Page<Veiculo> getAll() {
        return service.findAll();
    }
	

	
	@GetMapping("/{id}")
	public ResponseEntity<Optional<Veiculo>> find(@PathVariable Long id) {
		Optional<Veiculo> veiculo = service.obterPorId(id);
		return ResponseEntity.ok().body(veiculo);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Veiculo> autualizar(@PathVariable("id") Long id, @RequestBody Veiculo veiculo) {
		return service.obterPorId(id).map(entity -> {
			
			entity.setModel(veiculo.getModel());
			entity.setManufacturer(veiculo.getManufacturer());
			entity.setColor(veiculo.getColor());
			entity.setStatus(veiculo.getStatus());
			service.atualizar(entity);
			return ResponseEntity.ok(entity);
			}).orElseGet(()-> 
		new ResponseEntity("veiculo nao encontrado na base de dados.", HttpStatus.BAD_REQUEST));
	}

}
