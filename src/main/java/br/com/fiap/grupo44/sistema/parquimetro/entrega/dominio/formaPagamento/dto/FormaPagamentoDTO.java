package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.dto;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.entities.FormaPagamento;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormaPagamentoDTO {
	private Long id;
	@NotNull(message = "Descrição não deve ser nulo")
	private String descricao;
	@NotNull(message = "Estado não deve ser nulo")
	private Boolean estado;

	
	public FormaPagamentoDTO(FormaPagamento formaPagamento) {
		this.setId(formaPagamento.getId());
		this.setDescricao(formaPagamento.getDescricao());
		this.setEstado(formaPagamento.getEstado());
	}
}
