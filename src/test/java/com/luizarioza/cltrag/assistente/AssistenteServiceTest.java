package com.luizarioza.cltrag.assistente;

import com.luizarioza.cltrag.busca.BuscaService;
import com.luizarioza.cltrag.busca.ResultadoBusca;
import com.luizarioza.cltrag.erro.PerguntaInvalidaException;
import com.luizarioza.cltrag.geracao.OllamaGeracaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Este teste NÃO chama o Ollama nem o Postgres de verdade - "simulamos"
 * (mockamos) o BuscaService e o OllamaGeracaoService, controlando exatamente
 * o que cada um devolve. Isso deixa o teste rápido (roda em milissegundos,
 * não em segundos esperando uma IA responder) e determinístico (sempre dá
 * o mesmo resultado, não depende do que o LLM "decidiu" responder daquela vez).
 *
 * @ExtendWith(MockitoExtension.class) liga o Mockito nesse teste - é o que faz
 * as anotações @Mock funcionarem, criando objetos "fake" no lugar das
 * dependências reais.
 */
@ExtendWith(MockitoExtension.class)
class AssistenteServiceTest {

    @Mock
    private BuscaService buscaService;

    @Mock
    private OllamaGeracaoService geracaoService;

    @Test
    void devePerguntarEDevolverRespostaComFontesDosArtigosEncontrados() {
        // "Arrange": prepara o cenário - define o que os mocks devem devolver
        // quando forem chamados.
        List<ResultadoBusca> resultadosSimulados = List.of(
                new ResultadoBusca("Art. 59", "texto do artigo 59 sobre horas extras", 0.10),
                new ResultadoBusca("Art. 71", "texto do artigo 71 sobre intervalo", 0.25)
        );
        when(buscaService.buscar(anyString(), anyInt())).thenReturn(resultadosSimulados);
        when(geracaoService.gerarResposta(anyString()))
                .thenReturn("Você pode fazer até 2 horas extras por dia (Art. 59).");

        AssistenteService assistenteService = new AssistenteService(buscaService, geracaoService);

        // "Act": executa o que estamos testando de verdade.
        RespostaAssistente resposta = assistenteService.perguntar("quantas horas extras posso fazer?");

        // "Assert": confirma que o resultado é o esperado.
        assertEquals("Você pode fazer até 2 horas extras por dia (Art. 59).", resposta.resposta());
        assertEquals(List.of("Art. 59", "Art. 71"), resposta.fontes());

        // Confirma também que o prompt mandado pro LLM realmente incluiu o texto
        // do artigo recuperado - ou seja, que o "Augmented" do RAG está acontecendo.
        verify(geracaoService).gerarResposta(contains("texto do artigo 59 sobre horas extras"));
    }

    @Test
    void deveRecusarPerguntaVazia() {
        AssistenteService assistenteService = new AssistenteService(buscaService, geracaoService);

        assertThrows(PerguntaInvalidaException.class, () -> assistenteService.perguntar("   "));
    }

}
