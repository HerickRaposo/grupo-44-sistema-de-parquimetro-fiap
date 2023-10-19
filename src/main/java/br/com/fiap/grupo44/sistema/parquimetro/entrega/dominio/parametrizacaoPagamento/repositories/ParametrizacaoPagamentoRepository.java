package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.parametrizacaoPagamento.entities.ParametrizacaoPagamento;

public interface ParametrizacaoPagamentoRepository extends JpaRepository<ParametrizacaoPagamento, Long>{

}
