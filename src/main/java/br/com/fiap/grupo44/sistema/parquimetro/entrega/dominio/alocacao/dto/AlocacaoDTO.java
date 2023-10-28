package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.dto;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.entities.Alocacao;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.dto.EstacionamentoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.entities.Estacionamento;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.dto.ParametrizacaoPagamentoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.entities.ParametrizacaoPagamento;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.dto.VeiculoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.entities.Veiculo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlocacaoDTO {
    @JsonProperty
    private Long id;
    @JsonProperty
    private Date dataEntrada;
    @JsonProperty
    private Date dataSaida;
    @JsonProperty
    private Date dataInicioPago;
    @JsonProperty
    private Date dataFimPago;

    @JsonIgnore
    private Date ultimaVerificacao;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private VeiculoDTO veiculo;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private EstacionamentoDTO estacionamento;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ParametrizacaoPagamentoDTO parametrizacaoPagto;

    public AlocacaoDTO(Alocacao entity){
        this.id = entity.getId();
        this.dataEntrada = entity.getDataEntrada();
        this.dataSaida = entity.getDataSaida();
        this.dataInicioPago = entity.getDataInicioPago();
        this.dataFimPago = entity.getDataFimPago();
        this.ultimaVerificacao = entity.getUltimaVerificacao();
    }

    public AlocacaoDTO(Alocacao entity, Veiculo veiculo, Estacionamento estacionamento, ParametrizacaoPagamento parametrizacaoPagto) {
        this(entity);
        this.veiculo = new VeiculoDTO(veiculo);
        this.estacionamento = new EstacionamentoDTO(estacionamento);
        this.parametrizacaoPagto = new ParametrizacaoPagamentoDTO(parametrizacaoPagto);
    }
}
