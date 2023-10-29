package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.controllers;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.dto.AlocacaoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.services.AlocacaoService;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.dto.RestDataReturnDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/alocacao",produces = {"application/json"})
@Tag(name = "API ALOCAÇÃO")
public class AlocacaoController {
    @Autowired
    private AlocacaoService alocacaoService;
    @Operation(summary = "Retorna lista de alocacoes paginada podendo ser filtrada por marca,modelo,matriculaa",method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @GetMapping
    public RestDataReturnDTO findAll(
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "tamanho", defaultValue = "10") Integer tamanho,
            @RequestParam(required = false) String dataEntrada,
            @RequestParam(required = false) String dataSaida,
            @RequestParam(required = false) String dataInicioPago,
            @RequestParam(required = false) String dataFimPago
    ) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        AlocacaoDTO filtro = alocacaoService.retornaFiltroFormatado(dataEntrada,dataSaida,dataInicioPago,dataFimPago);
        return  alocacaoService.findAll(filtro,pageRequest);
    }
    @Operation(summary = "Consulta alocação por id",method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @GetMapping("/{id}")
    public ResponseEntity<AlocacaoDTO> findById(@PathVariable Long id) {
        var alocacao = alocacaoService.findById(id);
        return ResponseEntity.ok(alocacao);
    }
    @Operation(summary = "Controle de tempo de alocação por id",method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @GetMapping("/{id}")
    public ResponseEntity<String> controlaTempoAlocacao(@PathVariable Long id) {
        alocacaoService.controlaTempoAlocacao(id);
        return ResponseEntity.ok("Controle realizado");
    }

    @Operation(summary = "Insere alocação",method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @PostMapping
    public ResponseEntity save(@RequestBody AlocacaoDTO alocacaoDTO) {
        List<String> violacoesToList = alocacaoService.validate(alocacaoDTO);
        if (!violacoesToList.isEmpty()) {
            return ResponseEntity.badRequest().body(violacoesToList);
        }
        var alocacaoSaved = alocacaoService.save(alocacaoDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand((alocacaoSaved.getId())).toUri();
        return ResponseEntity.created(uri).body(alocacaoSaved);
    }

    @Operation(summary = "Atualiza alocação",method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody AlocacaoDTO alocacaoDTO, @PathVariable Long id) {
        List<String> violacoesToList = alocacaoService.validate(alocacaoDTO);
        if (!violacoesToList.isEmpty()) {
            return ResponseEntity.badRequest().body(violacoesToList);
        }
        var alocacaoUpdated = alocacaoService.update(id, alocacaoDTO);
        return  ResponseEntity.ok(alocacaoUpdated);
    }
    @Operation(summary = "Atualiza atributo especifico da alocação",method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @PatchMapping("/{id}")
    public AlocacaoDTO updateveiculoFiedls(@PathVariable Long id, @RequestBody Map<String, Object> fields){
        return alocacaoService.updateAlocacaoByFields(id,fields);
    }
    @Operation(summary = "Deleta alocação",method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        alocacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
