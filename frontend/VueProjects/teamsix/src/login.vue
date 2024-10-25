<template>
  <div class="login">
    <h2>Login</h2>
    <form @submit.prevent="handleLogin">
      <div class="input-container">
        <label for="username">Username (ID)</label>
        <input 
          type="text" 
          id="username"
          v-model="username" 
          required 
          class="input-field"
          placeholder="Enter your username"
        />
        <div class="underline"></div>
      </div>
      <div class="input-container">
        <label for="password">Password</label>
        <input 
          type="password" 
          id="password"
          v-model="password" 
          required 
          class="input-field"
          placeholder="Enter your password"
        />
        <div class="underline"></div>
      </div>
      <button type="submit" class="login-button">Login</button>
    </form>
    <div v-if="error" class="error">
      {{ error }}
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: '',  // 사용자 아이디
      password: '',  // 비밀번호
      error: null,   // 에러 메시지
    };
  },
  methods: {
    async handleLogin() {
      this.error = null;  // 에러 메시지 초기화
      try {
        const response = await fetch('https://your-api-url.com/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            username: this.username,  // 아이디 값
            password: this.password,  // 비밀번호 값
          }),
        });

        if (!response.ok) {
          throw new Error('Login failed');
        }

        const data = await response.json();
        // 로그인 성공 시 토큰 저장 및 페이지 이동
        localStorage.setItem('token', data.token);
        this.$router.push('/dashboard');  // 로그인 성공 후 대시보드로 이동
      } catch (err) {
        this.error = err.message;  // 에러 발생 시 메시지 출력
      }
    },
  },
};
</script>

<style scoped>
.login {
  width: 300px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
}

h2 {
  margin-bottom: 20px;
}

.input-container {
  margin-bottom: 20px;
  width: 100%;
  position: relative;
}

.input-field {
  width: 100%;
  padding: 10px;
  border: none;
  border-bottom: 2px solid #ccc;
  font-size: 16px;
  outline: none;
}

.input-field:focus {
  border-bottom: 2px solid #007BFF; /* 포커스 시 밑줄 색 변경 */
}

.underline {
  height: 2px;
  background-color: #ccc;
  width: 100%;
  position: absolute;
  bottom: 0;
  left: 0;
}

.login-button {
  width: 100%;
  padding: 10px;
  background-color: #007BFF;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
}

.login-button:hover {
  background-color: #0056b3;
}

.error {
  color: red;
  margin-top: 10px;
}
</style>