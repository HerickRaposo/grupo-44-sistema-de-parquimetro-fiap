package br.com.fiap.grupo44.sistema.parquimetro.entrega.exceptions;

public class ControllerNotFoundException extends RuntimeException{

    public ControllerNotFoundException(String message){
        super(message);
    }


}
