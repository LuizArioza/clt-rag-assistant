package com.luizarioza.cltrag.embedding;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * Responsável por conversar com o Ollama (rodando no Docker, porta 11434) e pedir
 * pra ele transformar um texto num "embedding" - um vetor de números que representa
 * o significado daquele texto. Textos com significado parecido geram vetores
 * parecidos, e é essa propriedade que a busca por similaridade (Etapa 3) vai explorar.
 *
 * RestClient é o cliente HTTP moderno do Spring (desde a versão 3.2) - já vem pronto
 * quando temos o spring-boot-starter-web no classpath, não precisamos de nenhuma
 * dependência nova só pra fazer essa chamada.
 */
@Service
public class OllamaEmbeddingService {

    private static final String MODELO = "nomic-embed-text";

    private final RestClient restClient;

    public OllamaEmbeddingService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("http://localhost:11434")
                .build();
    }

    /**
     * Manda o texto pro Ollama e devolve o vetor de embedding correspondente.
     * O modelo nomic-embed-text produz vetores de 768 números.
     */
    public double[] gerarEmbedding(String texto) {
        EmbedResponse resposta = restClient.post()
                .uri("/api/embed")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new EmbedRequest(MODELO, texto))
                .retrieve()
                .body(EmbedResponse.class);

        if (resposta == null || resposta.embeddings().isEmpty()) {
            throw new IllegalStateException("Ollama não retornou nenhum embedding para o texto enviado.");
        }

        // A API do Ollama devolve uma lista de vetores (pra suportar mandar vários
        // textos de uma vez) - como mandamos só um texto, pegamos o primeiro.
        List<Double> vetor = resposta.embeddings().get(0);
        double[] resultado = new double[vetor.size()];
        for (int i = 0; i < vetor.size(); i++) {
            resultado[i] = vetor.get(i);
        }
        return resultado;
    }

    // Records usados só pra representar o formato JSON que a API do Ollama espera
    // receber e o que ela devolve - o Spring converte JSON <-> Java automaticamente.
    private record EmbedRequest(String model, String input) {
    }

    private record EmbedResponse(List<List<Double>> embeddings) {
    }

}
