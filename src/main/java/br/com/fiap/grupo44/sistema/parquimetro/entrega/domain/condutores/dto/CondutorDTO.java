package br.com.fiap.grupo44.sistema.parquimetro.entrega.domain.condutores.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.domain.condutores.entities.Condutor;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.domain.endereco.dto.EnderecoDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CondutorDTO {


    private Long id;

    @NotNull(message = "Nome não pode deve ser nulo")
    @NotBlank(message = "Você deve preenche um nome, não pode ser vazio")
    private String nome;


    @NotNull(message = "Sobrenome não pode deve ser nulo")
    private String sobrenome;

    private String dataNascimento;
    @NotNull(message = "Sexo não deve ser nulo")
    @NotBlank(message = "Sexo deve ser apenas um caractere")
    @Max(value = 1, message = "Sexo só pode ter uma letra 'M' para masculino  ou 'F' para feminino")
    private String sexo;
    private Integer idade;
    private String email;
    private String phone;
    private String cell;
    private String fotosUrls;
    private String nat;
    @Null(message = "O valor do somatorio deve ser calculado automaticamente")
    private Double somatorioCustoMensal;
    private List<EnderecoDTO> enderecos = new ArrayList<>();

    public CondutorDTO(Condutor entidade) {
        this.id = entidade.getId();
        this.nome = entidade.getNome();
        this.sobrenome = entidade.getSobrenome();
        this.dataNascimento = entidade.getDataNascimento();
        this.sexo = entidade.getSexo();
        this.idade = entidade.getIdade();
        this.email = entidade.getEmail();
        this.phone = entidade.getPhone();
        this.cell = entidade.getCell();
        this.fotosUrls = entidade.getFotosUrls();
        this.nat = entidade.getNat();
    }

    public CondutorDTO(Condutor condutor, List<EnderecoDTO> enderecosDTO) {
        this(condutor);


        if (enderecosDTO!=null) {
            this.setEnderecos(enderecosDTO);
        }
    }
}
