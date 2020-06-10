package com.alelofrota.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.alelofrota.domain.Veiculo;
import com.alelofrota.repositories.VeiculoRepository;

@RunWith(SpringRunner.class)
public class VeiculoServiceTest {
	
	@SpyBean
	VeiculoService service;
	
	@MockBean
	VeiculoRepository repoRepository;
	
	@Test
	public void deveSalvarUmVeiculo() {
		
		Veiculo veiculoSalvar = criarVeiculo();
		
		Veiculo veiculoSalvo = criarVeiculo();
		when(repoRepository.save(veiculoSalvar)).thenReturn(veiculoSalvo);
		Veiculo veiculo = service.salva(veiculoSalvar);
		Assertions.assertThat(veiculo.getId()).isEqualTo(veiculoSalvo.getId());
		
	}
	
	@Test
	public void deveAtualizarUmVeiculo() {
		
		Veiculo veiculoSalvo = criarVeiculo();
		veiculoSalvo.setId(1l);
		
		when(repoRepository.save(veiculoSalvo)).thenReturn(veiculoSalvo);
		
		Veiculo veiculo = service.atualizar(veiculoSalvo);
		verify(repoRepository, Mockito.times(1)).save(veiculoSalvo);
	}
	
	@Test
	public void deveDeletarUmVeiculo() {
		Veiculo veiculoSalvar = criarVeiculo();
		veiculoSalvar.setId(1l);
		service.deletar(veiculoSalvar);
		
		verify(repoRepository).delete(veiculoSalvar);
	}
	
	@Test
	public void deveObterUmVeiculoPorID() {
		Long id = 1l;
		
		Veiculo veiculo = criarVeiculo();
		veiculo.setId(id);
		
		when( repoRepository.findById(id)).thenReturn(Optional.of(veiculo));
		Optional<Veiculo> resultado = service.obterPorId(id);
		Assertions.assertThat(resultado.isPresent()).isTrue();
	}
	
	@Test
	public void deveRetornarVazioQuandoOVeiculoNaoExistir() {
		Long id = 1l;
		
		Veiculo veiculo = criarVeiculo();
		veiculo.setId(id);
		
		when( repoRepository.findById(id)).thenReturn(Optional.empty());
		
		Optional<Veiculo> resultado = service.obterPorId(id);
		
		Assertions.assertThat(resultado.isPresent()).isFalse();
	}
	
	
	
	
	public static Veiculo criarVeiculo() {
		return Veiculo.builder().marca("fiat")
								.modelo("palio")
								.placa("hhh-9090")
								.build();
	}

}
