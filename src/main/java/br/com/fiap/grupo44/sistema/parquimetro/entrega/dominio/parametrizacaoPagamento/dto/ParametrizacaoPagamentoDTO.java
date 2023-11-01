package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.dto.FormaPagamentoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.entities.FormaPagamento;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.entities.ParametrizacaoPagamento;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.entities.PeriodoEstacionamento;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParametrizacaoPagamentoDTO {
	private Long id;
	private LocalDate data;
	private BigDecimal valorPorHora;
	private FormaPagamentoDTO formaPagamentoDTO;
	@Enumerated(EnumType.STRING)
	private PeriodoEstacionamento periodoEstacionamento;
	
	public ParametrizacaoPagamentoDTO(ParametrizacaoPagamento entity) {
	   this.data = entity.getData();
	   this.id = entity.getId();
	   this.periodoEstacionamento = entity.getPeriodoEstacionamento();
	   this.valorPorHora = entity.getValorPorHora();
	}
	public ParametrizacaoPagamentoDTO(ParametrizacaoPagamento entity, FormaPagamento formaPagamento){
		this(entity);
		if (formaPagamento == null) {
			this.formaPagamentoDTO = new FormaPagamentoDTO();
		} else {
			this.formaPagamentoDTO = new FormaPagamentoDTO(formaPagamento);
		}
	}
}
