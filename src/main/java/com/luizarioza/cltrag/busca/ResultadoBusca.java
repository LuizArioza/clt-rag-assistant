package com.luizarioza.cltrag.busca;

/**
 * Um artigo encontrado na busca por similaridade, junto com a "distância" dele
 * em relação à pergunta - quanto menor a distância, mais parecido o artigo é
 * com o que foi perguntado (0 seria uma correspondência perfeita).
 */
public record ResultadoBusca(String numeroArtigo, String texto, double distancia) {
}
