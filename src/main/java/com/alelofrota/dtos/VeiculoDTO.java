package com.alelofrota.dtos;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;

import com.alelofrota.enuns.StatusVeiculo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VeiculoDTO {
	
	private Long id;
	@NotBlank(message = "nao pode ser vazio")
	private String placa;
	@NotBlank(message = "nao pode ser vazio")
	private String modelo;
	@NotBlank(message = "nao pode ser vazio")
	private String marca;
	private StatusVeiculo status;

}
