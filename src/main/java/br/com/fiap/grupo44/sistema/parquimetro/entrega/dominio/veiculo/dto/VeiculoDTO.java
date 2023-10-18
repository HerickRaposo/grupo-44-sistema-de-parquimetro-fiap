package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.dto;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.dto.AlocacaoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.entities.Alocacao;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.entities.Veiculo;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VeiculoDTO {
    @JsonProperty
    private Long id;
    @JsonProperty
    @NotNull(message = "Marca não deve ser nulo")
    @NotBlank(message = "Marca não deve ser vazio")
    private String marca;
    @JsonProperty
    @NotNull(message = "Modelo não deve ser nulo")
    @NotBlank(message = "Modelo não deve ser vazio")
    private String modelo;
    @JsonProperty
    @NotNull(message = "Matricula não deve ser nulo")
    @NotBlank(message = "Matricula não deve ser vazio")
    private String matricula;
    @JsonProperty
    private Long cavalos;
    @JsonProperty
    @NotNull(message = "Defina ao menos 1 condutor para o veiculo")
    private Long idCondutor;

    private List<AlocacaoDTO> listaAlocacao;

    public VeiculoDTO(Veiculo entity){
        this.id = entity.getId();
        this.marca = entity.getMarca();
        this.modelo = entity.getModelo();
        this.matricula = entity.getMatricula();
        this.cavalos = entity.getCavalos();
        this.idCondutor = entity.getIdCondutor();
    }

    public VeiculoDTO(Veiculo entity, List<Alocacao> alocacoes){
        this(entity);
        if (alocacoes != null && alocacoes.size() > 0) {
            for(Alocacao alocacao: alocacoes){
                this.listaAlocacao.add(new AlocacaoDTO(alocacao));
            }
        }
    }
}
