package com.luizarioza.cltrag.ingestao;

/**
 * Representa um artigo da CLT tal como está no arquivo data/clt-artigos.json.
 * Um "record" em Java é uma forma enxuta de declarar uma classe que só serve
 * pra carregar dados - o compilador gera sozinho o construtor, os getters
 * (aqui chamados de numero() e texto(), sem "get" na frente) e outros métodos
 * padrão. O Jackson (biblioteca de JSON que já vem no Spring) sabe converter
 * JSON pra um record automaticamente, casando os nomes dos campos.
 */
public record ArtigoClt(String numero, String texto) {
}
