package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.entities.ParametrizacaoPagamento;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.entities.PeriodoEstacionamento;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParametrizacaoPagamentoDTO {
	private Long id;
	private LocalDate data;
	private BigDecimal valorPorHora;
	private Long formaPagamento;
	@Enumerated(EnumType.STRING)
	private PeriodoEstacionamento periodoEstacionamento;

	public ParametrizacaoPagamentoDTO() {
		
	}
	
	public ParametrizacaoPagamentoDTO(ParametrizacaoPagamento parametrizacaoPagamentoSalvo) {
	   this.data=parametrizacaoPagamentoSalvo.getData();
	   this.formaPagamento=parametrizacaoPagamentoSalvo.getFormaPagamento().getId();
	   this.id=parametrizacaoPagamentoSalvo.getId();
	   this.periodoEstacionamento=parametrizacaoPagamentoSalvo.getPeriodoEstacionamento();
	   this.valorPorHora=parametrizacaoPagamentoSalvo.getValorPorHora();
	}
}
