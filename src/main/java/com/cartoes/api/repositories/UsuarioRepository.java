package com.cartoes.api.repositories;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.cartoes.api.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	@Transactional(readOnly = true)
	Usuario findByCpfAndAtivo(String cpf, boolean ativo);
	
	@Transactional(readOnly = true)
	Optional<Usuario> findByCpf(String cpf);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Usuario SET senha = :novasenha WHERE id = :idusuario")
	void alterarSenhaUsuario(@Param("novasenha") String novasenha, @Param("idusuario") int idusuario);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Usuario SET ultimo_Acesso = now() WHERE cpf = :cpf")
	void atualizarUltimoAcessoUsuario(@Param("cpf") String cpf);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Usuario SET ativo = 0 WHERE datediff(now(), ultimo_Acesso) > 30")
	void desativarUsuarios();
	
}