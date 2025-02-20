package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.entities;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.condutores.entities.Condutor;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.entities.Estacionamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_endereco")
public class Endereco {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String cep;
	private String rua;
	private Integer numero; 
	private String bairro;
	private String cidade;
	private String estado;
	@ManyToMany
	@JoinTable(name = "tb_endereco_condutor",
			joinColumns = @JoinColumn(name = "endereco_id"),
			inverseJoinColumns = @JoinColumn(name = "condutor_id"))
	private List<Condutor> condutores= new ArrayList<>();

	@OneToMany(mappedBy = "endereco")
	private List<Estacionamento> estacionamentos = new ArrayList<>();

}