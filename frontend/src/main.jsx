import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'

// Este é o "ponto de entrada" de toda aplicação React: pega a div #root
// que existe no index.html e manda o React renderizar o componente <App />
// dentro dela. A partir daqui, é o React quem controla o que aparece na tela.
ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
)
