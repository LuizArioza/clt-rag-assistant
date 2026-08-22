package com.luizarioza.cltrag.assistente;

import java.util.List;

/**
 * O que a API devolve pro usuário final: a resposta gerada em texto e a lista
 * dos artigos que foram usados como fonte - é essa lista de fontes que separa
 * um RAG "de verdade" de um chatbot genérico que só inventa respostas.
 */
public record RespostaAssistente(String resposta, List<String> fontes) {
}
