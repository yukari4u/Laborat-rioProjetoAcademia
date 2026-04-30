package com.academia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "planos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false)
    private BigDecimal mensalidade;

    @Column(nullable = false)
    private Integer duracaoMeses;

    @Column(length = 255)
    private String descricao;

    // Relacionamento: um Plano possui vários Alunos
    @OneToMany(mappedBy = "plano", cascade = CascadeType.ALL, orphanRemoval = false)
    @Builder.Default
    private List<Aluno> alunos = new ArrayList<>();
}
