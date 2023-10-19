package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.services;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.dto.AlocacaoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.dto.VeiculoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.entities.Veiculo;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.repositories.IVeiculoRepository;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.exception.ControllerNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
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
public class VeiculoService {
    @Autowired
    private IVeiculoRepository repository;

    public Page<VeiculoDTO> findAll(VeiculoDTO filtro, PageRequest pagina) {

        AlocacaoDTO alocacaoDTO;


        Specification<Veiculo> specification = Specification.where(null);
        if (!StringUtils.isEmpty(filtro.getMatricula())) {
            specification = specification.and((root, query, builder) ->
                    builder.like(builder.lower(root.get("matricula")), "%" + filtro.getMatricula().toLowerCase() + "%"));
        }

        if (!StringUtils.isEmpty(filtro.getMarca())) {
            specification = specification.and((root, query, builder) ->
                    builder.like(builder.lower(root.get("marca")), "%" + filtro.getMarca().toLowerCase() + "%"));
        }

        if (!StringUtils.isEmpty(filtro.getModelo())) {
            specification = specification.and((root, query, builder) ->
                    builder.like(builder.lower(root.get("modelo")), "%" + filtro.getModelo().toLowerCase() + "%"));
        }

        var veiculos = repository.findAll(specification,pagina);
        return veiculos.map(veiculo -> new VeiculoDTO(veiculo,veiculo.getAlocacoes()));
    }

    public VeiculoDTO findById(Long id) {
        var veiculo = repository.findById(id).orElseThrow(() -> new ControllerNotFoundException("Veiculo não encontrado"));
        return new VeiculoDTO(veiculo,veiculo.getAlocacoes());
    }

    public VeiculoDTO save(VeiculoDTO dto) {
        Veiculo entity = new Veiculo();
        mapperDtoToEntity(dto,entity);
        var veiculoSaved = repository.save(entity);
        return new VeiculoDTO(veiculoSaved,veiculoSaved.getAlocacoes());
    }


    public VeiculoDTO update(Long id, VeiculoDTO dto) {
        try {
            Veiculo buscaVeiculo = repository.getOne(id);
            mapperDtoToEntity(dto,buscaVeiculo);
            buscaVeiculo = repository.save(buscaVeiculo);

            return new VeiculoDTO(buscaVeiculo,buscaVeiculo.getAlocacoes());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Veiculo não encontrado, id:" + id);
        }
    }

    public VeiculoDTO updateVeiculoByFields(Long id, Map<String, Object> fields) {
        Veiculo existingVeiculo = repository.findById(id).orElseThrow(() -> new ControllerNotFoundException("Veiculo não encontrada"));
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Veiculo.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, existingVeiculo, value);
        });
        var eletroAtualizado = repository.save(existingVeiculo);
        return new VeiculoDTO(eletroAtualizado);
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception ex) {
            throw new EntityNotFoundException("Erro ao deletar Veiculo: " + id);
        }

    }

    public List<String> validate(VeiculoDTO dto){
        Set<ConstraintViolation<VeiculoDTO>> violacoes = Validation.buildDefaultValidatorFactory().getValidator().validate(dto);
        List<String> violacoesToList = violacoes.stream()
                .map((violacao) -> violacao.getPropertyPath() + ":" + violacao.getMessage())
                .collect(Collectors.toList());
        return violacoesToList;
    }

    private void  mapperDtoToEntity(VeiculoDTO dto, Veiculo entity){
        entity.setMarca(dto.getMarca());
        entity.setModelo(dto.getModelo());
        entity.setMatricula(dto.getMatricula());
        entity.setCavalos(dto.getCavalos());
        entity.setIdCondutor(dto.getIdCondutor());
    }

}
