package com.alelofrota.controllers;

import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.alelofrota.domain.Veiculo;
import com.alelofrota.domain.VeiculoController;
import com.alelofrota.enuns.StatusVeiculo;
import com.alelofrota.repositories.VeiculoRepository;
import com.alelofrota.services.VeiculoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = VeiculoController.class)
@AutoConfigureMockMvc
public class VeiculoControllerTest {
	
	
	static final String API = "/api/veiculo";
	static final MediaType JSON = MediaType.APPLICATION_JSON;
	
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	VeiculoService service;
	
	@Test
	public void deveCriarUmVeiculo() throws Exception {
		
		String modelo = "suv";
		String marca = "chrvrolet";
		String placa = "phd-7777";
		
		Veiculo veiculo = Veiculo.builder().marca(marca)
				.modelo(modelo).placa(placa).build();
		
		when(service.salva(Mockito.any(Veiculo.class)) ).thenReturn(veiculo);
		
		String json = new ObjectMapper().writeValueAsString(veiculo);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(API )
				.accept(JSON)
				.contentType(JSON)
				.content(json);

mvc.perform(request)
.andExpect(MockMvcResultMatchers.status().isCreated() )
.andExpect(MockMvcResultMatchers.jsonPath("id").value(veiculo.getId()) )
.andExpect(MockMvcResultMatchers.jsonPath("marca").value(veiculo.getMarca()) )
.andExpect(MockMvcResultMatchers.jsonPath("placa").value(veiculo.getPlaca()) );

		
	}

}
