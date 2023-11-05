package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.entities.FormaPagamento;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

}
