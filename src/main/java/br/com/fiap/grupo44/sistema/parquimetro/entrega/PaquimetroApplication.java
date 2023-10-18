package br.com.mattec.grupo44.sistema.parquimetro.paquimetro;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Grupo FIAP 44", version = "1",description = "API desenvolvida para testes PROJETO FIAP GRUPO-44"))

public class PaquimetroApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaquimetroApplication.class, args);
	}

	
}
