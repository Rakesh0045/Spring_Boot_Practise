import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, '.', '')

  const DEV_BACKEND = env.VITE_DEV_BACKEND_URL ?? 'http://localhost:2801'


  return {
    plugins: [react()],
    server: {
      proxy: {
        '/api': {
          target: DEV_BACKEND,
          changeOrigin: true,
        }
      }
    },
    define: {
      'process.env': {}
    }
  }
})

