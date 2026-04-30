package com.academia.repository;

import com.academia.entity.Plano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanoRepository extends JpaRepository<Plano, Long> {

    /**
     * Verifica se já existe um plano com o mesmo nome.
     */
    boolean existsByNome(String nome);

    /**
     * Busca plano pelo nome (case-insensitive).
     */
    Optional<Plano> findByNomeIgnoreCase(String nome);
}
