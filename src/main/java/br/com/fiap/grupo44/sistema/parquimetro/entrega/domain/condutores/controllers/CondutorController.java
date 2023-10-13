package br.com.fiap.grupo44.sistema.parquimetro.entrega.domain.condutores.controllers;


import br.com.fiap.grupo44.sistema.parquimetro.entrega.domain.condutores.dto.CondutorDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.domain.condutores.dto.CondutorPatchDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.domain.condutores.sevices.CondutorService;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.domain.endereco.dto.RestDataReturnDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping(value = "/condutores",produces = {"application/json"})
@Tag(name = "API CONDUTORES")
public class CondutorController {

    private CondutorService condutorService;

    public CondutorController(CondutorService condutorService){this.condutorService = condutorService;}


    @Operation(tags="Lista de condutores" ,summary = "Lista todos os condutores cadastrados no banco de dados",method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A lot of Condutor has found"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Condutor not found"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")})
    @GetMapping
    public RestDataReturnDTO findAll(
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "tamanho", defaultValue = "10") Integer tamanho
    ){
        PageRequest pageRequest = PageRequest.of(pagina,tamanho);
        return condutorService.findAll(pageRequest);
    }
    @Operation(summary = "Consulta condutor passando o id",method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Condutor was found by Id"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Condutor not found"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")})
    @GetMapping("/{id}")
    public CondutorDTO getProductById(@PathVariable Long id) {
        return condutorService.findById(id);
    }

    @Operation(summary = "Cadastro de condutores",method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Condutor was created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Condutor not found"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")})
    @PostMapping
    public ResponseEntity<CondutorDTO> insert(@RequestBody CondutorDTO condutor){
        condutor = condutorService.insert(condutor);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(condutor.getId()).toUri();
        return ResponseEntity.created(uri).body(condutor);
    }

    @Operation(summary = "Atualiza condutor em todos os campos passando o id do condutor",method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Found was Updated"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Condutor not found"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")})
    @PutMapping("/{id}")
    public ResponseEntity<CondutorDTO> update(@RequestBody CondutorDTO condutorDTO,@PathVariable Long id){
        return ResponseEntity.ok(condutorService.update(id,condutorDTO));
    }

    @Operation(summary = "Atualiza condutor em um único campo desejado",method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Condutor was update "),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Condutor not found"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")})
    @PatchMapping("/{id}")
    public CondutorPatchDTO updateCondutorFiedls(@PathVariable Long id, @RequestBody Map<String, Object> fields){
        return condutorService.updatePessoaByFields(id,fields);
    }

    @Operation(summary = "Apaga do banco de dados passando, apenas o id do condutor",method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Condutor not found"),
            @ApiResponse(responseCode = "500", description = "Erro no seervio")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Long deleteProduct(@PathVariable Long id) {
        return condutorService.deletePessoa(id);
    }

}
