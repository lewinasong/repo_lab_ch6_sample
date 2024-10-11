<!-- PageHome.vue -->
<template>
  <div class="container">
    <h1>수행 프로그램 목록</h1>
    <div class="table-header">
      <span class="execution-date">실행 일시: {{ executionDate }}</span> <!-- 실행 일시 우측 정렬 -->
    </div>
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
          <td :class="item.success ? 'success' : 'failure'">
            {{ item.success ? '성공' : '실패' }}
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      executionDate: '2024-10-11 14:30:00', // 예시로 하드코딩된 실행 일시
      programList: [
        { name: '프로그램 A', success: true },
        { name: '프로그램 B', success: false },
        { name: '프로그램 C', success: true },
      ],
    };
  },
  mounted() {
    // 여기에 API 호출 로직을 추가하여 실행 일시 데이터를 가져올 수 있습니다.
  },
};
</script>

<style scoped>
@font-face {
  font-family: 'KCCMurukmuruk';
  src: url('@/fonts/KCCMurukmuruk.ttf') format('truetype');
}

.container {
  width: 60%;
  max-width: 800px;
  margin: 0 auto;
  padding: 80px 0;
  font-family: 'KCCMurukmuruk', sans-serif;
}

h1 {
  text-align: center;
  padding: 20px 0;
}

.table-header {
  display: flex;
  justify-content: flex-end; /* 오른쪽 정렬 */
  margin-bottom: 10px;
}

.execution-date {
  font-size: 16px;
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

/* 성공 여부에 따른 텍스트 색상 스타일 */
.success {
  color: green; /* 성공인 경우 초록색 */
}

.failure {
  color: red; /* 실패인 경우 빨간색 */
}
</style>
