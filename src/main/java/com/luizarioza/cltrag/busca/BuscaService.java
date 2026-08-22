package com.luizarioza.cltrag.busca;

import com.luizarioza.cltrag.embedding.OllamaEmbeddingService;
import com.luizarioza.cltrag.embedding.VetorPostgresUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementa a parte "R" do RAG (Retrieval - recuperação): dada uma pergunta em
 * texto livre, gera o embedding dela e busca no Postgres os artigos cujo embedding
 * está mais "perto" (mais parecido em significado).
 */
@Service
public class BuscaService {

    // O operador "<=>" do pgvector calcula a distância de cosseno entre dois
    // vetores - é a forma padrão de medir "quão parecido" dois textos são quando
    // os embeddings são normalizados (o Ollama já entrega os vetores normalizados,
    // conforme a documentação da API). Quanto MENOR o valor, mais parecido.
    private static final String SQL_BUSCA_POR_SIMILARIDADE = """
            SELECT numero_artigo, texto, embedding <=> ?::vector AS distancia
            FROM artigo_clt
            ORDER BY distancia ASC
            LIMIT ?
            """;

    private final JdbcTemplate jdbcTemplate;
    private final OllamaEmbeddingService embeddingService;

    public BuscaService(JdbcTemplate jdbcTemplate, OllamaEmbeddingService embeddingService) {
        this.jdbcTemplate = jdbcTemplate;
        this.embeddingService = embeddingService;
    }

    /**
     * Busca os "topK" artigos mais parecidos com a pergunta.
     */
    public List<ResultadoBusca> buscar(String pergunta, int topK) {
        double[] embeddingDaPergunta = embeddingService.gerarEmbedding(pergunta);
        String vetorFormatado = VetorPostgresUtils.formatarParaPostgres(embeddingDaPergunta);

        return jdbcTemplate.query(
                SQL_BUSCA_POR_SIMILARIDADE,
                (linha, numeroDaLinha) -> new ResultadoBusca(
                        linha.getString("numero_artigo"),
                        linha.getString("texto"),
                        linha.getDouble("distancia")
                ),
                vetorFormatado, topK
        );
    }

}
