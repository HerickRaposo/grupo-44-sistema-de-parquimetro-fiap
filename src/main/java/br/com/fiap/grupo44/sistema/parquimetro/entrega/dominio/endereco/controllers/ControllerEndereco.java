package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.controllers;


import br.com.fiap.grupo44.sistema.parquimetro.entrega.adpters.dto.CepDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.dto.EnderecoDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.dto.RestDataReturnDTO;
import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.services.EnderecoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "API ENDEREÇO")
@RequestMapping(value = "/enderecos", produces = "application/json")
public class ControllerEndereco {
	
	@Autowired
	private EnderecoService enderecoService;
	 
	@PostMapping("/salvar")
	public ResponseEntity<EnderecoDTO> salvar(@Valid @RequestBody CepDTO cepDTO) {
		EnderecoDTO enderecoDTO = this.enderecoService.salvar(cepDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(enderecoDTO); 
	}
	
	@PutMapping("/atualizar/{id}")
	public ResponseEntity<EnderecoDTO> atualizar(@RequestBody EnderecoDTO enderecoDTO,@PathVariable Long id) {
		enderecoDTO = this.enderecoService.atualizar(enderecoDTO,id);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(enderecoDTO); 
	}
	
	@DeleteMapping("/apagar/{id}")
	public String apagar(@PathVariable Long id) {
		 return this.enderecoService.apagar(id);
	}
	
	
	@GetMapping("/buscar-todos")
	public RestDataReturnDTO getAll(@RequestParam(value = "pagina", defaultValue = "0") Integer pagina, @RequestParam(value = "tamanho", defaultValue = "10") Integer tamanho) {
		PageRequest pageRequest = PageRequest.of(pagina,tamanho);
		return this.enderecoService.findAll(pageRequest);
	}
	
	@GetMapping("/buscar-getID")
	public ResponseEntity<EnderecoDTO> getID(@RequestParam Long id) {
		 EnderecoDTO enderecoDTO = this.enderecoService.findById(id);
		return ResponseEntity.status(HttpStatus.FOUND).body(enderecoDTO); 
	}
}
