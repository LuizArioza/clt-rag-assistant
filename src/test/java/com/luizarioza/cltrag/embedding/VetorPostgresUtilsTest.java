package com.luizarioza.cltrag.embedding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Este é o teste mais simples possível: VetorPostgresUtils.formatarParaPostgres
 * é uma função "pura" (não depende de banco, rede, nem nada externo - só recebe
 * um array e devolve uma String), então testar ela não precisa de nenhum "mock"
 * nem de subir a aplicação inteira. É por isso que vale a pena escrever código
 * assim sempre que possível: fica trivial de testar.
 *
 * Convenção de nome: classes de teste terminam em "...Test" e o Maven já sabe
 * rodar todas elas automaticamente quando você executa "mvn test".
 */
class VetorPostgresUtilsTest {

    @Test
    void deveFormatarVetorComTresPosicoes() {
        double[] vetor = {0.1, 0.2, 0.3};

        String resultado = VetorPostgresUtils.formatarParaPostgres(vetor);

        assertEquals("[0.1,0.2,0.3]", resultado);
    }

    @Test
    void deveFormatarVetorVazio() {
        double[] vetor = {};

        String resultado = VetorPostgresUtils.formatarParaPostgres(vetor);

        assertEquals("[]", resultado);
    }

    @Test
    void deveFormatarVetorComUmaUnicaPosicao() {
        double[] vetor = {0.5};

        String resultado = VetorPostgresUtils.formatarParaPostgres(vetor);

        assertEquals("[0.5]", resultado);
    }

}
