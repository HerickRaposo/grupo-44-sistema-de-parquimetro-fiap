package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.formaPagamento.entities.FormaPagamento;

public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long>{

}
