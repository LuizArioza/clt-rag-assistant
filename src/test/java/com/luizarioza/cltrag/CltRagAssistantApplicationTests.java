package com.luizarioza.cltrag;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Este é o teste mais simples que existe num projeto Spring Boot: ele apenas
 * sobe todo o "contexto" da aplicação (todos os componentes, controllers etc.)
 * e verifica se nada quebra ao inicializar. Não testa nenhuma regra de negócio -
 * é só uma rede de segurança pra pegar erros de configuração cedo.
 *
 * Vamos adicionar testes de verdade (que testam comportamento) a partir da Etapa 5.
 */
@SpringBootTest
class CltRagAssistantApplicationTests {

    @Test
    void contextLoads() {
        // Vazio de propósito: se o contexto do Spring não subir, este teste falha sozinho.
    }

}
