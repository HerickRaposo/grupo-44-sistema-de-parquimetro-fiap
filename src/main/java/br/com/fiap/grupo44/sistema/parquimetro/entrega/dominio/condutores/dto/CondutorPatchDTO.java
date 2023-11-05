package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.condutores.dto;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.condutores.entities.Condutor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CondutorPatchDTO {


    private Long id;

    private String nome;

    private String sobrenome;

    private String dataNascimento;
    private String sexo;

    public CondutorPatchDTO(Condutor entidade){
        this.id = entidade.getId();
        this.nome = entidade.getNome();
        this.sobrenome = entidade.getSobrenome();
        this.dataNascimento = entidade.getDataNascimento();
        this.sexo = entidade.getSexo();
    }
}
