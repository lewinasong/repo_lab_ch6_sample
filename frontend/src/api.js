import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080', // 백엔드 서버의 기본 URL을 설정하세요.
  headers: {
    'Content-Type': 'application/json',
  },
});

export default apiClient;
