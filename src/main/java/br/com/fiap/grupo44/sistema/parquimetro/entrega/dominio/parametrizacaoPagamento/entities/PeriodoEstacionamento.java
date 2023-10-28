package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.entities;

public enum PeriodoEstacionamento {
	FIXO(1l,"Fixo"),
	POR_HORA(2L, "Por hora");
	private final Long codigo;
	private final String nome;

	PeriodoEstacionamento(Long codigo, String nome) {
		this.codigo = codigo;
		this.nome = nome;
	}

	public Long getCodigo() {
		return codigo;
	}

	public String getNome() {
		return nome;
	}
}
