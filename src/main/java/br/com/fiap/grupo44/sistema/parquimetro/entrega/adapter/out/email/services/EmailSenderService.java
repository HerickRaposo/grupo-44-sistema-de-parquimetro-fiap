package br.com.fiap.grupo44.sistema.parquimetro.entrega.adapter.out.email.services;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private JavaMailSender javaMailSender;

    public ResponseEntity<String> enviarEmail(String destinatario, String assunto, String conteudo) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(destinatario);
            message.setSubject(assunto);
            message.setText(conteudo);

            javaMailSender.send(message);

            System.out.println("Email enviado com sucesso.");
            return new ResponseEntity<>("Email enviado com sucesso.", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao envio de email: " + ex.getMessage(), HttpStatus.OK);
        }
    }
}
