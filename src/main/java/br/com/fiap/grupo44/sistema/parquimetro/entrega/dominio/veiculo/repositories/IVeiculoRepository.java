package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.repositories;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.veiculo.entities.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IVeiculoRepository  extends JpaRepository<Veiculo, Long>, JpaSpecificationExecutor<Veiculo> {
}
