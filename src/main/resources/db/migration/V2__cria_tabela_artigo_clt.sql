-- Tabela que vai guardar cada artigo da CLT junto com o seu embedding (vetor de
-- 768 números, dimensão do modelo nomic-embed-text que estamos usando no Ollama -
-- se um dia trocarmos de modelo de embedding, esse número precisa mudar junto,
-- senão o Postgres recusa a inserção).

CREATE TABLE artigo_clt (
    id SERIAL PRIMARY KEY,
    numero_artigo VARCHAR(20) NOT NULL,
    texto TEXT NOT NULL,
    embedding VECTOR(768) NOT NULL
);
