package com.luizarioza.cltrag.geracao;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * Essa é a parte "G" do RAG (Generation - geração): manda um prompt pronto pro
 * modelo de linguagem (rodando no Ollama, mesmo container, mas um modelo diferente
 * do de embeddings) e recebe de volta um texto gerado em linguagem natural.
 *
 * Repara que a estrutura é quase idêntica ao OllamaEmbeddingService (Etapa 2) -
 * mesma ideia de RestClient conversando com a API do Ollama, só que aqui o
 * endpoint e o formato da resposta são outros.
 */
@Service
public class OllamaGeracaoService {

    // llama3.2 é um modelo de linguagem pequeno o suficiente pra rodar bem numa
    // máquina comum (sem GPU dedicada), mas que segue instruções razoavelmente bem -
    // adequado pra um projeto de portfólio que não pode depender de infraestrutura cara.
    private static final String MODELO = "llama3.2";

    private final RestClient restClient;

    public OllamaGeracaoService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("http://localhost:11434")
                .build();
    }

    public String gerarResposta(String prompt) {
        GenerateResponse resposta = restClient.post()
                .uri("/api/generate")
                .contentType(MediaType.APPLICATION_JSON)
                // stream=false: pedimos pro Ollama devolver a resposta inteira de uma vez,
                // em vez de mandar pedaço por pedaço (que é o padrão dele, pensado pra
                // interfaces de chat indeterminado). Fica mais simples de tratar no backend.
                .body(new GenerateRequest(MODELO, prompt, false))
                .retrieve()
                .body(GenerateResponse.class);

        if (resposta == null) {
            throw new IllegalStateException("Ollama não retornou nenhuma resposta.");
        }

        return resposta.response();
    }

    private record GenerateRequest(String model, String prompt, boolean stream) {
    }

    private record GenerateResponse(String response) {
    }

}
