package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.condutores.entities;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.entities.Endereco;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.entities.Veiculo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

import java.util.ArrayList;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_condutor")
public class Condutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String sobrenome;
    private String sexo;
    private String dataNascimento;
    private Integer idade;
    private String email;
    private String phone;
    private String cell;
    private String fotosUrls;
    private String nat;

    @CreationTimestamp
    private Instant dataDeCriacao;

    @ManyToMany(mappedBy = "condutores")
    private List<Endereco> enderecos = new ArrayList<Endereco>();

    @OneToMany(mappedBy = "condutor")
    private List<Veiculo> veiculos = new ArrayList<Veiculo>();

}
