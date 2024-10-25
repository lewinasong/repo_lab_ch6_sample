<template>
  <div class="container">
    <!-- 상단에 로그인 사용자 정보 표시 -->
    <div class="user-info">
      <p>로그인 사용자: {{ user?.employeeNumber || "정보 없음" }}</p>
      <button @click="downloadBatFile" class="download-btn">
        <span class="icon">⬇️</span> 실행 프로그램 다운로드
      </button> <!-- 실행 프로그램 다운로드 버튼 -->
    </div>

    <h1>수행 프로그램 목록</h1>
    <div class="table-header">
      <span class="execution-date">실행 일시: {{ executionDate || "불러오는 중..." }}</span>
    </div>

    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>

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
import { useUserStore } from '@/stores/userStore';
import axios from 'axios';

export default {
  setup() {
    const userStore = useUserStore();
    const user = userStore.user;

    return {
      user,
    };
  },
  data() {
    return {
      executionDate: '',
      programList: [],
      errorMessage: '',
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
        const programResponse = await axios.get('/api/programs');
        this.executionDate = programResponse.data.executionDate;
        this.programList = programResponse.data.programList;

        const statusResponse = await axios.get('/api/programs/status');
        const statusData = statusResponse.data;

        this.programList = this.programList.map(program => {
          const status = statusData.find(statusItem => statusItem.id === program.id);
          return {
            ...program,
            success: status ? status.success : false,
          };
        });
      } catch (error) {
        this.errorMessage = "프로그램 목록을 불러오는 중 에러가 발생했습니다.";
        console.error("API 호출 에러:", error);
      } finally {
        this.isLoading = false;
      }
    },
    downloadBatFile() {
     // const empNo = this.user.employeeNumber; // 로그인된 사용자 정보에서 empNo 가져옴
        const empNo = '1023083'; // 로그인된 사용자 정보에서 empNo 가져옴
      const batFileContent = `
@echo off

setlocal enabledelayedexpansion

set empNo=${empNo}


echo Calling API to fetch program information for employee number %empNo%
curl -X GET "http://localhost:8080/api/programs/%empNo%" -H "Content-Type: application/json" -o programs.json


echo Showing programs.json content:
type programs.json


echo Extracting program sequence, IDs, file paths, and sleep times...
for /f "delims=" %%i in ('jq -r ".[] | (.sequence | tostring) + \\" \\" + (.pgmId | tostring) + \\" \\" + .filePath + \\" \\" + (.sleepTime | tostring)" programs.json') do (
    for /f "tokens=1,2,3,4" %%a in ("%%i") do (
        echo Running program sequence: %%a, pgmId: %%b for empNo: %empNo% at %%c with sleep time: %%d seconds

        
        call "%%c"
    
        if errorlevel 1 (
            set scssYn=0
            echo Program sequence: %%a, pgmId: %%b for empNo: %empNo% failed, scssYn: !scssYn!
        ) else (
            set scssYn=1
            echo Program sequence: %%a, pgmId: %%b for empNo: %empNo% succeeded, scssYn: !scssYn!
        )

        echo Calling API to insert program status for empNo: %empNo%, pgmId: %%b, scssYn: !scssYn!
        curl -X POST "http://localhost:8080/api/insertStatus" ^
            -H "Content-Type: application/json" ^
            -d "{\\"empNo\\": \\"%empNo%\\", \\"pgmId\\": \\"%%b\\", \\"scssYn\\": !scssYn!}"

        echo Sleeping for %%d seconds...
        timeout /t %%d /nobreak >nul
    )
)

pause
      `;

      const blob = new Blob([batFileContent], { type: 'text/plain' });
      const url = URL.createObjectURL(blob);

      const link = document.createElement('a');
      link.href = url;
      link.download = 'PC자동실행프로그램.bat';
      link.click();

      URL.revokeObjectURL(url); // 다운로드 후 URL 제거
    },
  },
};
</script>

<style scoped>
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

.download-btn {
  padding: 10px 20px;
  font-size: 14px;
  background-color: #FFEFD5;
  color: black;
  border: none;
  cursor: pointer;
  font-family: 'KCCMurukmuruk', sans-serif;
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
  justify-content: flex-end;
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

.success {
  color: green;
}

.failure {
  color: red;
}

.error-message {
  color: red;
  font-weight: bold;
  margin-top: 20px;
}
</style>
