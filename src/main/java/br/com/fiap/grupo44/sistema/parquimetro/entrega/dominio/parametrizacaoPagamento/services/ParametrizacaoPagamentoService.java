package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.dto.FormaPagamentoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.entities.FormaPagamento;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.services.FormaPagamentoServices;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.entities.PeriodoEstacionamento;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.entities.Veiculo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.dto.Paginator;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.dto.RestDataReturnDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.dto.ParametrizacaoPagamentoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.entities.ParametrizacaoPagamento;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.repositories.IEParametrizacaoPagamentoRepository;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.exception.ControllerNotFoundException;
import org.springframework.util.ReflectionUtils;

@Service
public class ParametrizacaoPagamentoService {
	@Autowired
	private IEParametrizacaoPagamentoRepository repository;

	@Autowired
	private FormaPagamentoServices formaPagtoService;

	public RestDataReturnDTO findAll(PageRequest pageRequest) {
		Page<ParametrizacaoPagamento> parametrizacaoPagamento = this.repository.findAll(pageRequest);
		if (parametrizacaoPagamento.isEmpty()) {
			throw new ControllerNotFoundException("Nenhum Endereço para listar na pagina especificada.");
		}

		return new RestDataReturnDTO(parametrizacaoPagamento, new Paginator(parametrizacaoPagamento.getNumber(),parametrizacaoPagamento.getTotalElements(), parametrizacaoPagamento.getTotalPages()));
	}

	public ParametrizacaoPagamentoDTO findById(Long id) {
		ParametrizacaoPagamento parametrizacaoPagamento = this.repository.findById(id).orElseThrow(() -> new ControllerNotFoundException("Endereço não encontrado"));
		return new ParametrizacaoPagamentoDTO(parametrizacaoPagamento);
	}

	public ParametrizacaoPagamentoDTO save(ParametrizacaoPagamentoDTO dto) {
		ParametrizacaoPagamento entity = new ParametrizacaoPagamento();
		mapperDtoToEntity(dto,entity);
		if (dto.getFormaPagamento() != null){
			FormaPagamentoDTO fPagtoDTO = formaPagtoService.findById(dto.getFormaPagamento().getId());
			FormaPagamento formaPagto = new FormaPagamento();
			BeanUtils.copyProperties(fPagtoDTO, formaPagto);
			if (formaPagto.getDescricao().equalsIgnoreCase("pix") && entity.getPeriodoEstacionamento().equals(PeriodoEstacionamento.POR_HORA)){
				throw new ControllerNotFoundException("Opção pix limitada a tempo fixo.");
			}
			entity.setFormaPagamento(formaPagto);
		}
		var paramSaved = repository.save(entity);
		return new ParametrizacaoPagamentoDTO(paramSaved,paramSaved.getFormaPagamento());
	}

	public ParametrizacaoPagamentoDTO update(Long id, ParametrizacaoPagamentoDTO dto) {
		try {
			ParametrizacaoPagamento entity = repository.getOne(id);
			mapperDtoToEntity(dto,entity);
			entity = repository.save(entity);

			return new ParametrizacaoPagamentoDTO(entity,entity.getFormaPagamento());
		} catch (EntityNotFoundException e) {
			throw new EntityNotFoundException("Forma de pagamento não encontrado, id:" + id);
		}
	}
	public ParametrizacaoPagamentoDTO updateParametrizacaoPagtoByFields(Long id, Map<String, Object> fields) {
		ParametrizacaoPagamento existingParamPagto = repository.findById(id).orElseThrow(() -> new ControllerNotFoundException("Veiculo não encontrada"));
		fields.forEach((key, value) -> {
			Field field = ReflectionUtils.findField(Veiculo.class, key);
			field.setAccessible(true);
			ReflectionUtils.setField(field, existingParamPagto, value);
		});
		var ParamPagtoAtualizado = repository.save(existingParamPagto);
		return new ParametrizacaoPagamentoDTO(ParamPagtoAtualizado);
	}


	public String delete(Long id) {
		Optional<ParametrizacaoPagamento> OParametrizacaoPagamento = this.repository.findById(id);
		if (OParametrizacaoPagamento.isPresent()) {
			ParametrizacaoPagamento parametrizacaoPagamento = OParametrizacaoPagamento.get();
			this.repository.delete(parametrizacaoPagamento);
			return "Removida Forma de pagamentpo de ID: " + id;
		}
		throw new ControllerNotFoundException("Forma de pagamento não encontrada, id: " + id);
	}

	public List<String> validate(ParametrizacaoPagamentoDTO dto){
		Set<ConstraintViolation<ParametrizacaoPagamentoDTO>> violacoes = Validation.buildDefaultValidatorFactory().getValidator().validate(dto);
		List<String> violacoesToList = violacoes.stream()
				.map((violacao) -> violacao.getPropertyPath() + ":" + violacao.getMessage())
				.collect(Collectors.toList());
		return violacoesToList;
	}


	private void  mapperDtoToEntity(ParametrizacaoPagamentoDTO dto, ParametrizacaoPagamento entity){
		entity.setData(dto.getData());
		entity.setValorPorHora(dto.getValorPorHora());
		entity.setPeriodoEstacionamento(dto.getPeriodoEstacionamento());
	}
}
