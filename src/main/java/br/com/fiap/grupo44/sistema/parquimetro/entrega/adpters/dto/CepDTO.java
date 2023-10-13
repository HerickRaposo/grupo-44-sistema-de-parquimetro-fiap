package br.com.fiap.grupo44.sistema.parquimetro.entrega.adpters.dto;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.domain.condutores.entities.Condutor;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;

import lombok.Setter;

@Getter
@Setter
public class CepDTO {
	@NotBlank(message = "O campo CEP é obrigatório seu preenchimento.")
	private String cep;
	private Condutor condutor;
}
