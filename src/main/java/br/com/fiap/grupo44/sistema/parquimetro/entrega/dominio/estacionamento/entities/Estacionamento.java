package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.entities;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.entities.Alocacao;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.entities.Endereco;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tb_estacionamento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class Estacionamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    private Boolean estado = true;      //disponivel

    @ManyToOne
    @JoinColumn(name = "endereco_id") // Nome da coluna que faz referência ao Endereco
    private Endereco endereco;

    @OneToMany(mappedBy = "estacionamento")
    private List<Alocacao> listaAlocacao;
}
