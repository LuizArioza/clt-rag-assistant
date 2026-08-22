import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// Configuração padrão do Vite (o "empacotador" que compila o React e serve
// a página em desenvolvimento com recarregamento automático ao salvar).
// O plugin react() é o que ensina o Vite a entender arquivos .jsx.
export default defineConfig({
  plugins: [react()],
})
