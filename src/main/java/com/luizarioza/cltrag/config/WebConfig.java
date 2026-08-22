package com.luizarioza.cltrag.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS = "Cross-Origin Resource Sharing". Por padrão, o navegador bloqueia
 * uma página JavaScript de fazer requisições pra um endereço (origem)
 * diferente do dela mesma - é uma proteção de segurança.
 *
 * O nosso frontend React vai rodar em http://localhost:5173 (endereço padrão
 * do Vite) e precisa chamar a API que roda em http://localhost:8080 - são
 * origens diferentes (porta diferente já conta como origem diferente!), então
 * sem essa configuração o navegador bloquearia toda chamada do React pra API,
 * mesmo rodando na mesma máquina.
 *
 * Essa classe diz ao Spring: "confie em requisições vindas de
 * localhost:5173, e libere pra elas os métodos GET e POST em qualquer rota
 * da API".
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST");
    }

}
