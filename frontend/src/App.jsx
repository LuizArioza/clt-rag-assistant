import { useState } from 'react'
import './App.css'

// Endereço do backend Spring Boot. Em um projeto maior isso viria de uma
// variável de ambiente (.env), mas pra manter simples deixamos fixo aqui -
// é só o endereço onde "mvn spring-boot:run" sobe a aplicação.
const API_URL = 'http://localhost:8080'

function App() {
  // useState é o jeito do React de guardar "memória" que, quando muda, faz
  // a tela ser redesenhada automaticamente. Cada linha abaixo cria uma
  // variável (o valor atual) e uma função pra atualizá-la (por convenção,
  // começando com "set").
  const [pergunta, setPergunta] = useState('')
  const [resposta, setResposta] = useState(null)
  const [fontes, setFontes] = useState([])
  const [carregando, setCarregando] = useState(false)
  const [erro, setErro] = useState(null)

  // Função que chama a API. É "async" porque esperar a resposta do backend
  // (que por sua vez espera o Ollama gerar o texto) pode levar alguns
  // segundos - não queremos travar a página enquanto isso acontece.
  async function perguntar() {
    const perguntaLimpa = pergunta.trim()
    if (!perguntaLimpa) {
      return
    }

    setCarregando(true)
    setErro(null)
    setResposta(null)
    setFontes([])

    try {
      const resp = await fetch(
        `${API_URL}/assistente?pergunta=${encodeURIComponent(perguntaLimpa)}`,
      )

      if (!resp.ok) {
        // O backend devolve códigos diferentes pra cada tipo de problema
        // (veja GlobalExceptionHandler.java): 400 = pergunta inválida,
        // 503 = Ollama fora do ar, 500 = erro inesperado.
        if (resp.status === 503) {
          throw new Error(
            'O Ollama parece estar fora do ar. Confirme que "docker compose up -d" está rodando.',
          )
        }
        throw new Error(
          'Não foi possível obter uma resposta. Confirme que o backend (mvn spring-boot:run) está rodando.',
        )
      }

      const dados = await resp.json()
      setResposta(dados.resposta)
      setFontes(dados.fontes || [])
    } catch (e) {
      // Se o fetch nem chegou a conversar com o backend (ex: backend
      // desligado), o erro cai aqui também, com uma mensagem de rede.
      setErro(e.message || 'Erro ao conectar com o backend.')
    } finally {
      setCarregando(false)
    }
  }

  // Permite apertar Enter no campo de texto em vez de precisar clicar no
  // botão toda vez.
  function aoPressionarTecla(evento) {
    if (evento.key === 'Enter') {
      perguntar()
    }
  }

  return (
    <div className="pagina">
      <div className="cartao">
        <header>
          <h1>Assistente CLT</h1>
          <p className="subtitulo">
            Pergunte algo sobre a Consolidação das Leis do Trabalho e receba
            uma resposta com os artigos exatos usados como fonte.
          </p>
        </header>

        <div className="campo-busca">
          <input
            type="text"
            placeholder="Ex: quantas horas extras posso fazer por dia?"
            value={pergunta}
            onChange={(evento) => setPergunta(evento.target.value)}
            onKeyDown={aoPressionarTecla}
            disabled={carregando}
          />
          <button onClick={perguntar} disabled={carregando}>
            {carregando ? 'Consultando...' : 'Perguntar'}
          </button>
        </div>

        {carregando && (
          <p className="status">Consultando a CLT, isso pode levar alguns segundos...</p>
        )}

        {erro && <p className="erro">{erro}</p>}

        {resposta && (
          <div className="resultado">
            <p className="resposta">{resposta}</p>

            {fontes.length > 0 && (
              <div className="fontes">
                <span className="fontes-titulo">Fontes:</span>
                {fontes.map((fonte) => (
                  <span className="fonte-badge" key={fonte}>
                    {fonte}
                  </span>
                ))}
              </div>
            )}
          </div>
        )}

        <footer>
          <p>
            Projeto de portfólio · respostas geradas por IA a partir de um
            conjunto curado de artigos · não substitui aconselhamento jurídico.
          </p>
        </footer>
      </div>
    </div>
  )
}

export default App
