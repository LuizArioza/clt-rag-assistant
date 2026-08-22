package com.luizarioza.cltrag.assistente;

import com.luizarioza.cltrag.busca.BuscaService;
import com.luizarioza.cltrag.busca.ResultadoBusca;
import com.luizarioza.cltrag.erro.PerguntaInvalidaException;
import com.luizarioza.cltrag.geracao.OllamaGeracaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Junta as duas metades do RAG que já construímos: busca (Etapa 3) + geração
 * (esta etapa). É aqui que a "mágica" acontece de verdade - o resto do projeto
 * até agora foi construir as peças, esta classe é quem orquestra elas juntas.
 */
@Service
public class AssistenteService {

    private static final Logger logger = LoggerFactory.getLogger(AssistenteService.class);

    private static final int QUANTIDADE_DE_ARTIGOS_NO_CONTEXTO = 3;

    private final BuscaService buscaService;
    private final OllamaGeracaoService geracaoService;

    public AssistenteService(BuscaService buscaService, OllamaGeracaoService geracaoService) {
        this.buscaService = buscaService;
        this.geracaoService = geracaoService;
    }

    public RespostaAssistente perguntar(String pergunta) {
        // Validação simples: sem isso, uma pergunta vazia ainda geraria um
        // embedding e chamaria o LLM à toa, gastando tempo/recursos pra nada.
        // O GlobalExceptionHandler intercepta essa exceção e devolve um 400
        // com mensagem clara, em vez de deixar o erro estourar mais na frente.
        if (pergunta == null || pergunta.isBlank()) {
            throw new PerguntaInvalidaException("A pergunta não pode estar vazia.");
        }

        logger.info("Pergunta recebida: {}", pergunta);

        // 1. Recupera os artigos mais relevantes pra pergunta (a parte "Retrieval").
        List<ResultadoBusca> artigosRelevantes = buscaService.buscar(pergunta, QUANTIDADE_DE_ARTIGOS_NO_CONTEXTO);

        // 2. Monta o prompt final, injetando esses artigos como contexto (a parte
        // "Augmented" do nome RAG - a pergunta chega "aumentada" com informação real).
        String contexto = montarContexto(artigosRelevantes);
        String prompt = montarPrompt(contexto, pergunta);

        // 3. Manda pro modelo de linguagem gerar a resposta em texto (a parte "Generation").
        String respostaGerada = geracaoService.gerarResposta(prompt);

        List<String> fontes = artigosRelevantes.stream()
                .map(ResultadoBusca::numeroArtigo)
                .toList();

        logger.info("Resposta gerada com {} fonte(s): {}", fontes.size(), fontes);

        return new RespostaAssistente(respostaGerada.strip(), fontes);
    }

    private String montarContexto(List<ResultadoBusca> artigos) {
        StringBuilder construtor = new StringBuilder();
        for (ResultadoBusca artigo : artigos) {
            construtor.append("[").append(artigo.numeroArtigo()).append("] ")
                    .append(artigo.texto())
                    .append("\n\n");
        }
        return construtor.toString();
    }

    private String montarPrompt(String contexto, String pergunta) {
        // Um "text block" do Java (as três aspas) permite escrever texto de várias
        // linhas sem precisar concatenar strings com "+" - fica muito mais legível
        // pra prompts longos como este.
        return """
                Você é um assistente que responde perguntas sobre a CLT (Consolidação das Leis do Trabalho) com base EXCLUSIVAMENTE no contexto fornecido abaixo. Não use nenhum conhecimento que não esteja no contexto.

                Se o contexto não tiver informação suficiente para responder, diga claramente que não encontrou a resposta nos artigos disponíveis - não invente uma resposta.

                Sempre que usar uma informação do contexto, cite o número do artigo entre parênteses, por exemplo: (Art. 59).

                Responda em português, de forma direta e objetiva.

                Contexto:
                %s

                Pergunta: %s

                Resposta:
                """.formatted(contexto, pergunta);
    }

}
