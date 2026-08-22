package com.luizarioza.cltrag.embedding;

/**
 * O pgvector espera que um vetor seja passado como texto no formato "[0.1,0.2,0.3]"
 * quando usamos JDBC puro (sem uma biblioteca cliente específica). Essa classe
 * centraliza essa conversão num só lugar, porque tanto a ingestão (Etapa 2) quanto
 * a busca (Etapa 3) precisam fazer exatamente a mesma coisa - em vez de copiar e
 * colar o mesmo código nos dois lugares, escrevemos uma vez e reaproveitamos
 * (esse princípio se chama "DRY" - Don't Repeat Yourself).
 */
public final class VetorPostgresUtils {

    // Construtor privado: essa classe só tem métodos estáticos, não faz sentido
    // ninguém criar uma instância dela (ex: "new VetorPostgresUtils()").
    private VetorPostgresUtils() {
    }

    public static String formatarParaPostgres(double[] embedding) {
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
