package com.luizarioza.cltrag.erro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import java.util.Map;

/**
 * Até agora, qualquer erro (pergunta vazia, Ollama fora do ar, etc.) virava um
 * HTTP 500 cru com stack trace exposto - péssima experiência pra quem consome
 * a API. @RestControllerAdvice diz pro Spring: "essa classe trata exceções que
 * acontecerem em QUALQUER controller da aplicação, num lugar só" - em vez de
 * espalhar try/catch em cada método de cada controller, centralizamos aqui.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Erro de validação nosso (ex: pergunta vazia) - o usuário errou algo,
    // então devolvemos 400 Bad Request com uma mensagem clara.
    @ExceptionHandler(PerguntaInvalidaException.class)
    public ResponseEntity<Map<String, String>> tratarPerguntaInvalida(PerguntaInvalidaException excecao) {
        return ResponseEntity.badRequest().body(Map.of("erro", excecao.getMessage()));
    }

    // Acontece quando a aplicação não consegue conectar no Ollama (container
    // parado, por exemplo). Isso não é culpa de quem chamou a API, então
    // devolvemos 503 Service Unavailable, que é o código HTTP certo pra
    // "o serviço está temporariamente indisponível".
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, String>> tratarFalhaDeConexao(ResourceAccessException excecao) {
        logger.error("Falha ao conectar com um serviço externo (provavelmente o Ollama)", excecao);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of(
                "erro", "Não foi possível conectar ao Ollama. Verifique se o container está rodando (docker ps)."
        ));
    }

    // Rede de segurança final: qualquer outro erro que não previmos. Logamos o
    // detalhe completo no servidor (pra quem está desenvolvendo poder investigar),
    // mas devolvemos uma mensagem genérica pro cliente da API - nunca expomos
    // stack trace interna pra fora, isso pode vazar detalhe de implementação.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> tratarErroInesperado(Exception excecao) {
        logger.error("Erro inesperado ao processar a requisição", excecao);
        return ResponseEntity.internalServerError().body(Map.of(
                "erro", "Ocorreu um erro inesperado. Consulte os logs da aplicação para mais detalhes."
        ));
    }

}
