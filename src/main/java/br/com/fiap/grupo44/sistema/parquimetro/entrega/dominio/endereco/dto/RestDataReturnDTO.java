package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.dto;

import com.fasterxml.jackson.annotation.JsonInclude;



public class RestDataReturnDTO {
    private Object data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Paginator paginator;
    
	public RestDataReturnDTO(Object data, Paginator paginator) {
		this.data = data;
		this.paginator = paginator;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}
    
    
}
