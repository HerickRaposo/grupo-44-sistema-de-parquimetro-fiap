package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.entities;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.entities.Estacionamento;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.entities.ParametrizacaoPagamento;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.entities.Veiculo;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "tb_alocacao")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class Alocacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dataEntrada;
    private Date dataSaida;
    private Date dataInicioPago;
    private Date dataFimPago;
    private Date ultimaVerificacao;


    @ManyToOne
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;

    @ManyToOne
    @JoinColumn(name = "estacionamento_id")
    private Estacionamento estacionamento;

    @ManyToOne
    @JoinColumn(name = "PARAMETRIZACAO_PAGAMENTO_ID")
    private ParametrizacaoPagamento parametrizacaoPagto;
}