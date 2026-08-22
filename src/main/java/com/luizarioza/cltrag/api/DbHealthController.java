package com.luizarioza.cltrag.api;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Endpoint pra confirmar duas coisas de uma vez: que a aplicação consegue se
 * conectar no Postgres, e que a extensão pgvector está ativa nele.
 *
 * O JdbcTemplate é injetado automaticamente pelo Spring aqui no construtor -
 * isso se chama "injeção de dependência via construtor" e é a forma recomendada
 * de fazer isso no Spring (em vez de usar @Autowired direto no campo). O Spring
 * já vem com esse JdbcTemplate configurado sozinho, usando os dados que colocamos
 * em application.yml (spring.datasource), assim que adicionamos a dependência
 * spring-boot-starter-jdbc no pom.xml.
 */
@RestController
public class DbHealthController {

    private final JdbcTemplate jdbcTemplate;

    public DbHealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/db-health")
    public Map<String, Object> dbHealth() {
        // Essa query pergunta pro Postgres: "a extensão chamada 'vector' está instalada,
        // e se estiver, qual a versão dela?" - é uma tabela de sistema do próprio Postgres,
        // não fomos nós que criamos.
        String versaoExtensao = jdbcTemplate.queryForObject(
                "SELECT extversion FROM pg_extension WHERE extname = 'vector'",
                String.class
        );

        return Map.of(
                "conexaoComBanco", "ok",
                "extensaoPgvector", versaoExtensao
        );
    }

}
