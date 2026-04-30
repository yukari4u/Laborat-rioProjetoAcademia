package com.academia.exception;

/**
 * Exceção lançada quando uma regra de negócio é violada.
 * Ex: e-mail duplicado, plano já existente, etc.
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }

    public RegraNegocioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
