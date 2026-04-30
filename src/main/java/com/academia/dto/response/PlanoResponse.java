package com.academia.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanoResponse {

    private Long id;
    private String nome;
    private BigDecimal mensalidade;
    private Integer duracaoMeses;
    private String descricao;
    private Integer totalAlunos;
}
