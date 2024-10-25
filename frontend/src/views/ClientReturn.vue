<template>
  <div class="container">
    <h1>프로그램 실행 상태</h1>

    <!-- 프로그램 상태 표시 테이블 -->
    <table class="status-table">
      <thead>
        <tr>
          <th>프로그램명</th>
          <th>상태</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(log, index) in logs" :key="index">
          <td>{{ getProgramName(log) }}</td>
          <td>{{ log.includes('성공') ? '성공' : '실패' }}</td>
        </tr>
      </tbody>
    </table>

    <button @click="fetchStatus">상태 새로고침</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      logs: [] // 로그 데이터를 저장할 배열
    };
  },
  methods: {
    // Node.js 서버에서 프로그램 실행 상태 로그를 가져오는 메서드
    fetchStatus() {
      fetch('http://localhost:3000/api/status') // Node.js 서버의 API에 요청
        .then(response => response.json()) // 응답 데이터를 JSON 형식으로 변환
        .then(data => {
          this.logs = data.logs; // logs 배열에 API 응답 데이터를 저장
        })
        .catch(error => {
          console.error('Error fetching status:', error); // 오류 발생 시 콘솔에 출력
        });
    },
    getProgramName(log) {
      // 로그에서 프로그램 이름을 추출하는 로직 (예: "Chakra 실행 성공"에서 "Chakra" 추출)
      return log.split(' ')[0];
    }
  },
  mounted() {
    this.fetchStatus(); // 컴포넌트가 마운트될 때 상태를 불러옴
  }
};
</script>

<style scoped>
.container {
  width: 60%;
  max-width: 800px;
  margin: 0 auto;
  padding: 80px 0;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

th, td {
  border: 1px solid #ccc;
  padding: 10px;
  text-align: left;
}

button {
  margin-top: 20px;
  padding: 10px 20px;
  font-size: 16px;
}
</style>
