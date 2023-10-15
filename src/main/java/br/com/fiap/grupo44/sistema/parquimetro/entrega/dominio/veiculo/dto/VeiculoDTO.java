package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.dto;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.condutor.entities.Condutor;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.entities.Veiculo;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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
//    @NotNull(message = "Defina ao menos 1 condutor para o veiculo")
    private Condutor condutor;

    public VeiculoDTO(Veiculo entity){
        this.id = entity.getId();
        this.marca = entity.getMarca();
        this.modelo = entity.getModelo();
        this.matricula = entity.getMatricula();
        this.cavalos = entity.getCavalos();
    }
}
