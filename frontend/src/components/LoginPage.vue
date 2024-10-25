<template>
  <div class="login-container">
    <h2>로그인</h2>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="employeeNumber">행번</label>
        <input
          type="text"
          id="employeeNumber"
          v-model="employeeNumber"
          placeholder="행번을 입력해주세요(7자리)"
          required
        />
      </div>
      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
      <button type="submit">Login</button>
    </form>
  </div>
</template>

<script>
import { useUserStore } from '@/stores/userStore'; // Pinia 스토어 가져오기
import apiClient from '@/api'; // api.js에서 만든 axios 인스턴스를 가져옴

export default {
  data() {
    return {
      employeeNumber: "", // 입력된 행번을 저장
      errorMessage: "", // 에러 메시지
    };
  },
  methods: {
    async handleSubmit() {
      if (this.employeeNumber.length !== 7) {
        this.errorMessage = "행번을 확인해주세요"; // 에러 메시지
      } else {
        this.errorMessage = ""; // 에러 메시지 초기화

        try {
          // API 요청
          const response = await apiClient.get(`/api/login/${this.employeeNumber}`);

          const responseText = response.data; // 백엔드에서 받은 응답 텍스트

          // 로그인 성공 여부 판단
          if (responseText.startsWith("로그인 성공")) {
            // 직원 이름 추출 (예: "로그인 성공. 직원 이름: 홍길동")
            const employeeName = responseText.split("직원 이름: ")[1];

            // Pinia 스토어에 사용자 정보 저장
            const userStore = useUserStore();
            userStore.setUser({
              employeeNumber: this.employeeNumber,
              name: employeeName, // 추출한 직원 이름
            });

            // 로그인 후 메인 페이지로 이동
            this.$router.push({ name: 'PageHome' });
          } else {
            // 로그인 실패 시 에러 메시지 표시
            this.errorMessage = "로그인에 실패했습니다. 다시 시도해주세요.";
          }

        } catch (error) {
          // 에러 처리: 네트워크 오류 또는 기타 오류 시 에러 메시지 표시
          this.errorMessage = "로그인에 실패했습니다. 다시 시도해주세요.";
        }
      }
    },
  },
};
</script>

<style scoped>
/* 사용자 정보를 우측 상단에 고정하는 스타일 */
.user-info {
  position: fixed;
  top: 10px;
  right: 20px;
  background-color: #f4f4f4;
  padding: 10px;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  font-size: 14px;
}

@font-face {
  font-family: 'KCCMurukmuruk';
  src: url('@/fonts/KCCMurukmuruk.ttf') format('truetype');
}

body, html {
  height: 100%;
  margin: 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 40px;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  font-family: 'KCCMurukmuruk', sans-serif;
}

.form-group {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.form-group label {
  margin-right: 10px;
  width: 60px;
}

.form-group input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  height: 20px;
}

.error-message {
  color: red;
  margin: 10px 0;
  font-size: 14px;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-family: 'KCCMurukmuruk', sans-serif;
  height: 48px;
}

button:hover {
  background-color: #38a169;
}
</style>