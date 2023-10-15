package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.controllers;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.dto.VeiculoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.services.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/veiculo",produces = {"application/json"})
@Tag(name = "API VEICULO")
public class VeiculoController {
    @Autowired
    private VeiculoService veiculoService;
    @Operation(summary = "Retorna lista de vriculod paginada podendo ser filtrada por marca,modelo,matriculaa",method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @GetMapping
    public ResponseEntity<Page<VeiculoDTO>> findAll(
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "tamanho", defaultValue = "10") Integer tamanho,
            @RequestParam(required = false) String matricula,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String modelo
    ) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        VeiculoDTO filtro = new VeiculoDTO();
        filtro.setMatricula(matricula);
        filtro.setMarca(marca);
        filtro.setModelo(modelo);
        var veiculos = veiculoService.findAll(filtro,pageRequest);
        return ResponseEntity.ok(veiculos);
    }
    @Operation(summary = "Consulta veiculo por id",method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @GetMapping("/{id}")
    public ResponseEntity<VeiculoDTO> findById(@PathVariable Long id) {
        var veiculo = veiculoService.findById(id);
        return ResponseEntity.ok(veiculo);
    }

    @Operation(summary = "Insere veiculo",method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @PostMapping
    public ResponseEntity save(@RequestBody VeiculoDTO veiculoDTO) {
        List<String> violacoesToList = veiculoService.validate(veiculoDTO);
        if (!violacoesToList.isEmpty()) {
            return ResponseEntity.badRequest().body(violacoesToList);
        }
        var veiculoSaved = veiculoService.save(veiculoDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand((veiculoSaved.getId())).toUri();
        return ResponseEntity.created(uri).body(veiculoSaved);
    }

    @Operation(summary = "Atualiza veiculo",method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody VeiculoDTO veiculoDTO, @PathVariable Long id) {
        List<String> violacoesToList = veiculoService.validate(veiculoDTO);
        if (!violacoesToList.isEmpty()) {
            return ResponseEntity.badRequest().body(violacoesToList);
        }
        var veiculoUpdated = veiculoService.update(id, veiculoDTO);
        return  ResponseEntity.ok(veiculoUpdated);
    }
    @Operation(summary = "Atualiza atributo especifico do veiculo",method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @PatchMapping("/{id}")
    public VeiculoDTO updateveiculoFiedls(@PathVariable Long id, @RequestBody Map<String, Object> fields){
        return veiculoService.updateVeiculoByFields(id,fields);
    }
    @Operation(summary = "Deleta veiculo",method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        veiculoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
