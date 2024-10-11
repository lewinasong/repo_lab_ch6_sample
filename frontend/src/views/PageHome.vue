<!-- PageHome.vue -->
<template>
  <div class="container">
    <h1>수행 프로그램 목록</h1>
    <table>
      <thead>
        <tr>
          <th>순번</th>
          <th>프로그램명</th>
          <th>성공 여부</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, index) in programList" :key="index">
          <td>{{ index + 1 }}</td>
          <td>{{ item.name }}</td>
          <td>{{ item.success ? '성공' : '실패' }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      programList: [
        { name: '프로그램 A', success: true },
        { name: '프로그램 B', success: false },
        { name: '프로그램 C', success: true },
      ],
    };
  },
};
</script>

<style scoped>

@font-face {
  font-family: 'KCCMurukmuruk';
  src: url('C:/Users/user/Desktop/sample_project/repo_lab_ch6_sample/frontend/sam/src/fonts/KCCMurukmuruk.ttf') format('truetype');
}

.container {
  width: 60%; /* 너비를 60%로 설정 */
  max-width: 800px;
  margin: 0 auto; /* 가로 중앙 정렬 */
  padding: 80px 0; /* 상하 40px 패딩, 좌우 패딩은 0 */
  font-family: 'KCCMurukmuruk', sans-serif;
}

h1 {
  text-align: center;
  padding: 20px 0; /* 상하 20px 패딩, 좌우 패딩은 0 */
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

table, th, td {
  border: 1px solid black;
}

th, td {
  padding: 8px;
  text-align: center;
}
</style>
