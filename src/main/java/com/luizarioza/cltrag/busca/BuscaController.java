package com.luizarioza.cltrag.busca;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Endpoint de busca. É GET (diferente do /ingestao/executar, que é POST) porque
 * essa operação só lê dados, não altera nada no banco - por isso dá pra testar
 * direto no navegador, colando a pergunta na URL.
 */
@RestController
public class BuscaController {

    private final BuscaService buscaService;

    public BuscaController(BuscaService buscaService) {
        this.buscaService = buscaService;
    }

    @GetMapping("/perguntas")
    public Map<String, Object> perguntar(@RequestParam String pergunta) {
        List<ResultadoBusca> resultados = buscaService.buscar(pergunta, 3);

        return Map.of(
                "pergunta", pergunta,
                "resultados", resultados
        );
    }

}
