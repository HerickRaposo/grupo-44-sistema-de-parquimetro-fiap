package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.condutores.repositories;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.condutores.entities.Condutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICondutorRepository extends JpaRepository<Condutor,Long> {
}
