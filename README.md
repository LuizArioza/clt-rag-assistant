# clt-rag-assistant

Assistente que responde perguntas sobre a CLT (Consolidação das Leis do Trabalho) usando o
padrão RAG (Retrieval-Augmented Generation), citando o artigo exato usado em cada resposta.

Projeto de portfólio construído passo a passo para aprender Spring Boot na prática.

## Status atual: Etapa 1 - Fundação

Por enquanto este é só o esqueleto: uma aplicação Spring Boot rodando, com um endpoint
`/health` pra provar que tudo está funcionando. As próximas etapas vão adicionar o banco
de dados com busca vetorial, a ingestão do texto da CLT e a geração de respostas com IA.

## Como rodar

Pré-requisitos: Java 21 e Maven instalados (`java -version` e `mvn -version` devem funcionar).

```bash
mvn spring-boot:run
```

A aplicação sobe em `http://localhost:8080`. Com ela rodando, em outro terminal (ou no navegador):

```bash
curl http://localhost:8080/health
```

Resposta esperada:

```json
{"status":"ok","servico":"clt-rag-assistant"}
```
