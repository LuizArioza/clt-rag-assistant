package com.luizarioza.cltrag.api;

import com.luizarioza.cltrag.embedding.OllamaEmbeddingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

/**
 * Endpoint só pra provar que a aplicação consegue conversar com o Ollama e gerar
 * um embedding de verdade - antes de montar toda a pipeline de ingestão da CLT
 * em cima disso. Mesmo princípio do /db-health: valida uma peça de cada vez.
 */
@RestController
public class EmbeddingHealthController {

    private final OllamaEmbeddingService embeddingService;

    public EmbeddingHealthController(OllamaEmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @GetMapping("/embedding-health")
    public Map<String, Object> embeddingHealth() {
        double[] vetor = embeddingService.gerarEmbedding("teste de conexão com o Ollama");

        return Map.of(
                "conexaoComOllama", "ok",
                "dimensaoDoVetor", vetor.length,
                "primeirosValores", Arrays.copyOfRange(vetor, 0, Math.min(5, vetor.length))
        );
    }

}
