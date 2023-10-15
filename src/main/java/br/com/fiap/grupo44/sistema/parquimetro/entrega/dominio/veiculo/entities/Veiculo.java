package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.entities;


import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.condutor.entities.Condutor;
import jakarta.persistence.*;
import lombok.*;

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
    private Condutor condutor;
}
