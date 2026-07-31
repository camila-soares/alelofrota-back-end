package com.plataformaempregos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class PlataformaEmpregosApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlataformaEmpregosApplication.class, args);
	}

}
