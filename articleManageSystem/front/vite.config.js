import { defineConfig } from 'vite';
import { fileURLToPath, URL } from 'node:url';
import vue from '@vitejs/plugin-vue';
import path from 'path';




export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  server: {
    port: 8889,  // 修改为你想要的端口号
    // proxy: {
    //   '/api': {
    //     target: 'http://localhost:8080',
    //     changeOrigin: true,
    //     rewrite: (path) => path.replace(/^\/api/, ''),
    //   },
    // },
  },
});

// export default defineConfig({
//   plugins: [vue()],
//   resolve: {
//       alias: {
//           '@': path.resolve(__dirname, './src'),
//       },
//   },
//   server: {
//     //   proxy: {
//     //       '/api': {
//     //           target: 'http://localhost:8080',
//     //           changeOrigin: true,
//     //           rewrite: (path) => path.replace(/^\/api/, ''),
//     //       },
//     //   },
//   },
// });


