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
            placeholder="행번을 입력해주세요"
            required
          />
        </div>
        <button type="submit">Login</button>
      </form>
    </div>
  </template>
  
  <script>
  export default {
    name: "LoginPage",
    data() {
      return {
        employeeNumber: "",
      };
    },
    methods: {
      handleSubmit() {
        console.log("Employee Number:", this.employeeNumber);
        // 행번 확인 후 로그인 성공 시 메인 페이지로 이동
        this.$router.push({ name: 'PageHome' });
      },
    },
  };
  </script>
  

  <style scoped>

  /* 커스텀 폰트 설정 */
  @font-face {
    font-family: 'KCCMurukmuruk'; /* 폰트 이름 정의 */
    src: url('C:/Users/user/Desktop/sample_project/repo_lab_ch6_sample/frontend/sam/src/fonts/KCCMurukmuruk.ttf') format('truetype'); /* 폰트 파일 경로 */
  }
  
  /* 페이지 전체 레이아웃 설정 */
  body, html {
    height: 100%; /* 페이지의 전체 높이를 사용 */
    margin: 0; /* 기본 여백 제거 */
    display: flex; /* Flexbox를 사용하여 중앙 정렬 */
    justify-content: center; /* 수평 중앙 정렬 */
    align-items: center; /* 수직 중앙 정렬 */
  }
 
  /* 로그인 컨테이너 스타일 */
  .login-container {
    max-width: 400px; /* 최대 너비 설정 */
    margin: 50px auto 50px; /* 상단 여백 150px, 하단 여백 50px */
    padding: 100px; /* 내부 여백 설정 */
    border: 1px solid #ccc; /* 테두리 스타일 */
    border-radius: 8px; /* 테두리 둥글게 설정 */
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* 그림자 효과 추가 */
    font-family: 'KCCMurukmuruk', sans-serif; /* 커스텀 폰트 적용 */
  }
  
  /* 폼 그룹 스타일 */
  .form-group {
    margin-bottom: 15px; /* 각 폼 그룹 간의 간격 설정 */
  }
  
  /* 폼 라벨 스타일 */
  .form-group label {
    display: block; /* 블록 요소로 설정하여 줄 바꿈 */
    margin-bottom: 5px; /* 라벨과 입력 필드 간의 간격 설정 */
  }
  
*,
*::before,
*::after {
  box-sizing: border-box; /* 패딩과 테두리를 요소의 전체 크기로 포함시킴 */
}

/* 입력 필드 스타일 */
.form-group input {
  width: 100%; /* 입력 필드의 너비를 부모 요소에 맞춤 */
  padding: 10px; /* 내부 여백 설정 */
  border: 1px solid #ccc; /* 테두리 스타일 */
  border-radius: 4px; /* 테두리 둥글게 설정 */
  height: 48px; /* 버튼과 동일한 높이로 설정 */
}

/* 버튼 스타일 */
button {
  width: 100%; /* 버튼의 너비를 부모 요소에 맞춤 */
  padding: 10px; /* 내부 여백 설정 */
  background-color: #42b983; /* 버튼 배경색 설정 */
  color: white; /* 버튼 텍스트 색상 설정 */
  border: none; /* 기본 테두리 제거 */
  border-radius: 4px; /* 테두리 둥글게 설정 */
  cursor: pointer; /* 마우스 포인터를 손가락 모양으로 변경 */
  font-family: 'KCCMurukmuruk', sans-serif; /* 커스텀 폰트 적용 */
  height: 48px; /* 입력 필드와 동일한 높이로 설정 */
}
  
  /* 버튼 호버 스타일 */
  button:hover {
    background-color: #38a169; /* 마우스를 올렸을 때 배경색 변경 */
  }
</style>