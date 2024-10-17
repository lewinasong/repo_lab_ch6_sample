// main.js 또는 main.ts
import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router'; // 라우터가 있을 경우

const app = createApp(App);

// Pinia 생성
const pinia = createPinia();

// Pinia를 앱에 추가
app.use(pinia);
app.use(router); // 라우터가 있을 경우

app.mount('#app');