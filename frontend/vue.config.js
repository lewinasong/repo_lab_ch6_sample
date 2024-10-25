const { defineConfig } = require('@vue/cli-service');
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8088, // 개발 서버 포트를 8082로 고정
  }
});
