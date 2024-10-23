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
  background-repeat: no-repeat;
  background-position: center;
  padding: 20px;
  transition: height 0.3s ease;
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  width: 100%;
  max-width: 100%;
}

/* 홈 경로일 때 헤더 높이를 더 크게 설정 */
.home-header {
  height: 170px;
}

.system-name {
  font-size: clamp(16px, 4vw, 30px);
  color: #0000CD;
  text-shadow: 2px 2px 4px #aaa;
  font-family: 'KCCMurukmuruk', sans-serif;
  animation: blink 1.5s infinite alternate;
}

/* 로그인 링크를 우측 상단에 배치 */
.login-link {
  position: absolute;
  top: 20px;
  right: 40px; /* 오른쪽에서의 간격을 넓혀 텍스트가 잘리지 않도록 함 */
  font-size: clamp(12px, 2.5vw, 18px);
  font-family: 'KCCMurukmuruk', sans-serif;
  max-width: 200px; /* 텍스트가 줄어들지 않도록 최대 너비 조정 */
  padding: 5px;
  white-space: nowrap; /* 텍스트 줄바꿈 방지 */
  overflow: hidden; /* 텍스트가 잘리지 않도록 함 */
  text-overflow: ellipsis; /* 텍스트가 너무 길 경우 생략 기호 처리 */
}

.header {
  display: flex;
  justify-content: center; /* 요소들을 중앙에 배치 */
  align-items: center;
  gap: 20px; /* 요소들 간의 간격을 적절하게 조정 */
  font-size: clamp(16px, 3vw, 25px);
  font-family: 'KCCMurukmuruk', sans-serif; 
  width: 100%;
  margin: 0 auto;
}

.separator {
  margin: 0 10px; /* 링크 사이의 간격을 줄여서 간격을 좁게 만듦 */
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