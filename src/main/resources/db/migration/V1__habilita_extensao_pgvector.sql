-- Arquivos dentro de db/migration são "migrations" do Flyway: cada um representa
-- uma mudança no banco, é executado uma única vez (o Flyway guarda o histórico
-- numa tabela própria) e o nome segue o padrão V<numero>__descricao.sql.
-- Este é o primeiro (V1) e faz a coisa mais fundamental do projeto todo:
-- habilita a extensão "vector" no Postgres, que nos dá o tipo de dado VECTOR
-- e os operadores de busca por similaridade que vamos usar a partir da Etapa 2
-- pra guardar e comparar embeddings de texto da CLT.

CREATE EXTENSION IF NOT EXISTS vector;
