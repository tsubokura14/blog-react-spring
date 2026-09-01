import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    // ブラウザからは同一オリジン(localhost:5173)へのリクエストに見えるため、
    // backend側でCORSを許可しなくてもローカル開発中はエラーにならない。
    // 本番相当のCORS設定はdocs/overview/ロードマップ.mdのPhase 7で扱う
    //
    // /api配下だけをbackendへ転送する。backendのAPIを/apiに分離しているのは、
    // frontendのSPAルート(例: /posts/new)とパスが衝突しないようにするため
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})
