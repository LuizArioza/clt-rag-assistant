package com.luizarioza.cltrag.assistente;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * O endpoint principal do projeto - o motivo de tudo que construímos até aqui.
 * GET porque, do ponto de vista do cliente da API, perguntar algo não altera
 * nenhum estado (o fato de gerar uma resposta com IA por trás é um detalhe de
 * implementação).
 */
@RestController
public class AssistenteController {

    private final AssistenteService assistenteService;

    public AssistenteController(AssistenteService assistenteService) {
        this.assistenteService = assistenteService;
    }

    @GetMapping("/assistente")
    public RespostaAssistente perguntar(@RequestParam String pergunta) {
        return assistenteService.perguntar(pergunta);
    }

}
