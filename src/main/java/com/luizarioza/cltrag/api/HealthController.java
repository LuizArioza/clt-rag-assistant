package com.luizarioza.cltrag.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Um "controller" no Spring é a classe responsável por receber requisições HTTP
 * e devolver uma resposta. Cada método marcado com @GetMapping, @PostMapping etc.
 * vira um endpoint da sua API.
 *
 * @RestController é um atalho pra duas anotações:
 *   - @Controller: registra esta classe no Spring como responsável por lidar com requisições
 *   - @ResponseBody: diz que o retorno de cada método deve ser convertido direto pra JSON
 *     na resposta HTTP (sem isso, o Spring tentaria procurar uma página HTML com esse nome)
 *
 * Este é só um endpoint de "estou vivo" - o objetivo dele aqui na Etapa 1 é único:
 * provar que o projeto sobe e responde a uma requisição, antes de qualquer lógica real.
 */
@RestController
public class HealthController {

    // @GetMapping("/health") diz: "quando chegar um GET para /health, rode este método"
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok", "servico", "clt-rag-assistant");
    }

}
