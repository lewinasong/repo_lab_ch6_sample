<!-- PageFooter.vue -->
<template>
  <hr/>
  <footer class="footer">
    Copyright(c)2024 LIPOLAB TEAM 6 All rights reserved.
  </footer>
</template>

<script>
export default {}
</script>

<style scoped>
.footer {
  position: fixed; /* 화면에 고정 */
  bottom: 0; /* 하단에 위치 */
  left: 0;
  width: 100%; /* 화면 전체 너비 사용 */
  background-image: url('@/assets/downimage.jpg'); /* 배경 이미지 설정 */
  background-size: cover; /* 배경 이미지가 전체를 덮도록 설정 */
  background-position: center;
  padding: 10px 60px; /* 좌우 패딩을 늘려서 텍스트가 화면 끝에 붙지 않도록 함 */
  text-align: right; /* 텍스트를 오른쪽으로 정렬 */
  font-size: clamp(12px, 1.5vw, 16px); /* 최소 12px, 최대 16px으로 크기 조정 */
  white-space: nowrap; /* 텍스트가 한 줄로 유지되도록 설정 */
  overflow: hidden; /* 넘치는 텍스트를 잘라냄 */
  text-overflow: ellipsis; /* 넘치는 텍스트에 생략 기호(...) 추가 */
  box-sizing: border-box; /* 패딩과 너비 계산을 함께 포함하도록 설정 */
}
</style>