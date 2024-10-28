<template>
  <div class="container">
    <div class="user-info">
      <p>로그인 사용자: {{ user.name ? user.name + " (" + user.employeeNumber + ")" : "정보 없음" }}</p>
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
    this.loadUserFromLocalStorage();
    this.fetchProgramData();
  },
  methods: {
    loadUserFromLocalStorage() {
      const storedUser = localStorage.getItem('user');
      if (storedUser) {
        this.user = JSON.parse(storedUser); 
      } else {
        this.user = { name: this.user.name, employeeNumber: this.user.employeeNumber };
        localStorage.setItem('user', JSON.stringify(this.user));
      }
    },
    async fetchProgramData() {
      this.isLoading = true;
      try {
        const programResponse = await axios.get(`/api/program/PagePgmBase/${this.user.employeeNumber}`);
        this.programList = programResponse.data.map((program) => ({
          ...program,
          scssYn: null,
        }));

        const statusResponse = await axios.get(`/api/searchByEmpNo/${this.user.employeeNumber}`);
        const statusData = statusResponse.data;

        const execListResponse = await axios.get(`/api/program/PagePgmDtlEmpno/${this.user.employeeNumber}`);
        const execOrderList = execListResponse.data;

        const orderedProgramList = execOrderList.map((pgmId) => {
          const program = this.programList.find((item) => item.pgmId.toString() === pgmId.toString()) || {};
          const matchingStatus = statusData.find((status) => status.pgmId.toString() === pgmId.toString());
          return {
            ...program,
            scssYn: matchingStatus ? matchingStatus.scssYn : null,
            pgmStrDtm: matchingStatus ? matchingStatus.pgmStrDtm : null,
          };
        });

        this.programList = orderedProgramList;
        this.calculateMaxExecutionDate();
      } catch (error) {
        if (error.response && error.response.status === 500 && error.response.data.includes("해당 직원번호에 대한 프로그램이 존재하지 않습니다.")) {
          this.errorMessage = "해당 직원번호로 등록된 프로그램이 없습니다.";
        } else {
          this.errorMessage = "프로그램 목록을 불러오는 중 에러가 발생했습니다.";
        }
      } finally {
        this.isLoading = false;
      }
    },
    calculateMaxExecutionDate() {
      const validDates = this.programList
        .map((item) => item.pgmStrDtm)
        .filter((date) => date);

      if (validDates.length > 0) {
        const maxDate = validDates.reduce((max, date) => {
          const itemDate = new Date(date);
          return itemDate > max ? itemDate : max;
        }, new Date(validDates[0]));

        this.executionDate = maxDate.toLocaleString();
      } else {
        this.executionDate = "실행 기록 없음";
      }
    },
    downloadBatFile() {
      const empNo = this.user.employeeNumber || "정보 없음";
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
      const blob = new Blob([batFileContent], { type: "text/plain" });
      const url = URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = "PC자동실행프로그램.bat";
      link.click();
      URL.revokeObjectURL(url);
    },
  },
};
</script>

<style scoped>
.user-info {
  display: flex;
  align-items: center;
  text-align: left;
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: bold;
}

.download-btn {
  margin-left: auto;
  padding: 10px 20px;
  font-size: 14px;
  background-color: #FFEFD5;
  color: black;
  border: none;
  cursor: pointer;
  font-family: "KCCMurukmuruk", sans-serif;
}

.container {
  width: 60%;
  max-width: 800px;
  margin: 0 auto;
  padding: 80px 0;
  font-family: "KCCMurukmuruk", sans-serif;
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

table,
th,
td {
  border: 1px solid black;
}

th,
td {
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
