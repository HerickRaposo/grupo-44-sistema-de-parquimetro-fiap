package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.dto;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.dto.AlocacaoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.entities.Alocacao;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.dto.EnderecoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.entities.Endereco;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.entities.Estacionamento;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstacionamentoDTO {
    @JsonProperty
    private Long id;
    @JsonProperty
    @NotNull(message = "Descrição não deve ser nulo")
    @NotBlank(message = "Descrição não deve ser vazio")
    private String descricao;
    @JsonProperty
    @NotNull(message = "estado não deve ser nulo")
    private Boolean estado;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AlocacaoDTO> listaAlocacao;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private EnderecoDTO endereco;

    public EstacionamentoDTO(Estacionamento entity){
        this.id = entity.getId();
        this.descricao = entity.getDescricao();
        this.estado = entity.getEstado();
    }

    public EstacionamentoDTO(Estacionamento entity, Endereco endereco, List<Alocacao> alocacoes) {
        this(entity);
        if (endereco != null){
            this.endereco = new EnderecoDTO(endereco);
        }

        if (alocacoes != null && alocacoes.size() > 0) {
            if (this.listaAlocacao == null){
                this.listaAlocacao = new ArrayList<>();
            }

            for(Alocacao alocacao: alocacoes){
                this.listaAlocacao.add(new AlocacaoDTO(alocacao));
            }
        }
    }
}
