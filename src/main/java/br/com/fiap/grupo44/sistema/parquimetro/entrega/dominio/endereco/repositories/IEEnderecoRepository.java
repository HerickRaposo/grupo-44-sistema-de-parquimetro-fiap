package br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.repositories;

import br.com.fiap.grupo44.sistema.parquimetro.entrega.dominio.endereco.entities.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface IEEnderecoRepository extends JpaRepository<Endereco,Long> {
	@Query(value = "SELECT * FROM TB_ENDERECO E WHERE E.CEP=?1",nativeQuery = true)
	public Endereco BUSCAR_ENDERECO_POR_CEP(String cep); 
	@Transactional
    @Modifying
	@Query(value = "INSERT INTO TB_ENDERECO_CONDUTOR(ENDERECO_ID,CONDUTOR_ID) VALUES(?1,?2)",nativeQuery = true)
	int SALVAR_ENDERECO(Long endereco,Long condutor);
}
