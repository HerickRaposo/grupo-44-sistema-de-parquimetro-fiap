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
    private IEFormaPagamentoRepository ieFormaPagamentoRepository;

    public FormaPagamentoDTO save(FormaPagamentoDTO formaPagamentoDTO) {
        FormaPagamento formaPagamento = new FormaPagamento(formaPagamentoDTO);
        FormaPagamento formaPagamentoSalva = this.ieFormaPagamentoRepository.save(formaPagamento);
        return new FormaPagamentoDTO(formaPagamentoSalva);
    }

    public FormaPagamentoDTO findById(Long id) {
        FormaPagamento formaPagamento = this.ieFormaPagamentoRepository.findById(id).orElseThrow(() -> new ControllerNotFoundException("Endereço não encontrado"));
        return new FormaPagamentoDTO(formaPagamento);
    }


    public RestDataReturnDTO findAll(PageRequest pageRequest) {
        Page<FormaPagamento> formaPagamento = this.ieFormaPagamentoRepository.findAll(pageRequest);
        if (formaPagamento.isEmpty()) {
            throw new ControllerNotFoundException("Nenhum Endereço para listar na pagina especificada.");
        }

        return new RestDataReturnDTO(formaPagamento, new Paginator(formaPagamento.getNumber(),formaPagamento.getTotalElements(), formaPagamento.getTotalPages()));
    }

    public FormaPagamentoDTO update(Long id,FormaPagamentoDTO enderecoDTO) {
        Optional<FormaPagamento> OFormaPagamento = this.ieFormaPagamentoRepository.findById(id);

        try {
            FormaPagamento formaPagamento = OFormaPagamento.get();
            formaPagamento.setDescricao(enderecoDTO.getDescricao());
            formaPagamento.setEstado(enderecoDTO.getEstado());
            this.ieFormaPagamentoRepository.save(formaPagamento);
            return new FormaPagamentoDTO(formaPagamento);
        } catch (Exception e) {
            throw new ControllerNotFoundException("Forma de pagamento não encontrada, id: " + id);
        }
    }
    public FormaPagamentoDTO updateFormaPagtoByFields(Long id, Map<String, Object> fields) {
        FormaPagamento existingFormaPagto = ieFormaPagamentoRepository.findById(id).orElseThrow(() -> new ControllerNotFoundException("Veiculo não encontrada"));
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Veiculo.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, existingFormaPagto, value);
        });
        var formaPagtoAtualizado = ieFormaPagamentoRepository.save(existingFormaPagto);
        return new FormaPagamentoDTO(formaPagtoAtualizado);
    }

    public String delete(Long id) {
        Optional<FormaPagamento> OFormaPagamento = this.ieFormaPagamentoRepository.findById(id);
        if (OFormaPagamento.isPresent()) {
            FormaPagamento formaPagamento = OFormaPagamento.get();
            this.ieFormaPagamentoRepository.delete(formaPagamento);
            return "Removida Forma de pagamentpo de ID: " + id;
        }
        throw new ControllerNotFoundException("Forma de pagamento não encontrada, id: " + id);
    }
}
