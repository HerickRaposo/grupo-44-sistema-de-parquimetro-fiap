package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormaPagamentoDTO {
	private Long id;
	private String descricao;
	private Boolean estado = true;
}
