package com.luizarioza.cltrag.ingestao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luizarioza.cltrag.embedding.OllamaEmbeddingService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Orquestra a etapa de ingestão: lê os artigos da CLT do arquivo JSON, gera o
 * embedding de cada um (via Ollama) e salva tudo no Postgres.
 *
 * Isso é o pipeline de "carregar dados no formato que a busca por similaridade
 * (Etapa 3) vai conseguir consultar depois.
 */
@Service
public class IngestaoService {

    private final JdbcTemplate jdbcTemplate;
    private final OllamaEmbeddingService embeddingService;
    private final ObjectMapper objectMapper;

    public IngestaoService(JdbcTemplate jdbcTemplate,
                            OllamaEmbeddingService embeddingService,
                            ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.embeddingService = embeddingService;
        this.objectMapper = objectMapper;
    }

    /**
     * Executa a ingestão completa. Devolve quantos artigos foram inseridos.
     */
    public int executar() throws IOException {
        List<ArtigoClt> artigos = carregarArtigosDoJson();

        // Limpa a tabela antes de reinserir - assim podemos rodar esse endpoint
        // várias vezes (por exemplo, depois de adicionar mais artigos no JSON)
        // sem acabar com artigos duplicados no banco.
        jdbcTemplate.update("DELETE FROM artigo_clt");

        for (ArtigoClt artigo : artigos) {
            double[] embedding = embeddingService.gerarEmbedding(artigo.texto());
            String vetorFormatadoParaPostgres = formatarVetor(embedding);

            // O "?::vector" diz pro Postgres: "receba esse parâmetro como texto e
            // converta pro tipo vector" - é assim que passamos um vetor de 768
            // números através do JDBC sem precisar de uma biblioteca extra.
            jdbcTemplate.update(
                    "INSERT INTO artigo_clt (numero_artigo, texto, embedding) VALUES (?, ?, ?::vector)",
                    artigo.numero(), artigo.texto(), vetorFormatadoParaPostgres
            );
        }

        return artigos.size();
    }

    private List<ArtigoClt> carregarArtigosDoJson() throws IOException {
        ClassPathResource resource = new ClassPathResource("data/clt-artigos.json");
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<List<ArtigoClt>>() {
            });
        }
    }

    /**
     * O pgvector espera o vetor escrito como texto no formato "[0.1,0.2,0.3]".
     * Essa função só monta essa string a partir do array de doubles.
     */
    private String formatarVetor(double[] embedding) {
        StringBuilder construtor = new StringBuilder("[");
        for (int i = 0; i < embedding.length; i++) {
            if (i > 0) {
                construtor.append(",");
            }
            construtor.append(embedding[i]);
        }
        construtor.append("]");
        return construtor.toString();
    }

}
