package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.services;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.dto.AlocacaoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.entities.Alocacao;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.dto.EstacionamentoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.dto.Paginator;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.dto.RestDataReturnDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.entities.Estacionamento;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.repositories.IEstacionamentoRepository;
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
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EstacionamentoService {
    @Autowired
    private IEstacionamentoRepository repository;

    public RestDataReturnDTO findAll(EstacionamentoDTO filtro, PageRequest pagina) {
        List<EstacionamentoDTO> estacionamentosDTO= new ArrayList<EstacionamentoDTO>();
        AlocacaoDTO alocacaoDTO;
        List<AlocacaoDTO> alocacoesList = new ArrayList<>();


        Specification<Estacionamento> specification = Specification.where(null);
        if (!StringUtils.isEmpty(filtro.getDescricao())) {
            specification = specification.and((root, query, builder) ->
                    builder.like(builder.lower(root.get("descricao")), "%" + filtro.getDescricao().toLowerCase() + "%"));
        }

        if (!StringUtils.isEmpty(filtro.getEstado())) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get("estado"), filtro.getEstado()));
        }
        EstacionamentoDTO estacionamentoDTO=null;
        var estacionamentos = repository.findAll(specification,pagina);
        final Page<EstacionamentoDTO> map = estacionamentos.map(EstacionamentoDTO::new);

        for (Estacionamento estacionamento : estacionamentos.getContent()) {
            estacionamentoDTO = new EstacionamentoDTO(estacionamento,estacionamento.getEndereco(),estacionamento.getListaAlocacao());
            BeanUtils.copyProperties(estacionamento, estacionamentoDTO);
            estacionamentosDTO.add(estacionamentoDTO);

            List<AlocacaoDTO> alocacoesDTO = estacionamento.getListaAlocacao().stream()
                    .map(AlocacaoDTO::new)
                    .collect(Collectors.toList());

            estacionamentoDTO.setListaAlocacao(alocacoesDTO);

                final List<Alocacao> listaAlocacoes = estacionamento.getListaAlocacao();
            for (Alocacao alocacao : estacionamento.getListaAlocacao()) {
                alocacaoDTO=new AlocacaoDTO();
                BeanUtils.copyProperties(alocacao, alocacaoDTO);
                alocacoesList.add(alocacaoDTO);
            }
            estacionamentoDTO.setListaAlocacao(alocacoesList);
        }
        return new RestDataReturnDTO(estacionamentosDTO, new Paginator(estacionamentos.getNumber(), estacionamentos.getTotalElements(), estacionamentos.getTotalPages()));

    }

    public EstacionamentoDTO findById(Long id) {
        var estacionamento = repository.findById(id).orElseThrow(() -> new ControllerNotFoundException("Estacionamento não encontrado"));
        return new EstacionamentoDTO(estacionamento,estacionamento.getEndereco(),estacionamento.getListaAlocacao());
    }

    public EstacionamentoDTO save(EstacionamentoDTO dto) {
        Estacionamento entity = new Estacionamento();
        mapperDtoToEntity(dto,entity);
        var estacSaved = repository.save(entity);
        return new EstacionamentoDTO(estacSaved,estacSaved.getEndereco(),estacSaved.getListaAlocacao());
    }

    public EstacionamentoDTO update(Long id, EstacionamentoDTO dto) {
        try {
            Estacionamento buscaEstacionamento = repository.getOne(id);
            mapperDtoToEntity(dto,buscaEstacionamento);
            buscaEstacionamento = repository.save(buscaEstacionamento);

            return new EstacionamentoDTO(buscaEstacionamento,buscaEstacionamento.getEndereco(),buscaEstacionamento.getListaAlocacao());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Estacionamento não encontrado, id:" + id);
        }
    }


    public EstacionamentoDTO updateEstacionamentoByFields(Long id, Map<String, Object> fields) {
        Estacionamento existingEstacionamento = repository.findById(id).orElseThrow(() -> new ControllerNotFoundException("Estacionamento não encontrada"));
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Estacionamento.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, existingEstacionamento, value);
        });
        var eletroAtualizado = repository.save(existingEstacionamento);
        return new EstacionamentoDTO(eletroAtualizado);
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception ex) {
            throw new EntityNotFoundException("Erro ao deletar Estacionamento: " + id);
        }

    }

    public List<String> validate(EstacionamentoDTO dto){
        Set<ConstraintViolation<EstacionamentoDTO>> violacoes = Validation.buildDefaultValidatorFactory().getValidator().validate(dto);
        List<String> violacoesToList = violacoes.stream()
                .map((violacao) -> violacao.getPropertyPath() + ":" + violacao.getMessage())
                .collect(Collectors.toList());
        return violacoesToList;
    }

    private void  mapperDtoToEntity(EstacionamentoDTO dto, Estacionamento entity){
        entity.setDescricao(dto.getDescricao());
        entity.setEstado(dto.getEstado());
    }
}
