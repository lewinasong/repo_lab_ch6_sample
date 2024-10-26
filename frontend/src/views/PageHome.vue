<template>
  <div class="container">
    <div class="user-info">
      <p>로그인 사용자: {{ user.employeeNumber || "정보 없음" }}</p>
    </div>

    <h1>수행 프로그램 목록</h1>

    <table v-if="programList.length">
      <thead>
        <tr>
          <th>순번</th>
          <th>프로그램명</th>
          <th>상태</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, index) in programList" :key="index">
          <td>{{ index + 1 }}</td>
          <td>{{ item.pgmNm }}</td> <!-- 프로그램명 표시 -->
          <td :class="item.scssYn === 1 ? 'success' : item.scssYn === 0 ? 'failure' : 'running'">
            <!-- 완료, 확인필요, 기동중 표시 -->
            {{ item.scssYn === 1 ? '완료' : item.scssYn === 0 ? '확인필요' : '기동중' }}
          </td>
        </tr>
      </tbody>
    </table>

    <p v-else>프로그램 목록을 불러오는 중입니다...</p>
  </div>
</template>

<script>
import { useUserStore } from "@/stores/userStore";
import axios from "axios";

export default {
  setup() {
    const userStore = useUserStore();
    const user = userStore;

    return {
      user,
    };
  },
  data() {
    return {
      programList: [],
      isLoading: false,
    };
  },
  mounted() {
    this.fetchProgramData();
  },
  methods: {
    async fetchProgramData() {
      this.isLoading = true;
      try {
        // 첫 번째 API 호출 - 프로그램 목록 불러오기
        const programResponse = await axios.get(`/api/program/PagePgmBase/${this.user.employeeNumber}`);
        this.programList = programResponse.data.map((program) => ({
          ...program,
          scssYn: null, // 성공 여부는 두 번째 API로부터 받을 예정
        }));

        console.log("Program List:", this.programList); // 첫 번째 API 응답 확인

        // 두 번째 API 호출 - 성공 여부 상태값 불러오기
        const statusResponse = await axios.get(`/api/searchByEmpNo/${this.user.employeeNumber}`);
        const statusData = statusResponse.data;
        console.log("Status Data:", statusData); // 두 번째 API 응답 확인

        // 세 번째 API 호출 - EXEC_LIST에서 순서대로 프로그램 ID 가져오기
        const execListResponse = await axios.get(`/api/program/PagePgmDtlEmpno/${this.user.employeeNumber}`);
        const execOrderList = execListResponse.data; // 프로그램 ID 리스트를 순서대로 가져옴
        console.log("Exec Order List:", execOrderList); // 세 번째 API 응답 확인

        // 첫 번째 API 응답과 두 번째 API 응답 데이터 결합
        const orderedProgramList = execOrderList.map((pgmId) => {
          const program = this.programList.find((item) => item.pgmId.toString() === pgmId.toString()) || {};
          const matchingStatus = statusData.find((status) => status.pgmId.toString() === pgmId.toString());
          return {
            ...program,
            scssYn: matchingStatus ? matchingStatus.scssYn : null, // 매칭되지 않으면 null로 설정
          };
        });

        this.programList = orderedProgramList;
      } catch (error) {
        console.error("API 호출 에러:", error);
      } finally {
        this.isLoading = false;
      }
    },
  },
};
</script>

<style scoped>
.container {
  width: 60%;
  max-width: 800px;
  margin: 0 auto;
  padding: 80px 0;
  font-family: 'KCCMurukmuruk', sans-serif;
}

.user-info {
  text-align: right;
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: bold;
}

h1 {
  text-align: center;
  padding: 20px 0;
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

.success {
  color: green;
}

.failure {
  color: red;
}

.running {
  color: blue;
}

.error-message {
  color: red;
  font-weight: bold;
  margin-top: 20px;
}
</style>
