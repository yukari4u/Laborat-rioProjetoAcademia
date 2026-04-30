package com.academia.repository;

import com.academia.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    /**
     * Busca aluno pelo e-mail.
     */
    Optional<Aluno> findByEmail(String email);

    /**
     * Verifica se já existe um aluno com o e-mail informado.
     */
    boolean existsByEmail(String email);

    /**
     * Verifica se já existe um aluno com o CPF informado.
     */
    boolean existsByCpf(String cpf);

    /**
     * Lista todos os alunos de um plano específico.
     */
    List<Aluno> findByPlanoId(Long planoId);
}
