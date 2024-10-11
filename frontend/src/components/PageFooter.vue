<!-- PageFooter.vue -->
<template>
  <hr/>
  <footer class="footer">
    Copyright(c)2002 LIPOLAB TEAM 6 All rights reserved.
  </footer>
</template>

<script>
export default {}
</script>

<style scoped>
.footer {
  position: fixed; /* 화면에 고정 */
  bottom: 0; /* 하단에 위치 */
  left: 0; /* 왼쪽 끝에 정렬 */
  width: 100%; /* 화면의 전체 너비 사용 */
  background-image: url('C:/Users/user/Desktop/sample_project/repo_lab_ch6_sample/frontend/sam/src/assets/downimage.jpg'); /* 이미지 경로 설정 */
  background-size: cover; /* 이미지가 전체 배경을 덮도록 설정 */
  background-position: center; /* 배경 이미지 위치 */
  padding: 20px;
  text-align: right; /* 텍스트를 오른쪽으로 정렬 */
  font-size: 14px; /* 텍스트 크기 조정 */
}
</style>