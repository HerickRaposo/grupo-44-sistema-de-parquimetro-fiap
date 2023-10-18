package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.entities;


import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.entities.Alocacao;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Table(name = "tb_veiculo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")

public class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String marca;
    private String modelo;
    private String matricula;
    private Long cavalos;
    private Long idCondutor;

    @OneToMany(mappedBy = "veiculo")
    private List<Alocacao> alocacoes;
}