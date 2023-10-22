package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.entities;


import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.dto.FormaPagamentoDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name="TB_FORMA_PAGAMENTO")
public class FormaPagamento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String descricao;
	private Boolean estado=true;
	
	public FormaPagamento(FormaPagamentoDTO formaPagamentoDTO) {
      this.setId(formaPagamentoDTO.getId());
	  this.setDescricao(formaPagamentoDTO.getDescricao());
      this.setEstado(formaPagamentoDTO.getEstado());
	}
}
