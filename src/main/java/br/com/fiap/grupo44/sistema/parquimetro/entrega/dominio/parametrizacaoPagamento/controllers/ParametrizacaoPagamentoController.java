package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.controllers;


import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.dto.RestDataReturnDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.dto.ParametrizacaoPagamentoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.services.ParametrizacaoPagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


public class ParametrizacaoPagamentoController {
    @Autowired
    private ParametrizacaoPagamentoService paramPagtoService;
    @Operation(summary = "Retorna lista de alocacoes paginada podendo ser filtrada por marca,modelo,matriculaa",method = "GET")
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
    @Operation(summary = "Consulta alocação por id",method = "GET")
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
    @Operation(summary = "Insere alocação",method = "POST")
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

    @Operation(summary = "Atualiza alocação",method = "PUT")
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
        var paramUpdated = paramPagtoService.update(paramPagtoDTO, id);
        return  ResponseEntity.ok(paramUpdated);
    }
    @Operation(summary = "Atualiza atributo especifico da alocação",method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @PatchMapping("/{id}")
    public ParametrizacaoPagamentoDTO updateveiculoFiedls(@PathVariable Long id, @RequestBody ParametrizacaoPagamentoDTO parametrizacaoPagamentoDTO){
        return paramPagtoService.update(parametrizacaoPagamentoDTO,id);
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
