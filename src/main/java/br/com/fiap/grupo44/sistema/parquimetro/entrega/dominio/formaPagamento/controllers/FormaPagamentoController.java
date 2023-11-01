package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.controllers;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.dto.FormaPagamentoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.dto.RestDataReturnDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.services.FormaPagamentoServices;
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
@RequestMapping(value = "/formaPagto",produces = {"application/json"})
@Tag(name = "API DE FORMA DE PAGAMENTO")
public class FormaPagamentoController {
    @Autowired
    private FormaPagamentoServices formaPagtoService;
    @Operation(summary = "Retorna lista de formas de pagamento",method = "GET")
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
        return  formaPagtoService.findAll(pageRequest);
    }
    @Operation(summary = "Consulta formas de pagamento  por id",method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @GetMapping("/{id}")
    public ResponseEntity<FormaPagamentoDTO> findById(@PathVariable Long id) {
        var formaPagto = formaPagtoService.findById(id);
        return ResponseEntity.ok(formaPagto);
    }
    @Operation(summary = "Insere forma de pagamento",method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @PostMapping
    public ResponseEntity save(@RequestBody FormaPagamentoDTO formaPagtoDTO) {
        List<String> violacoesToList = formaPagtoService.validate(formaPagtoDTO);
        if (!violacoesToList.isEmpty()) {
            return ResponseEntity.badRequest().body(violacoesToList);
        }
        var formaPagtoSaved = formaPagtoService.save(formaPagtoDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand((formaPagtoSaved.getId())).toUri();
        return ResponseEntity.created(uri).body(formaPagtoSaved);
    }

    @Operation(summary = "Atualiza forma de pagamento",method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody FormaPagamentoDTO formaPagtoDTO, @PathVariable Long id) {
        List<String> violacoesToList = formaPagtoService.validate(formaPagtoDTO);
        if (!violacoesToList.isEmpty()) {
            return ResponseEntity.badRequest().body(violacoesToList);
        }
        var paramUpdated = formaPagtoService.update(id,formaPagtoDTO);
        return  ResponseEntity.ok(paramUpdated);
    }
    @Operation(summary = "Atualiza atributo especifico da forma   pagamento",method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @PatchMapping("/{id}")
    public FormaPagamentoDTO updateveiculoFiedls(@PathVariable Long id, @RequestBody Map<String, Object> fields){
        return formaPagtoService.updateFormaPagtoByFields(id,fields);
    }
    @Operation(summary = "Deleta forma de pagamento",method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        formaPagtoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
