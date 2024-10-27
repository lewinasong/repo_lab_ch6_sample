<template>
  <div class="container">
    <div class="user-info">
      <p>로그인 사용자: {{ user.employeeNumber || "정보 없음" }}</p>
      <button @click="downloadBatFile" class="download-btn">
        <span class="icon">⬇️</span> 실행 프로그램 다운로드
      </button>
    </div>

    <h1>수행 프로그램 목록</h1>
    <div class="table-header">
      <span class="execution-date">
        실행 일시:
        {{
          isLoading ? "불러오는 중..." :
          (executionDate || "실행 기록 없음")
        }}
      </span>
    </div>
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>

    <!-- 프로그램 목록이 없고 오류 메시지도 없을 때만 "불러오는 중..." 표시 -->
    <table v-if="programList.length && !errorMessage">
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
          <td>{{ item.pgmNm }}</td>
          <td :class="item.scssYn === 1 ? 'success' : item.scssYn === 0 ? 'failure' : 'running'">
            {{ item.scssYn === 1 ? '완료' : item.scssYn === 0 ? '확인 필요' : '실행된 프로그램이 없습니다' }}
          </td>
        </tr>
      </tbody>
    </table>

    <p v-else-if="!programList.length && !errorMessage">프로그램 목록을 불러오는 중입니다...</p>
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
      executionDate: null,
      errorMessage: null,
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
        const execOrderList = execListResponse.data;
        console.log("Exec Order List:", execOrderList); // 세 번째 API 응답 확인

        // 첫 번째 API 응답과 두 번째 API 응답 데이터 결합
        const orderedProgramList = execOrderList.map((pgmId) => {
          const program = this.programList.find((item) => item.pgmId.toString() === pgmId.toString()) || {};
          const matchingStatus = statusData.find((status) => status.pgmId.toString() === pgmId.toString());
          return {
            ...program,
            scssYn: matchingStatus ? matchingStatus.scssYn : null,
            pgmStrDtm: matchingStatus ? matchingStatus.pgmStrDtm : null // 실행 일시 추가
          };
        });

        this.programList = orderedProgramList;
        this.calculateMaxExecutionDate(); // 데이터를 결합한 후 실행 일시 계산
      } catch (error) {
        if (error.response && error.response.status === 500 && error.response.data.includes("해당 직원번호에 대한 프로그램이 존재하지 않습니다.")) {
          this.errorMessage = "해당 직원번호로 등록된 프로그램이 없습니다.";
          console.error("Error details:", error.response || error.message || error);
        } else {
          console.error("Error details:", error.response || error.message || error);
          this.errorMessage = "프로그램 목록을 불러오는 중 에러가 발생했습니다.";
        }
      } finally {
        this.isLoading = false;
      }
    },
    calculateMaxExecutionDate() {
      // pgmStrDtm이 유효한 값만 필터링하여 유효한 날짜 리스트 생성
      const validDates = this.programList
        .map((item) => item.pgmStrDtm)
        .filter((date) => date); // undefined, null, 빈 문자열 등을 제외

      if (validDates.length > 0) {
        // 유효한 pgmStrDtm 값들 중 최신 날짜 계산
        const maxDate = validDates.reduce((max, date) => {
          const itemDate = new Date(date);
          return itemDate > max ? itemDate : max;
        }, new Date(validDates[0]));

        this.executionDate = maxDate.toLocaleString();
      } else {
        this.executionDate = '실행 기록 없음';
      }
    },
    downloadBatFile() {
      const empNo = this.user.employeeNumber || '정보 없음';
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
      URL.revokeObjectURL(url);
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

.running {
  color: blue;
}

.error-message {
  color: red;
  font-weight: bold;
  margin-top: 20px;
}
</style>