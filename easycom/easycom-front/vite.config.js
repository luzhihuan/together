import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve:{
    alias:{
      '@':fileURLToPath(new URL('./src',import.meta.url))
    },
  },
  server:{
    port:6060,
    hmr:true,
    proxy:{
      '/api':{
        target:"http://127.0.0.1:6061/",
        changeOrigin:true,
        pathRewrite:{
          "^/api":"/api"
        }
      }
    }
  }

})

