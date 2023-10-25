package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.dto;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.entities.FormaPagamento;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormaPagamentoDTO {
	private Long id;
	private String descricao;
	private Boolean estado = true;

	
	public FormaPagamentoDTO(FormaPagamento formaPagamento) {
		this.setId(formaPagamento.getId());
		this.setDescricao(formaPagamento.getDescricao());
		this.setEstado(formaPagamento.getEstado());
	}
}
