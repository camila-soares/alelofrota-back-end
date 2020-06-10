package com.alelofrota.dtos;

import com.alelofrota.enuns.StatusVeiculo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VeiculoDTO {
	
	private Long id;
	private String placa;
	private String modelo;
	private String marca;
	private StatusVeiculo status;

}
