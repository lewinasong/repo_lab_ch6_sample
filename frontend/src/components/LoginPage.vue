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
      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p> <!-- 에러 메시지 표시 -->
      <button type="submit">Login</button>
    </form>
  </div>
</template>

<script>
import { useUserStore } from '@/stores/userStore'; // Pinia 스토어 가져오기

export default {
  name: "LoginPage",
  data() {
    return {
      employeeNumber: "", // 입력된 행번을 저장
      errorMessage: "", // 에러 메시지
    };
  },
  methods: {
    handleSubmit() {
      if (this.employeeNumber.length !== 7) {
        this.errorMessage = "행번을 확인해주세요"; // 에러 메시지
      } else {
        this.errorMessage = ""; // 에러 메시지 초기화
        const userStore = useUserStore();
        
        // Pinia 스토어에 사용자 정보 저장
        userStore.setUser({
          employeeNumber: this.employeeNumber, // 사용자 행번
          name: "홍길동" // 이 부분은 실제 데이터로 수정
        });

        // 로그인 후 메인 페이지로 이동
        this.$router.push({ name: 'PageHome' });
      }
    },
  },
};
</script>


<style scoped>
/* 사용자 정보를 우측 상단에 고정하는 스타일 */
.user-info {
  position: fixed; /* 화면에 고정 */
  top: 10px; /* 상단에서 10px 떨어진 위치 */
  right: 20px; /* 우측에서 20px 떨어진 위치 */
  background-color: #f4f4f4; /* 배경색 설정 */
  padding: 10px; /* 여백 설정 */
  border-radius: 5px; /* 모서리 둥글게 설정 */
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* 약간의 그림자 효과 */
  font-size: 14px; /* 폰트 크기 설정 */
}
/* 커스텀 폰트 설정 */
@font-face {
  font-family: 'KCCMurukmuruk'; /* 커스텀 폰트의 이름 정의 */
  src: url('@/fonts/KCCMurukmuruk.ttf') format('truetype'); /* 폰트 파일의 경로 및 형식 지정 */
}

/* 페이지 전체 레이아웃 설정 */
body, html {
  height: 100%; /* 페이지 전체 높이를 화면에 맞게 설정 */
  margin: 0; /* 기본 여백 제거 */
  display: flex; /* Flexbox 레이아웃을 사용하여 중앙 정렬 */
  justify-content: center; /* 수평으로 중앙 정렬 */
  align-items: center; /* 수직으로 중앙 정렬 */
}

/* 로그인 컨테이너 스타일 */
.login-container {
  max-width: 400px; /* 로그인 컨테이너의 최대 너비 설정 */
  margin: 50px auto; /* 위아래로 50px 마진을 두고, 좌우는 중앙 정렬 */
  padding: 40px; /* 컨테이너 내부 여백 설정 */
  border: 1px solid #ccc; /* 회색 테두리 추가 */
  border-radius: 8px; /* 테두리를 둥글게 설정 */
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* 컨테이너 주위에 살짝 그림자 효과 추가 */
  font-family: 'KCCMurukmuruk', sans-serif; /* 컨테이너 내 모든 텍스트에 커스텀 폰트 적용 */
}

/* 폼 그룹 스타일 */
.form-group {
  display: flex; /* Flexbox를 사용하여 레이블과 입력 필드를 가로로 정렬 */
  align-items: center; /* 레이블과 입력 필드를 수직 가운데 정렬 */
  margin-bottom: 15px; /* 각 폼 그룹 간의 간격 설정 */
}

/* 폼 라벨 스타일 */
.form-group label {
  margin-right: 10px; /* 레이블과 입력 필드 사이의 간격 설정 */
  width: 60px; /* 레이블의 고정된 너비 설정으로 정렬 일관성 유지 */
}

/* 입력 필드 스타일 */
.form-group input {
  flex: 1; /* 입력 필드가 가능한 공간을 최대한 차지하도록 설정 */
  padding: 10px; /* 입력 필드 내부 여백 설정 */
  border: 1px solid #ccc; /* 회색 테두리 추가 */
  border-radius: 4px; /* 입력 필드의 테두리를 둥글게 설정 */
  height: 20px; /* 입력 필드의 높이 설정 */
}

/* 에러 메시지 스타일 */
.error-message {
  color: red; /* 에러 메시지를 빨간색으로 표시 */
  margin: 10px 0; /* 위아래 간격 설정 */
  font-size: 14px; /* 에러 메시지의 글씨 크기 설정 */
}

/* 버튼 스타일 */
button {
  width: 100%; /* 버튼 너비를 부모 요소의 너비에 맞춤 */
  padding: 10px; /* 버튼 내부 여백 설정 */
  background-color: #42b983; /* 버튼의 배경색 설정 */
  color: white; /* 버튼 텍스트 색상 설정 */
  border: none; /* 기본 테두리 제거 */
  border-radius: 4px; /* 버튼의 테두리를 둥글게 설정 */
  cursor: pointer; /* 마우스 포인터가 버튼 위에 있을 때 손가락 모양으로 변경 */
  font-family: 'KCCMurukmuruk', sans-serif; /* 커스텀 폰트 적용 */
  height: 48px; /* 버튼의 높이를 입력 필드와 일치하도록 설정 */
}

/* 버튼 호버 스타일 */
button:hover {
  background-color: #38a169; /* 마우스를 버튼 위에 올렸을 때 배경색 변경 */
}
</style>
