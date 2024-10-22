<template>
  <header>
    <div class="system-name">♥일일 PC 자동세팅 시스템♥</div>
    <div class='header' id="nav">
      <router-link to="/">Home</router-link>
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

}
</script>

<style scoped>

/* @font-face를 사용하여 폰트를 정의 */
@font-face {
  font-family: 'KCCMurukmuruk';
  src: url('C:/Users/82103/VueProjects/teamsix/src/fonts/KCCMurukmuruk.ttf') format('truetype');
}

.system-name {
  text-align: left;
  padding: 10px;
  background-color: #FFFFF0;
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
  padding: 10px;
  background-color: #FFFFF0;
  font-size: 25px;
  font-family: 'KCCMurukmuruk', sans-serif;
}

.separator {
  margin: 0 100px; /* 파이프(|) 기호와 링크 사이의 간격 조정 */
}

</style>
