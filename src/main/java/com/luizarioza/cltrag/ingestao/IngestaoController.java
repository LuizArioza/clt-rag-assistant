package com.luizarioza.cltrag.ingestao;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * Endpoint administrativo pra disparar a ingestão manualmente. Usamos POST (em vez
 * de GET) porque essa operação muda o estado do banco (apaga e reinsere dados) -
 * é uma convenção do REST: GET é só leitura, POST é pra quando algo é alterado.
 * Por isso não dá pra testar esse aqui só abrindo no navegador; precisa do curl
 * (ou de uma ferramenta tipo Postman/Insomnia).
 */
@RestController
@RequestMapping("/ingestao")
public class IngestaoController {

    private final IngestaoService ingestaoService;

    public IngestaoController(IngestaoService ingestaoService) {
        this.ingestaoService = ingestaoService;
    }

    @PostMapping("/executar")
    public Map<String, Object> executar() throws IOException {
        int quantidadeInserida = ingestaoService.executar();
        return Map.of(
                "status", "ok",
                "artigosInseridos", quantidadeInserida
        );
    }

}
