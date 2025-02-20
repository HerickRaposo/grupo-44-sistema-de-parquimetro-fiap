package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.services;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.adpters.dto.EmailDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.adpters.in.email.EmailSenderService;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.dto.AlocacaoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.entities.Alocacao;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.repositories.IAlocacaoRepository;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.dto.Paginator;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.dto.RestDataReturnDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.repositories.IEstacionamentoRepository;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.entities.PeriodoEstacionamento;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.repositories.IEParametrizacaoPagamentoRepository;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.entities.Veiculo;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.repositories.IVeiculoRepository;
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
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlocacaoService {
    @Autowired
    private EmailSenderService emailService;
    @Autowired
    private IAlocacaoRepository repository;
    @Autowired
    private IVeiculoRepository veiculoRepository;
    @Autowired
    private IEstacionamentoRepository estacRepository;

    @Autowired
    private IEParametrizacaoPagamentoRepository parametrizacaoPagtoRepository;

    public AlocacaoDTO retornaFiltroFormatado(String dataEntrada, String dataSaida, String dataInicioPago, String dataFimPago){
        try {
            AlocacaoDTO filtro = new AlocacaoDTO();
            if (dataEntrada != null && !dataEntrada.isBlank()){
                Date dtEntrada = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataEntrada);
                filtro.setDataEntrada(dtEntrada);
            }

            if (dataSaida != null && !dataSaida.isBlank()){
                Date dtSaida = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataSaida);
                filtro.setDataSaida(dtSaida);
            }
            if (dataFimPago != null && !dataInicioPago.isBlank()){
                Date dataIniPago = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicioPago);
                filtro.setDataInicioPago(dataIniPago);
            }
            if (dataFimPago != null && !dataFimPago.isBlank()){
                Date dtFimPago = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimPago);
                filtro.setDataFimPago(dtFimPago);
            }
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
            alocacaoDTO = new AlocacaoDTO(alocacao,alocacao.getVeiculo(),alocacao.getEstacionamento(),alocacao.getParametrizacaoPagto());
            BeanUtils.copyProperties(alocacao, alocacaoDTO);
            listaAlocacoesDTO.add(alocacaoDTO);
        }
        return new RestDataReturnDTO(listaAlocacoesDTO, new Paginator(alocacoes.getNumber(), alocacoes.getTotalElements(), alocacoes.getTotalPages()));
    }
    public AlocacaoDTO findById(Long id) {
        var alocacao = repository.findById(id).orElseThrow(() -> new ControllerNotFoundException("Alocacao não encontrada"));
        return new AlocacaoDTO(alocacao,alocacao.getVeiculo(),alocacao.getEstacionamento(),alocacao.getParametrizacaoPagto());
    }

    public AlocacaoDTO iniciarAlocacao(AlocacaoDTO dto) {
        Alocacao entity = new Alocacao();
        mapperDtoToEntity(dto,entity);
        if (entity.getEstacionamento() == null || entity.getEstacionamento().getEstado().equals(Boolean.FALSE)){
             throw new ControllerNotFoundException("Estacionamento não disponivel");
        }
        var alocacaoSaved = repository.save(entity);
        entity.getEstacionamento().setEstado(Boolean.FALSE);
        estacRepository.save(entity.getEstacionamento());
        return new AlocacaoDTO(alocacaoSaved,alocacaoSaved.getVeiculo(),alocacaoSaved.getEstacionamento(),alocacaoSaved.getParametrizacaoPagto());
    }

    public AlocacaoDTO update(Long id, AlocacaoDTO dto) {
        try {
            Alocacao buscaAlocacao = repository.getOne(id);
            mapperDtoToEntity(dto,buscaAlocacao);
            buscaAlocacao = repository.save(buscaAlocacao);

            return new AlocacaoDTO(buscaAlocacao,buscaAlocacao.getVeiculo(),buscaAlocacao.getEstacionamento(),buscaAlocacao.getParametrizacaoPagto());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Estacionamento não encontrado, id:" + id);
        }
    }

    public AlocacaoDTO updateAlocacaoByFields(Long id, Map<String, Object> fields) {
        Alocacao existingAlocacao = repository.findById(id).orElseThrow(() -> new ControllerNotFoundException("Alocação não encontrada"));
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Alocacao.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, existingAlocacao, value);
        });
        var alocacao = repository.save(existingAlocacao);
        return new AlocacaoDTO(alocacao);
    }

    public  AlocacaoDTO finalizarAlocacao(Long id) {
        Alocacao entity = new Alocacao();
        AlocacaoDTO dto = findById(id);
        dto.setDataSaida(new Date());

        //Alocação por hora
        if (dto.getDataFimPago() == null) {
            dto.setDataFimPago(new Date());
        }

        mapperDtoToEntity(dto,entity);
        var alocacaoSaved = repository.save(entity);
        entity.getEstacionamento().setEstado(Boolean.TRUE);
        estacRepository.save(entity.getEstacionamento());
        return new AlocacaoDTO(alocacaoSaved,alocacaoSaved.getVeiculo(),alocacaoSaved.getEstacionamento(),alocacaoSaved.getParametrizacaoPagto());
    }


    public void controlaTempoAlocacao(Long id){
        var alocacaoDTO = findById(id);
        Veiculo veiculo = veiculoRepository.getReferenceById(alocacaoDTO.getVeiculo().getId());
        //Estacionamento alocado para um veiculo
        if (alocacaoDTO.getDataSaida() == null && !alocacaoDTO.getEstacionamento().getEstado()){

            //Pega hora atual
            Calendar calAtual = Calendar.getInstance();
            calAtual.set(Calendar.SECOND, 0);


            if (alocacaoDTO.getParametrizacaoPagto().getPeriodoEstacionamento().equals(PeriodoEstacionamento.FIXO)){
                Calendar calFim = Calendar.getInstance();
                calFim.setTime(alocacaoDTO.getDataFimPago());
                calFim.set(Calendar.SECOND, 0);

                calFim.add(Calendar.MINUTE, -10);
                Date dezMinAntes = calFim.getTime();
                if ((calAtual.getTime() == dezMinAntes || calAtual.getTime().after(dezMinAntes)) && calAtual.getTime().before(alocacaoDTO.getDataFimPago())){
                    //Calcula diferença entre periodo de tempo
                    long diferencaEmMilissegundos = alocacaoDTO.getDataFimPago().getTime() - calAtual.getTimeInMillis();

                    // Converte a diferença em milissegundos para horas e minutos
                    long diferencaEmMinutos = diferencaEmMilissegundos / (60 * 1000);
                    long horas = diferencaEmMinutos / 60;
                    long minutos = diferencaEmMinutos % 60;

                    System.out.println("Sua alocação expira em " + horas + ":" + "minutos");

                    EmailDTO email = new EmailDTO(veiculo.getCondutor().getEmail(),"Tempo restante alocação", "Sua alocação espira em "  + horas + ":" + minutos);
                    emailService.enviarEmail(email);
                    //Envia mensagem tempo fixo;
                    //EmailDTO email = new EmailDTO(alocacaoDTO)
                }
            } else {
                //Pega hora de entrada e zera segundos
                Calendar cal = Calendar.getInstance();
                cal.setTime(alocacaoDTO.getDataEntrada());
                cal.set(Calendar.SECOND, 0);

                //Adiciona uma hora
                cal.add(Calendar.HOUR_OF_DAY, 1);
                Date umaHoraDepois = cal.getTime();

                if (alocacaoDTO.getDataSaida() == null && (calAtual.getTime() == umaHoraDepois || calAtual.getTime().after(umaHoraDepois))){
                    EmailDTO email = new EmailDTO(veiculo.getCondutor().getEmail(),"Renovação automatica alocação", "Sua alocação será renovada automaticamente por mais uma hora ");
                    emailService.enviarEmail(email);
                }

            }

            Map<String,Object> mapaDtaVerificacao = new HashMap<>();
            mapaDtaVerificacao.put("ultimaVerificacao", calAtual.getTime());
            updateAlocacaoByFields(alocacaoDTO.getId(), mapaDtaVerificacao);

        }

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
        if (dto.getDataEntrada() != null) {
            entity.setDataEntrada(dto.getDataEntrada());
        } else {
            entity.setDataSaida(new Date());
        }
        entity.setDataSaida(dto.getDataSaida());
        entity.setDataInicioPago(dto.getDataInicioPago());
        entity.setDataFimPago(dto.getDataFimPago());
        entity.setVeiculo(veiculoRepository.getReferenceById(dto.getVeiculo().getId()));
        entity.setEstacionamento(estacRepository.getReferenceById(dto.getEstacionamento().getId()));
        entity.setParametrizacaoPagto(parametrizacaoPagtoRepository.getReferenceById(dto.getParametrizacaoPagto().getId()));
    }
}
