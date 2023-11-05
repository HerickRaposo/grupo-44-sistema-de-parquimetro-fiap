package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.controllers;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.dto.EstacionamentoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.dto.RestDataReturnDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.services.EstacionamentoService;

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
@RequestMapping(value = "/estacionamento",produces = {"application/json"})
@Tag(name = "API ESTACIONAMENTO")
public class EstacionamentoController {
    @Autowired
    private EstacionamentoService estacionamentoService;
    @Operation(summary = "Retorna lista de estacionamentos paginada podendo ser filtrada por marca,modelo,matriculaa",method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @GetMapping
    public RestDataReturnDTO findAll(
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "tamanho", defaultValue = "10") Integer tamanho,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) String estado
    ) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        EstacionamentoDTO filtro = new EstacionamentoDTO();
        filtro.setDescricao(descricao);
        if (filtro.getEstado() != null){
            filtro.setEstado("true".equals(estado));
        }

        return  estacionamentoService.findAll(filtro,pageRequest);
    }
    @Operation(summary = "Consulta estacionamento por id",method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @GetMapping("/{id}")
    public ResponseEntity<EstacionamentoDTO> findById(@PathVariable Long id) {
        var estacionamento = estacionamentoService.findById(id);
        return ResponseEntity.ok(estacionamento);
    }

    @Operation(summary = "Insere estacionamento",method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @PostMapping
    public ResponseEntity save(@RequestBody EstacionamentoDTO EstacionamentoDTO) {
        List<String> violacoesToList = estacionamentoService.validate(EstacionamentoDTO);
        if (!violacoesToList.isEmpty()) {
            return ResponseEntity.badRequest().body(violacoesToList);
        }
        var estacSaved = estacionamentoService.save(EstacionamentoDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand((estacSaved.getId())).toUri();
        return ResponseEntity.created(uri).body(estacSaved);
    }

    @Operation(summary = "Atualiza estacionamento",method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody EstacionamentoDTO EstacionamentoDTO, @PathVariable Long id) {
        List<String> violacoesToList = estacionamentoService.validate(EstacionamentoDTO);
        if (!violacoesToList.isEmpty()) {
            return ResponseEntity.badRequest().body(violacoesToList);
        }
        var estacUpdated = estacionamentoService.update(id, EstacionamentoDTO);
        return  ResponseEntity.ok(estacUpdated);
    }
    @Operation(summary = "Atualiza atributo especifico do estacionamento",method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @PatchMapping("/{id}")
    public EstacionamentoDTO updateveiculoFiedls(@PathVariable Long id, @RequestBody Map<String, Object> fields){
        return estacionamentoService.updateEstacionamentoByFields(id,fields);
    }
    @Operation(summary = "Deleta estacionamento",method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        estacionamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
