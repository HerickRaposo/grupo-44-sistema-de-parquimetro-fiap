package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.entities.FormaPagamento;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.dto.ParametrizacaoPagamentoDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Entity
@Table(name="TB_PARAMETRIZACAO_PAGAMENTO")
public class ParametrizacaoPagamento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDate data;
	private BigDecimal valorPorHora;
	@ManyToOne
	@JoinColumn(name = "FORMA_PAGAMENTO_ID")
	private FormaPagamento formaPagamento;
	@Enumerated(EnumType.STRING)
	private PeriodoEstacionamento periodoEstacionamento;
	
	//@OneToMany(mappedBy = "parametrizacao")
	//private List<EstacionamentoVeiculo> estacionamentoVeiculo = new ArrayList<>();
	
	public ParametrizacaoPagamento() {
		
	}
	
	public ParametrizacaoPagamento(ParametrizacaoPagamentoDTO parametrizacaoPagamentoDTO) {
		this.id=parametrizacaoPagamentoDTO.getId();
		this.valorPorHora=parametrizacaoPagamentoDTO.getValorPorHora();
		this.formaPagamento=new FormaPagamento(parametrizacaoPagamentoDTO.getFormaPagamento());
		this.periodoEstacionamento=parametrizacaoPagamentoDTO.getPeriodoEstacionamento();
	}
}
