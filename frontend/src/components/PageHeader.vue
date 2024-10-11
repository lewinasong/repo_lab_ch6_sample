<template>
  <header :class="{ 'home-header': isHomePage }">
    <div class="system-name">♥일일 PC 자동세팅 시스템♥</div>
    <!-- 로그인 화면으로 돌아가기 링크를 우측 상단에 배치 -->
    <div class="login-link" v-if="!isHomePage">
      <router-link to="/">로그인화면으로 돌아가기</router-link>
    </div>
    <div class="header" id="nav" v-if="!isHomePage">
      <router-link to="/PageHome">Home</router-link>
      <span class="separator">|</span>
      <router-link to="/PagePgmBase">프로그램 등록 및 수정</router-link>
      <span class="separator">|</span>
      <router-link to="/PagePgmDtl">프로그램 실행순서 설정</router-link>
    </div>
  </header>
  <hr/>
</template>

<script>
export default {
  computed: {
    isHomePage() {
      return this.$route.path === '/'; // 현재 경로가 '/'인지 확인
    },
  },
};
</script>

<style scoped>
header {
  background-image: url('@/assets/upimage.jpg');
  background-size: cover;
  background-position: center;
  padding: 20px;
  transition: height 0.3s ease;
  position: relative; /* 자식 요소의 절대 위치 조정을 위해 relative로 설정 */
}

/* 홈 경로일 때 헤더 높이를 더 크게 설정 */
.home-header {
  height: 170px;
}

.system-name {
  text-align: left;
  padding: 20px;
  font-size: 20px;
  color: #0000CD;
  text-shadow: 2px 2px 4px #aaa;
  font-family: 'KCCMurukmuruk', sans-serif;
  animation: blink 1.5s infinite alternate;
}

/* 로그인 링크를 우측 상단에 배치 */
.login-link {
  position: absolute; /* 부모 요소에 상대적인 절대 위치 */
  top: 20px; /* 상단에서 20px 아래 */
  right: 20px; /* 우측에서 20px 왼쪽 */
  font-size: 16px;
  font-family: 'KCCMurukmuruk', sans-serif;
}

.header {
  justify-content: space-between;
  font-size: 25px;
  font-family: 'KCCMurukmuruk', sans-serif; 
  width: 60%;
  margin: 0 auto;
}

.separator {
  margin: 0 50px;
}

/* 반짝이는 효과 애니메이션 */
@keyframes blink {
  0% {
    color: #0000FF;
    text-shadow: 2px 2px 4px #aaa;
  }
  100% {
    color: #8A2BE2;
    text-shadow: 2px 2px 4px #aaa;
  }
}
</style>