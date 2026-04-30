package com.academia.exception;

/**
 * Exceção lançada quando um recurso não é encontrado no banco de dados.
 * Ex: aluno não encontrado, plano não encontrado.
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public RecursoNaoEncontradoException(String recurso, Long id) {
        super(String.format("%s com id %d não encontrado.", recurso, id));
    }
}
