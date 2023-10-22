package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.services;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.dto.AlocacaoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.entities.Alocacao;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.repositories.IAlocacaoRepository;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.dto.Paginator;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.dto.RestDataReturnDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.entities.Estacionamento;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.exception.ControllerNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlocacaoService {
    @Autowired
    private IAlocacaoRepository repository;

    public AlocacaoDTO retornaFiltroFormatado(String dataEntrada, String dataSaida, String dataInicioPago, String dataFimPago){
        try {
            Date dtEntrada = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataEntrada);
            Date dtSaida = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataSaida);
            Date dataIniPago = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicioPago);
            Date dtFimPago = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimPago);
            AlocacaoDTO filtro = new AlocacaoDTO();
            filtro.setDataEntrada(dtEntrada);
            filtro.setDataSaida(dtSaida);
            filtro.setDataInicioPago(dataIniPago);
            filtro.setDataFimPago(dtFimPago);
            return filtro;
        } catch (Exception e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public RestDataReturnDTO findAll(AlocacaoDTO filtro, PageRequest pagina) {
;       List<AlocacaoDTO> listaAlocacoesDTO = new ArrayList<>();
        AlocacaoDTO alocacaoDTO;


        Specification<Alocacao> specification = Specification.where(null);
        if (filtro.getDataEntrada() != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get("data_entrada"), filtro.getDataEntrada()));
        }

        if (filtro.getDataSaida() != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get("data_saida"), filtro.getDataSaida()));
        }

        if (filtro.getDataInicioPago() != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get("data_inicio_pago"), filtro.getDataInicioPago()));
        }

        if (filtro.getDataFimPago() != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get("data_fim_pago"), filtro.getDataFimPago()));
        }

        var alocacoes = repository.findAll(specification,pagina);
        final Page<AlocacaoDTO> map = alocacoes.map(AlocacaoDTO::new);

        for (Alocacao alocacao : alocacoes.getContent()) {
            alocacaoDTO = new AlocacaoDTO(alocacao,alocacao.getVeiculo(),alocacao.getEstacionamento());
            BeanUtils.copyProperties(alocacao, alocacaoDTO);
            listaAlocacoesDTO.add(alocacaoDTO);
        }
        return new RestDataReturnDTO(listaAlocacoesDTO, new Paginator(alocacoes.getNumber(), alocacoes.getTotalElements(), alocacoes.getTotalPages()));
    }
    public AlocacaoDTO findById(Long id) {
        var alocacao = repository.findById(id).orElseThrow(() -> new ControllerNotFoundException("Alocacao não encontrada"));
        return new AlocacaoDTO(alocacao,alocacao.getVeiculo(),alocacao.getEstacionamento());
    }

    public AlocacaoDTO save(AlocacaoDTO dto) {
        Alocacao entity = new Alocacao();
        mapperDtoToEntity(dto,entity);
        var alocacaoSaved = repository.save(entity);
        return new AlocacaoDTO(alocacaoSaved,alocacaoSaved.getVeiculo(),alocacaoSaved.getEstacionamento());
    }

    public AlocacaoDTO update(Long id, AlocacaoDTO dto) {
        try {
            Alocacao buscaAlocacao = repository.getOne(id);
            mapperDtoToEntity(dto,buscaAlocacao);
            buscaAlocacao = repository.save(buscaAlocacao);

            return new AlocacaoDTO(buscaAlocacao,buscaAlocacao.getVeiculo(),buscaAlocacao.getEstacionamento());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Estacionamento não encontrado, id:" + id);
        }
    }

    public AlocacaoDTO updateAlocacaoByFields(Long id, Map<String, Object> fields) {
        Alocacao existingAlocacao = repository.findById(id).orElseThrow(() -> new ControllerNotFoundException("Alocação não encontrada"));
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Estacionamento.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, existingAlocacao, value);
        });
        var alocacao = repository.save(existingAlocacao);
        return new AlocacaoDTO(alocacao);
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception ex) {
            throw new EntityNotFoundException("Erro ao deletar Estacionamento: " + id);
        }

    }

    public List<String> validate(AlocacaoDTO dto){
        Set<ConstraintViolation<AlocacaoDTO>> violacoes = Validation.buildDefaultValidatorFactory().getValidator().validate(dto);
        List<String> violacoesToList = violacoes.stream()
                .map((violacao) -> violacao.getPropertyPath() + ":" + violacao.getMessage())
                .collect(Collectors.toList());
        return violacoesToList;
    }

    private void  mapperDtoToEntity(AlocacaoDTO dto, Alocacao entity){
        entity.setDataEntrada(dto.getDataEntrada());
        entity.setDataSaida(dto.getDataSaida());
        entity.setDataInicioPago(dto.getDataInicioPago());
        entity.setDataFimPago(dto.getDataFimPago());
    }
}
