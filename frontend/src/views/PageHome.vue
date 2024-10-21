<template>
  <div class="container">
    <!-- 상단에 로그인 사용자 정보 표시 -->
    <div class="user-info">
      <p>로그인 사용자: {{ user?.employeeNumber || "정보 없음" }}</p> <!-- 사용자 정보가 없을 때 "정보 없음" 출력 -->
    </div>

    <h1>수행 프로그램 목록</h1>
    <div class="table-header">
      <span class="execution-date">실행 일시: {{ executionDate || "불러오는 중..." }}</span> <!-- 실행 일시 우측 정렬 -->
    </div>

    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p> <!-- 에러 메시지 표시 -->

    <table v-if="programList.length">
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
    
    <p v-else>프로그램 목록을 불러오는 중입니다...</p>
  </div>
</template>

<script>
import { useUserStore } from '@/stores/userStore'; // 사용자 정보를 Pinia 또는 Vuex에서 가져옴
import axios from 'axios'; // axios를 사용하여 API 호출

export default {
  setup() {
    const userStore = useUserStore(); // Pinia에서 사용자 정보 가져오기
    const user = userStore.user; // 사용자 정보

    return {
      user,
    };
  },
  data() {
    return {
      executionDate: '', // API에서 실행 일시 받아오기
      programList: [], // API에서 프로그램 목록 받아오기
      errorMessage: '', // 에러 메시지
      isLoading: false, // 로딩 상태 표시
    };
  },
  mounted() {
    // 컴포넌트가 마운트될 때 프로그램 목록과 실행 일시를 API로부터 가져옴
    this.fetchProgramData();
  },
  methods: {
    async fetchProgramData() {
      this.isLoading = true; // 로딩 시작
      try {
        // 1. 프로그램 목록 API 호출
        const programResponse = await axios.get('/api/programs');
        this.executionDate = programResponse.data.executionDate;
        this.programList = programResponse.data.programList;

        // 2. 각 프로그램에 대한 성공 여부 API 호출
        const statusResponse = await axios.get('/api/programs/status');
        const statusData = statusResponse.data;

        // 프로그램 리스트에 성공 여부를 매핑
        this.programList = this.programList.map(program => {
          const status = statusData.find(statusItem => statusItem.id === program.id);
          return {
            ...program,
            success: status ? status.success : false, // 성공 여부를 프로그램에 추가
          };
        });

      } catch (error) {
        // API 호출 실패 시 에러 메시지 처리
        this.errorMessage = "프로그램 목록을 불러오는 중 에러가 발생했습니다.";
        console.error("API 호출 에러:", error);
      } finally {
        this.isLoading = false; // 로딩 종료
      }
    },
  },
};
</script>

<style scoped>
/* 사용자 정보를 우측 상단에 고정하는 스타일 */
.user-info {
  text-align: right;
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: bold;
}

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

/* 에러 메시지 스타일 */
.error-message {
  color: red;
  font-weight: bold;
  margin-top: 20px;
}
</style>