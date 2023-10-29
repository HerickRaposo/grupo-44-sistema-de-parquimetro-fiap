package br.com.fiap.grupo44.sistema.parquimetro.entrega.adapter.out.email.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailObj {
    private String destinatario;
    private String assunto;
    private String mensagem;
}
