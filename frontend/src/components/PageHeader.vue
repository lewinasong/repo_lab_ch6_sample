<template>
  <header :class="{ 'home-header': isHomePage }">
    <div class="system-name">♥일일 PC 자동세팅 시스템♥</div>
    <!-- 로그인 화면으로 돌아가기 링크를 우측 상단에 배치 -->
    <div class="login-link" v-if="!isHomePage">
      <router-link to="/" @click.prevent="handleLogout">로그인화면으로 돌아가기</router-link>
    </div>
    <div class="header" id="nav" v-if="!isHomePage">
      <router-link to="/PageHome">프로그램 모니터링</router-link>
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
  methods: {
    handleLogout(event) {
      event.preventDefault(); // 기본 동작 방지
      localStorage.clear(); // localStorage의 모든 항목을 삭제하여 완전히 초기화
      this.$router.push('/'); // 수동으로 홈 경로로 이동
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
  right: 40px;
  font-size: clamp(12px, 2.5vw, 18px);
  font-family: 'KCCMurukmuruk', sans-serif;
  max-width: 200px;
  padding: 5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.header {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  font-size: clamp(16px, 3vw, 25px);
  font-family: 'KCCMurukmuruk', sans-serif;
  width: 100%;
  margin: 0 auto;
}

.separator {
  margin: 0 10px;
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
