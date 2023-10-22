package br.com.fiap.grupo44.sistema.parquimetro.entrega;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
@SpringBootApplication
public class PaquimetroApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaquimetroApplication.class, args);
	}

}
