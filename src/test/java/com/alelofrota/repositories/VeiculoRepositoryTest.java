package com.alelofrota.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.alelofrota.domain.Veiculo;
import com.alelofrota.enuns.StatusVeiculo;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class VeiculoRepositoryTest {
	
	@Autowired
	VeiculoRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveSalvarUmLancamento() {
		Veiculo veiculo = criarEPersistirUmVeiculo();
		
		veiculo = repository.save(veiculo);
		
		assertThat(veiculo.getId()).isNotNull();
	}
	
	@Test
	public void deveDeletarUmVeiculo() {
		Veiculo veiculo = criarEPersistirUmVeiculo();
		
		veiculo = entityManager.find(Veiculo.class, veiculo.getId());
		 
		 repository.delete(veiculo);
		 
		 Veiculo veiculoInxistente = entityManager.find(Veiculo.class, veiculo.getId());
	
		 assertThat(veiculoInxistente).isNull();
	}
	
	@Test
	public void deveBuscarVeiculoPorId() {
		Veiculo veiculo = criarEPersistirUmVeiculo();
		Optional<Veiculo> veiculoEncontrado = repository.findById(veiculo.getId());
		
		assertThat(veiculoEncontrado.isPresent()).isTrue();
	}
	
	@Test
	public void deveAtualizarUmVeiculo() {
		Veiculo veiculo = criarEPersistirUmVeiculo();
		veiculo.setManufacturer("honda");
		veiculo.setModel("teste atualizar");
		veiculo.setColor("black");
		veiculo.setStatus(StatusVeiculo.INATIVO);
		
		repository.save(veiculo);
		
		Veiculo veiculoAtualizado = entityManager.find(Veiculo.class, veiculo.getId());
		
		assertThat(veiculoAtualizado.getManufacturer()).isEqualTo("honda");
		assertThat(veiculoAtualizado.getModel()).isEqualTo("teste atualizar");
		assertThat(veiculoAtualizado.getStatus()).isEqualTo(StatusVeiculo.INATIVO);
		assertThat(veiculoAtualizado.getColor()).isEqualTo("black");
		
	}
	
	
	public static Veiculo criarVeiculo() {
		return Veiculo.builder().manufacturer("fiat")
								.model("palio")
								.plate("hhh-9090")
								.color("black")
								.build();
	}
	
	
	
	
	private Veiculo criarEPersistirUmVeiculo() {
		Veiculo veiculo = criarVeiculo();
		entityManager.persist(veiculo);
		return veiculo;
	}

}
