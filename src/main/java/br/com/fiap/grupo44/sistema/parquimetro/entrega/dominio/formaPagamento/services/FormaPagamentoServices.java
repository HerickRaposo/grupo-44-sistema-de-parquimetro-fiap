package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.dto.FormaPagamentoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.entities.Veiculo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.dto.RestDataReturnDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.dto.Paginator;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.entities.FormaPagamento;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.repositories.IEFormaPagamentoRepository;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.exception.ControllerNotFoundException;
import org.springframework.util.ReflectionUtils;

@Service
public class FormaPagamentoServices {

    @Autowired
    private IEFormaPagamentoRepository repository;

    public RestDataReturnDTO findAll(PageRequest pageRequest) {
        Page<FormaPagamento> formaPagamento = this.repository.findAll(pageRequest);
        if (formaPagamento.isEmpty()) {
            throw new ControllerNotFoundException("Nenhum Endereço para listar na pagina especificada.");
        }

        return new RestDataReturnDTO(formaPagamento, new Paginator(formaPagamento.getNumber(), formaPagamento.getTotalElements(), formaPagamento.getTotalPages()));
    }

    public FormaPagamentoDTO findById(Long id) {
        FormaPagamento formaPagamento = this.repository.findById(id).orElseThrow(() -> new ControllerNotFoundException("Endereço não encontrado"));
        return new FormaPagamentoDTO(formaPagamento);
    }

    public FormaPagamentoDTO save(FormaPagamentoDTO dto) {
        FormaPagamento entity = new FormaPagamento();
        mapperDtoToEntity(dto, entity);
        var formaPagtoSaved = repository.save(entity);
        return new FormaPagamentoDTO(formaPagtoSaved);
    }

    public FormaPagamentoDTO update(Long id, FormaPagamentoDTO dto) {
        try {
            FormaPagamento entity = repository.getOne(id);
            mapperDtoToEntity(dto, entity);
            entity = repository.save(entity);

            return new FormaPagamentoDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Forma pagamento  não encontrado, id:" + id);
        }
    }

    public FormaPagamentoDTO updateFormaPagtoByFields(Long id, Map<String, Object> fields) {
        FormaPagamento existingFormaPagto = repository.findById(id).orElseThrow(() -> new ControllerNotFoundException("Veiculo não encontrada"));
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Veiculo.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, existingFormaPagto, value);
        });
        var formaPagtoAtualizado = repository.save(existingFormaPagto);
        return new FormaPagamentoDTO(formaPagtoAtualizado);
    }


    public String delete(Long id) {
        Optional<FormaPagamento> OFormaPagamento = this.repository.findById(id);
        if (OFormaPagamento.isPresent()) {
            FormaPagamento formaPagamento = OFormaPagamento.get();
            this.repository.delete(formaPagamento);
            return "Removida Forma de pagamentpo de ID: " + id;
        }
        throw new ControllerNotFoundException("Forma de pagamento não encontrada, id: " + id);
    }

    public List<String> validate(FormaPagamentoDTO dto) {
        Set<ConstraintViolation<FormaPagamentoDTO>> violacoes = Validation.buildDefaultValidatorFactory().getValidator().validate(dto);
        List<String> violacoesToList = violacoes.stream()
                .map((violacao) -> violacao.getPropertyPath() + ":" + violacao.getMessage())
                .collect(Collectors.toList());
        return violacoesToList;
    }

    private void mapperDtoToEntity(FormaPagamentoDTO dto, FormaPagamento entity) {
        entity.setDescricao(dto.getDescricao());
        entity.setEstado(dto.getEstado());
    }

}
