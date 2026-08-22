package com.luizarioza.cltrag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Esta é a "porta de entrada" de qualquer aplicação Spring Boot.
 *
 * A anotação @SpringBootApplication na verdade é um atalho que junta três outras:
 *   - @Configuration: diz que esta classe pode definir configurações do Spring
 *   - @EnableAutoConfiguration: diz pro Spring "configure automaticamente tudo que
 *     você achar necessário com base nas dependências do pom.xml" (é essa anotação
 *     que faz o Tomcat embutido subir sozinho, por exemplo, só porque adicionamos
 *     o spring-boot-starter-web)
 *   - @ComponentScan: diz pro Spring "procure, dentro deste pacote e dos pacotes
 *     filhos, todas as classes marcadas com @Controller, @Service, @Repository etc.
 *     e gerencie elas pra mim" (isso é o que chamamos de "injeção de dependência" -
 *     você não cria os objetos na mão, o Spring cria e entrega prontos onde precisar)
 *
 * O método main() é o ponto de partida padrão de qualquer aplicação Java - é o que
 * roda quando você executa "mvn spring-boot:run" ou o .jar gerado.
 */
@SpringBootApplication
public class CltRagAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(CltRagAssistantApplication.class, args);
    }

}
