package br.com.fiap.grupo44.sistema.parquimetro.entrega.adpters.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {
    private String destinatario;
    private String assunto;
    private String mensagem;
}
