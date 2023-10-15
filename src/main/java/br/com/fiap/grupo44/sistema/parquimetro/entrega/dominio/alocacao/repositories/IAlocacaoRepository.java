package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.repositories;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.alocacao.entities.Alocacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IAlocacaoRepository extends JpaRepository<Alocacao, Long>, JpaSpecificationExecutor<Alocacao> {
}
