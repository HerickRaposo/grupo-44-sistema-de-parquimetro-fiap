package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.dto.FormaPagamentoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.dto.Paginator;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.dto.RestDataReturnDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.entities.FormaPagamento;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.dto.ParametrizacaoPagamentoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.entities.ParametrizacaoPagamento;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.repositories.IEParametrizacaoPagamentoRepository;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.exception.ControllerNotFoundException;

@Service
public class ParametrizacaoPagamentoService {
	private IEParametrizacaoPagamentoRepository iEParametrizacaoPagamentoRepository;
	
	public ParametrizacaoPagamentoDTO salvar(ParametrizacaoPagamentoDTO parametrizacaoPagamentoDTO) {
		ParametrizacaoPagamento parametrizacaoPagamento      = new ParametrizacaoPagamento(parametrizacaoPagamentoDTO);
		ParametrizacaoPagamento parametrizacaoPagamentoSalvo = this.iEParametrizacaoPagamentoRepository.save(parametrizacaoPagamento);
		return new ParametrizacaoPagamentoDTO(parametrizacaoPagamentoSalvo);
	}

	public ParametrizacaoPagamentoDTO findById(Long id) {
		ParametrizacaoPagamento parametrizacaoPagamento = this.iEParametrizacaoPagamentoRepository.findById(id).orElseThrow(() -> new ControllerNotFoundException("Endereço não encontrado"));
		return new ParametrizacaoPagamentoDTO(parametrizacaoPagamento);
	}

	
	public RestDataReturnDTO findAll(PageRequest pageRequest) {
		Page<ParametrizacaoPagamento> parametrizacaoPagamento = this.iEParametrizacaoPagamentoRepository.findAll(pageRequest);
		if (parametrizacaoPagamento.isEmpty()) {
			throw new ControllerNotFoundException("Nenhum Endereço para listar na pagina especificada.");
		}

		return new RestDataReturnDTO(parametrizacaoPagamento, new Paginator(parametrizacaoPagamento.getNumber(),parametrizacaoPagamento.getTotalElements(), parametrizacaoPagamento.getTotalPages()));
	}

	public ParametrizacaoPagamentoDTO atualizar(ParametrizacaoPagamentoDTO parametrizacaoPagamentoDTO, Long id) {
		Optional<ParametrizacaoPagamento> OParametrizacaoPagamento = this.iEParametrizacaoPagamentoRepository.findById(id);

		try {
			ParametrizacaoPagamento parametrizacaoPagamento = OParametrizacaoPagamento.get();
			
			parametrizacaoPagamento.setData(parametrizacaoPagamentoDTO.getData());
			parametrizacaoPagamento.setFormaPagamento(new FormaPagamento(parametrizacaoPagamentoDTO.getFormaPagamento()));
			parametrizacaoPagamento.setPeriodoEstacionamento(parametrizacaoPagamentoDTO.getPeriodoEstacionamento());
			parametrizacaoPagamento.setValorPorHora(parametrizacaoPagamentoDTO.getValorPorHora());
			
			
			this.iEParametrizacaoPagamentoRepository.save(parametrizacaoPagamento);
			
			new ParametrizacaoPagamentoDTO(parametrizacaoPagamento);
			
			return new ParametrizacaoPagamentoDTO(parametrizacaoPagamento);
		} catch (Exception e) {
			throw new ControllerNotFoundException("Forma de pagamento não encontrada, id: " + id);
		}
	}

	public String apagar(Long id) {
		Optional<ParametrizacaoPagamento> OParametrizacaoPagamento = this.iEParametrizacaoPagamentoRepository.findById(id);
		if (OParametrizacaoPagamento.isPresent()) {
			ParametrizacaoPagamento parametrizacaoPagamento = OParametrizacaoPagamento.get();
			this.iEParametrizacaoPagamentoRepository.delete(parametrizacaoPagamento);
			return "Removida Forma de pagamentpo de ID: " + id;
		}
		throw new ControllerNotFoundException("Forma de pagamento não encontrada, id: " + id);
	}

}
