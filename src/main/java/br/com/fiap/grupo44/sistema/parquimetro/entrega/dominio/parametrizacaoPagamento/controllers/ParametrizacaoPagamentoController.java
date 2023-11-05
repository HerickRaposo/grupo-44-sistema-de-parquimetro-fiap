package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.controllers;


import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.dto.RestDataReturnDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.dto.ParametrizacaoPagamentoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.services.ParametrizacaoPagamentoService;
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
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/parametrizacaoPagto",produces = {"application/json"})
@Tag(name = "API DE PARAMETRIZAÇÃO DE PAGAMENTO")
public class ParametrizacaoPagamentoController {
    @Autowired
    private ParametrizacaoPagamentoService paramPagtoService;
    @Operation(summary = "Retorna lista de parametros de pagamento",method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @GetMapping
    public RestDataReturnDTO findAll(
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "tamanho", defaultValue = "10") Integer tamanho
    ) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        return  paramPagtoService.findAll(pageRequest);
    }
    @Operation(summary = "Consulta parametros de pagamento  por id",method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @GetMapping("/{id}")
    public ResponseEntity<ParametrizacaoPagamentoDTO> findById(@PathVariable Long id) {
        var paramPagto = paramPagtoService.findById(id);
        return ResponseEntity.ok(paramPagto);
    }
    @Operation(summary = "Insere parametros de pagamento",method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @PostMapping
    public ResponseEntity save(@RequestBody ParametrizacaoPagamentoDTO paramPagtoDTO) {
        List<String> violacoesToList = paramPagtoService.validate(paramPagtoDTO);
        if (!violacoesToList.isEmpty()) {
            return ResponseEntity.badRequest().body(violacoesToList);
        }
        var paramPagtoSaved = paramPagtoService.save(paramPagtoDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand((paramPagtoSaved.getId())).toUri();
        return ResponseEntity.created(uri).body(paramPagtoSaved);
    }

    @Operation(summary = "Atualiza parametros de pagamento",method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody ParametrizacaoPagamentoDTO paramPagtoDTO, @PathVariable Long id) {
        List<String> violacoesToList = paramPagtoService.validate(paramPagtoDTO);
        if (!violacoesToList.isEmpty()) {
            return ResponseEntity.badRequest().body(violacoesToList);
        }
        var paramUpdated = paramPagtoService.update(id,paramPagtoDTO);
        return  ResponseEntity.ok(paramUpdated);
    }
    @Operation(summary = "Atualiza atributo especifico do  pagamento",method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @PatchMapping("/{id}")
    public ParametrizacaoPagamentoDTO updateveiculoFiedls(@PathVariable Long id, @RequestBody Map<String, Object> fields){
        return paramPagtoService.updateParametrizacaoPagtoByFields(id,fields);
    }
    @Operation(summary = "Deleta alocação",method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        paramPagtoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
