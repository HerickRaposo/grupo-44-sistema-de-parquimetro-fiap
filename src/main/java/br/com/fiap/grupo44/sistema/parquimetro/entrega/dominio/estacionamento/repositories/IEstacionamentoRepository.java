package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.repositories;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.estacionamento.entities.Estacionamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IEstacionamentoRepository extends JpaRepository<Estacionamento, Long>, JpaSpecificationExecutor<Estacionamento> {
}
