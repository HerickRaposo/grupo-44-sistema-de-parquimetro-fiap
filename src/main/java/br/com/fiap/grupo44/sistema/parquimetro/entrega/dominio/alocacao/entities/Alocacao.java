package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.entities;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.entities.Estacionamento;
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
    private Date dataentrada;
    private Date dataSaida;
    private Date dataInicioPago;
    private Date dataFimPago;
    private Veiculo veiculo;
    private Estacionamento estacionamento;


}
