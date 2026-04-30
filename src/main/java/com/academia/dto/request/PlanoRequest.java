package com.academia.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanoRequest {

    @NotBlank(message = "O nome do plano é obrigatório.")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    private String nome;

    @NotNull(message = "A mensalidade é obrigatória.")
    @Positive(message = "A mensalidade deve ser um valor positivo.")
    private BigDecimal mensalidade;

    @NotNull(message = "A duração em meses é obrigatória.")
    @Min(value = 1, message = "A duração mínima é de 1 mês.")
    private Integer duracaoMeses;

    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
    private String descricao;
}
