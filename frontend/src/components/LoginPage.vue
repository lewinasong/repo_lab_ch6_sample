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
import { ref } from 'vue';
import { useUserStore } from '@/stores/userStore'; // Pinia 스토어 가져오기
import apiClient from '@/api'; // api.js에서 만든 axios 인스턴스
import { useRouter } from 'vue-router'; // vue-router에서 useRouter 가져오기

export default {
  setup() {
    const employeeNumber = ref(""); // 입력된 행번을 저장
    const errorMessage = ref(""); // 에러 메시지
    const userStore = useUserStore(); // Pinia 스토어 사용
    const router = useRouter(); // useRouter를 통해 라우터 가져오기

    // 행번 유효성 검사 함수
    const validateEmployeeNumber = () => {
      if (employeeNumber.value.length !== 7) {
        errorMessage.value = "행번을 확인해주세요 (7자리 숫자)";
        return false;
      }
      return true;
    };

    // 로그인 핸들러
    const handleSubmit = async () => {
      if (!validateEmployeeNumber()) return;

      errorMessage.value = ""; // 에러 메시지 초기화

      try {
        // API 요청
        const response = await apiClient.get(`/api/login/${employeeNumber.value}`);
        const responseText = response.data;

        // 로그인 성공 여부 판단
        if (responseText.startsWith("로그인 성공")) {
          // 직원 이름 추출
          const employeeName = responseText.split("직원 이름: ")[1];

          // Pinia 스토어에 사용자 정보 저장
          userStore.setUser({
            employeeNumber: employeeNumber.value,
            name: employeeName
          });

          // 로그인 후 메인 페이지로 이동 (useRouter로 라우터 사용)
          router.push({ name: 'PageHome' });
        } else {
          // 로그인 실패 시 에러 메시지 표시
          errorMessage.value = "로그인에 실패했습니다. 다시 시도해주세요.";
        }

      } catch (error) {
        // 네트워크 오류 또는 기타 오류 처리
        console.error("네트워크 또는 서버 오류:", error);
        errorMessage.value = "로그인에 실패했습니다. 네트워크 상태를 확인해주세요.";
      }
    };

    return {
      employeeNumber,
      errorMessage,
      handleSubmit
    };
  }
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
