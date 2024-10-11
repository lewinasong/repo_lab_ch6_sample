<template>
  <header :class="{ 'home-header': isHomePage }">
    <div class="system-name">♥일일 PC 자동세팅 시스템♥</div>
    <div class="header" id="nav" v-if="!isHomePage">
      <router-link to="/">로그인화면으로 돌아가기</router-link>
      <span class="separator">|</span>
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
  background-image: url('C:/Users/user/Desktop/sample_project/repo_lab_ch6_sample/frontend/sam/src/assets/upimage.jpg'); /* 이미지 경로 설정 */
  background-size: cover; /* 이미지가 전체 배경을 덮도록 설정 */
  background-position: center; /* 배경 이미지 위치 */
  padding: 20px;
  transition: height 0.3s ease; /* 높이 변경 시 부드러운 전환 효과 */
}

/* 홈 경로일 때 헤더 높이를 더 크게 설정 */
.home-header {
  height: 170px; /* 첫 화면에서 더 많은 배경 이미지를 표시하도록 높이 설정 */
}

/* @font-face를 사용하여 폰트를 정의 */
@font-face {
  font-family: 'KCCMurukmuruk';
  src: url('C:/Users/user/Desktop/sample_project/repo_lab_ch6_sample/frontend/sam/src/fonts/KCCMurukmuruk.ttf') format('truetype');
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

/* 배경색과 간격 조정 */
.header {
  justify-content: space-between;
  font-size: 25px;
  font-family: 'KCCMurukmuruk', sans-serif; 
  width: 60%; /* 가로 사이즈를 80%로 조정 */
  margin: 0 auto; /* 중앙 정렬 */
}

.separator {
  margin: 0 50px; /* 파이프(|) 기호와 링크 사이의 간격 조정 */
}

</style>
