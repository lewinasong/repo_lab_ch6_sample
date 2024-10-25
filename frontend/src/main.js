// main.js
import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router'; // 라우터가 있을 경우
import axios from 'axios'; // axios 추가

// axios 기본 설정
axios.defaults.baseURL = process.env.VUE_APP_API_BASE_URL; // .env 파일의 기본 URL 사용
axios.defaults.headers.common['Content-Type'] = 'application/json';

const app = createApp(App);

// Pinia 생성
const pinia = createPinia();

// Pinia를 앱에 추가
app.use(pinia);
app.use(router); // 라우터가 있을 경우

// axios를 전역에서 사용 가능하도록 설정
app.config.globalProperties.$axios = axios;

app.mount('#app');
