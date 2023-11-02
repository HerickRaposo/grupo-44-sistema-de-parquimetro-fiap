
package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.condutores.sevices;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.condutores.dto.CondutorDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.condutores.dto.CondutorPatchDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.condutores.entities.Condutor;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.condutores.repositories.ICondutorRepository;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.dto.EnderecoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.dto.Paginator;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.dto.RestDataReturnDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.entities.Endereco;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.repositories.IEEnderecoRepository;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.services.EnderecoService;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.dto.VeiculoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.entities.Veiculo;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.exception.ControllerNotFoundException;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class CondutorService {

    @Autowired
    private ICondutorRepository repo;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private IEEnderecoRepository enderecoRepository;

    public RestDataReturnDTO findAll(PageRequest pagina){
    	List<CondutorDTO> PessoasDTO=new ArrayList<CondutorDTO>();
    	List<EnderecoDTO> enderecosDTO;
    	EnderecoDTO enderecoDTO;
        List<VeiculoDTO> veiculosDTO;
        VeiculoDTO veiculoDTO;

    	CondutorDTO pessoaDTO=null;
        ;
    	var condutores = repo.findAll(pagina);
        final Page<CondutorDTO> map = condutores.map(CondutorDTO::new);
        for (Condutor condutor : condutores.getContent()) {
    		enderecosDTO= new ArrayList<EnderecoDTO>();
            veiculosDTO = new ArrayList<VeiculoDTO>();

    		pessoaDTO   = new CondutorDTO(condutor);

          //PARSEAR DADOS DE PESSOA
			BeanUtils.copyProperties(condutor, pessoaDTO);
			PessoasDTO.add(pessoaDTO);

    		
    		 for (Endereco endereco : condutor.getEnderecos()) {
				//PARSSEAR DADOS DE ENDEREÇO
    			 enderecoDTO=new EnderecoDTO();
    			 BeanUtils.copyProperties(endereco, enderecoDTO);
    			 enderecosDTO.add(enderecoDTO);
			}

            pessoaDTO.setEnderecos(enderecosDTO);

             for (Veiculo veiculo: condutor.getVeiculos()){
                 //PARSSEAR DADOS DE VEICULO
                 veiculoDTO = new VeiculoDTO();
                 BeanUtils.copyProperties(veiculo, veiculoDTO);
                 veiculosDTO.add(veiculoDTO);
             }

             pessoaDTO.setVeiculos(veiculosDTO);
		}

    	return new RestDataReturnDTO(PessoasDTO, new Paginator(condutores.getNumber(), condutores.getTotalElements(), condutores.getTotalPages()));

    }

    @Transactional(readOnly = true)
    public CondutorDTO findById(Long id) {
        var pessoa = repo.findById(id).orElseThrow(() -> new ControllerNotFoundException("Pessoa não encontrada"));
        return new CondutorDTO(pessoa);
    }

    @Transactional
    public CondutorDTO insert(CondutorDTO dto) {
        Condutor entity = new Condutor();
        mapperDtoToEntity(dto,entity);
        var  pessoaSaved = repo.save(entity);
        return new CondutorDTO(pessoaSaved);
    }

    @Transactional
    public CondutorDTO update(Long id,CondutorDTO dto){
        try {
            Condutor entity = repo.getOne(id);
            mapperDtoToEntity(dto,entity);
            entity = repo.save(entity);

            return new CondutorDTO(entity);
        }catch (EntityNotFoundException ene){
            throw new ControllerNotFoundException("Pessoa não encontrada, id: " + id);
        }

    }

    public CondutorPatchDTO updatePessoaByFields(Long id, Map<String, Object> fields) {
        Condutor existingPessoa = repo.findById(id).orElseThrow(() -> new ControllerNotFoundException("Pessoa não encontrada"));
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Condutor.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, existingPessoa, value);
        });
        var pessoaAtualizada = repo.save(existingPessoa);
        return new CondutorPatchDTO(pessoaAtualizada);
    }

    public Long deletePessoa(Long id) {
        Optional<Condutor> existingPessoa = repo.findById(id);
        if (existingPessoa.isPresent()) {
            repo.deleteById(id);
        } else {
            throw new ControllerNotFoundException("Pessoa não encontrada, impossível deletar se não existe pessoa no id: " + id);
        }
        return repo.count();
    }

    @Transactional(readOnly = true)
    public void mapperDtoToEntity(CondutorDTO dto, Condutor entity) {
        entity.setNome(dto.getNome());
        entity.setSobrenome(dto.getSobrenome());
        entity.setDataNascimento(dto.getDataNascimento());
        entity.setSexo(dto.getSexo());
        entity.setIdade(dto.getIdade());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setCell(dto.getCell());
        entity.setFotosUrls(dto.getFotosUrls());
        entity.setNat(dto.getNat());

    }

}
