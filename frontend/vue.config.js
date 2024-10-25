const { defineConfig } = require('@vue/cli-service');
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8088, // 개발 서버 포트를 8088로 고정
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 백엔드 서버 주소
        changeOrigin: true,
        pathRewrite: { '^/api': '/api' }, // '/api'로 시작하는 요청을 프록시
      }
    }
  }
});
