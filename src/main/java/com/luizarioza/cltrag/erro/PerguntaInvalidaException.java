package com.luizarioza.cltrag.erro;

/**
 * Lançada quando a pergunta enviada pelo usuário não é válida (por exemplo,
 * vazia). Estender RuntimeException (em vez de Exception) significa que essa
 * exceção não precisa ser declarada com "throws" em todo método que a lança -
 * é o padrão mais comum pra erros de validação em aplicações Spring.
 */
public class PerguntaInvalidaException extends RuntimeException {

    public PerguntaInvalidaException(String mensagem) {
        super(mensagem);
    }

}
