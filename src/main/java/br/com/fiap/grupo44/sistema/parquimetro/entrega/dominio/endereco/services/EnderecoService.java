package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.services;


import br.com.fiap.grupo44.sistema.parquimetro.entrega.adpters.dto.CepDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.adpters.dto.EnderecoResultViaCepDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.adpters.out.ServicoViaCepValidatorOut;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.condutores.dto.CondutorDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.condutores.entities.Condutor;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.dto.EnderecoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.dto.Paginator;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.dto.RestDataReturnDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.entities.Endereco;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.repositories.IEEnderecoRepository;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.dto.EstacionamentoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.entities.Estacionamento;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.exception.ControllerNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class EnderecoService {
 
	@Autowired
	private IEEnderecoRepository enderecoRepository;
	@Autowired
	private ServicoViaCepValidatorOut servicoViaSepValidator;


	public EnderecoDTO salvar(CepDTO cepDTO, int numeroDeEnereco) {
		EnderecoResultViaCepDTO enderecoResultViaCepDTO = this.servicoViaSepValidator.validarEndereco(cepDTO);
		
		Endereco OEndereco = this.enderecoRepository.BUSCAR_ENDERECO_POR_CEP(cepDTO.getCep());
 
		
		if(OEndereco==null) {
	    	Endereco enderecoEntity = enderecoResultViaCepDTO.getEndereco(cepDTO);
			enderecoEntity.setNumero(numeroDeEnereco);
	    	OEndereco=this.enderecoRepository.save(enderecoEntity);			    	
	    }else {
	    	this.enderecoRepository.SALVAR_ENDERECO(OEndereco.getId(), cepDTO.getCondutor().getId());
	    	System.err.println("PASSEI PELO SERVIÇO COM SUCESSO!");
	    }

		return new EnderecoDTO(OEndereco); 
	}
	public EnderecoDTO salvar(CepDTO cepDTO) {
		EnderecoResultViaCepDTO enderecoResultViaCepDTO = this.servicoViaSepValidator.validarEndereco(cepDTO);

		Endereco OEndereco = this.enderecoRepository.BUSCAR_ENDERECO_POR_CEP(cepDTO.getCep());


		if(OEndereco==null) {
			Endereco enderecoEntity = enderecoResultViaCepDTO.getEndereco(cepDTO);
			OEndereco=this.enderecoRepository.save(enderecoEntity);
		}else {
			this.enderecoRepository.SALVAR_ENDERECO(OEndereco.getId(), cepDTO.getCondutor().getId());
			System.err.println("PASSEI PELO SERVIÇO COM SUCESSO!");
		}

		return new EnderecoDTO(OEndereco);
	}
	
	public EnderecoDTO findById(Long id) {
		Endereco endereco = enderecoRepository.findById(id).orElseThrow(() -> new ControllerNotFoundException("Endereço não encontrado"));
		return new EnderecoDTO(endereco);
	}

	public RestDataReturnDTO findAll(PageRequest pageRequest){
		Page<Endereco> enderecos   = enderecoRepository.findAll(pageRequest);
     	List<EnderecoDTO> enderecosDTO= new ArrayList<EnderecoDTO>();
     	EnderecoDTO enderecoDTO;
     	List<CondutorDTO>   condutoresDTO;
		CondutorDTO pessoaDTO;
		List<EstacionamentoDTO> estacionamentosDTO;
		EstacionamentoDTO estacionamentoDTO;
        if(!enderecos.isEmpty()) {
        	for (Endereco endereco : enderecos) {
        		condutoresDTO   = new ArrayList<CondutorDTO>();
				estacionamentosDTO = new ArrayList<EstacionamentoDTO>();
        		enderecoDTO= new EnderecoDTO();
        		BeanUtils.copyProperties(endereco, enderecoDTO);
        		for (Condutor condutor : endereco.getCondutores()) {
        			pessoaDTO=new CondutorDTO();
        			BeanUtils.copyProperties(condutor, pessoaDTO);
        			condutoresDTO.add(pessoaDTO);
        		}
        		
        		enderecosDTO.add(enderecoDTO);

				for (Estacionamento estacionamento : endereco.getEstacionamentos()){
					estacionamentoDTO = new EstacionamentoDTO();
					BeanUtils.copyProperties(estacionamento, estacionamentoDTO);
					estacionamentosDTO.add(estacionamentoDTO);
				}

				enderecoDTO.setEstacionamentos(estacionamentosDTO);
				enderecosDTO.add(enderecoDTO);
        		
			}
        	return new RestDataReturnDTO(enderecosDTO, new Paginator(enderecos.getNumber(), enderecos.getTotalElements(), enderecos.getTotalPages()));
        }
        throw new ControllerNotFoundException("Nenhum Endereço para listar na pagina especificada.");
    }

	public EnderecoDTO atualizar(EnderecoDTO enderecoDTO,Long id) {
        Optional<Endereco> OPendereco = this.enderecoRepository.findById(id);
        try {
        	Endereco endereco = OPendereco.get();
        	endereco.setBairro(enderecoDTO.getBairro());
        	endereco.setCidade(enderecoDTO.getCidade());
        	endereco.setEstado(enderecoDTO.getEstado());
        	endereco.setNumero(enderecoDTO.getNumero());
        	endereco.setRua(enderecoDTO.getRua());
        	this.enderecoRepository.save(endereco);
        	return new EnderecoDTO(endereco);
        }catch (Exception e) {
            throw new ControllerNotFoundException("Endereço não encontrado, id: " + id);
		}
	}

	public String apagar(Long id) {
		Optional<Endereco> OPendereco = this.enderecoRepository.findById(id);
		if(OPendereco.isPresent()){
			Endereco endereco = OPendereco.get();
			this.enderecoRepository.delete(endereco);
			return "Removido o endereço de ID: "+id;
		}
        throw new ControllerNotFoundException("Endereço não encontrado, id: " + id);
	}
}
