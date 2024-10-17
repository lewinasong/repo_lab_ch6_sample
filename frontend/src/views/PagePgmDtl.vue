<template>
  <div class="container">
    <!-- 로그인 사용자 정보 -->
    <div class="user-info">
      <p>로그인 사용자: {{ user?.employeeNumber || "정보 없음" }}</p> <!-- 사용자 정보가 없을 때 "정보 없음" 출력 -->
    </div>

    <h1>프로그램 실행순서 설정</h1>

    <!-- 실행 대기 시간 입력 필드 추가 -->
    <div class="delay-container">
      <label for="wait-time">실행 대기 시간 (초):</label>
      <input
        type="text" 
        id="wait-time"
        v-model="waitTime"
        placeholder="예: 3"
        min="0"
        class="wait-time-input"
      />
    </div>

    <!-- 프로그램 순서 입력 폼 -->
    <table class="fixed-table">
      <thead>
        <tr>
          <th class="header-cell">순번 입력</th>
          <th class="header-cell">프로그램명</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(program, index) in programs" :key="index">
          <td>
            <input 
              type="text" 
              v-model="program.order" 
              placeholder="순번 입력"
              @input="validateOrder(program)"
            />
          </td>
          <td>{{ program.name }}</td>
        </tr>
      </tbody>
    </table>

    <button @click="sortPrograms">변경후 프로그램 실행순서 확인</button>

    <!-- 정렬된 프로그램 목록 표시 -->
    <div v-if="sortedPrograms.length > 0">
      <h2>변경후 프로그램 실행순서</h2>
      <table class="fixed-table">
        <thead>
          <tr>
            <th class="header-cell2">순번 입력</th>
            <th class="header-cell2">프로그램명</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(program, index) in sortedPrograms" :key="index">
            <td>{{ index + 1 }}</td>
            <td>{{ program.name }}</td>
          </tr>
        </tbody>
      </table>
      <!-- 저장 버튼 클릭 시 모달 열기 -->
      <button class="save_button" @click="openSaveModal">저장</button>
    </div>

    <!-- 저장 확인 모달 -->
    <ModalConfirm 
      v-if="showSaveModal" 
      @confirm="savePrograms" 
      @close="showSaveModal = false" 
    />
  </div>
</template>

<script>
import ModalConfirm from './ModalConfirm.vue';
import { useUserStore } from '@/stores/userStore'; // Pinia 스토어 가져오기

export default {
  components: {
    ModalConfirm
  },
  data() {
    return {
      programs: [
        { id: 1, name: "SG Client", filePath: "C:\\Users\\82103\\OneDrive\\SG Client.exe", order: '' },
        { id: 2, name: "Chakra", filePath: "C:\\Program Files\\Chakra", order: '' },
        { id: 3, name: "NeoWorks", filePath: "C:\\NeoWorks\\neo.exe", order: '' },
        { id: 4, name: "Slack", filePath: "C:\\Users\\82103\\AppData\\Local\\slack\\slack.exe", order: '' }
      ],
      sortedPrograms: [],
      waitTime: 3, // 기본 실행 대기 시간
      showSaveModal: false // 모달 상태
    };
  },
  computed: {
    user() {
      const userStore = useUserStore();
      return userStore.user; // Pinia에서 로그인된 사용자 정보 가져오기
    }
  },
  methods: {
    validateOrder(program) {
      if (!/^\d*$/.test(program.order) || (program.order !== '' && parseInt(program.order) <= 0)) {
        program.order = '';
        alert('0 이상의 숫자를 입력하세요.');
      }
    },
    hasDuplicateOrders() {
      const orders = this.programs.map(program => program.order).filter(order => order !== '');
      const uniqueOrders = new Set(orders);
      return uniqueOrders.size !== orders.length;
    },
    hasEmptyOrders() {
      return this.programs.some(program => program.order === '');
    },
    hasOrderExceedingProgramCount() {
      const maxOrder = Math.max(...this.programs.map(program => parseInt(program.order) || 0));
      return maxOrder > this.programs.length;
    },
    sortPrograms() {
      if (this.hasEmptyOrders()) {
        alert('순번입력이 완료되지 않았습니다. 확인 후 진행해주세요.');
        return;
      }
      if (this.hasDuplicateOrders()) {
        alert('중복된 순번이 있습니다. 순번을 확인해주세요.');
        return;
      }
      if (this.hasOrderExceedingProgramCount()) {
        alert('순번은 ' + this.programs.length + '이하로 입력해주세요');
        return;
      }
      this.sortedPrograms = this.programs
        .filter(program => program.order !== '' && parseInt(program.order) > 0)
        .sort((a, b) => parseInt(a.order) - parseInt(b.order));
    },
    openSaveModal() {
      if (this.sortedPrograms.length === 0) {
        alert('프로그램 순서를 먼저 확인해주세요.');
        return;
      }
      this.showSaveModal = true;
    },
    savePrograms() {
      let batContent = '@echo off\nsetlocal EnableDelayedExpansion\n';

      this.sortedPrograms.forEach((program, index) => {
        const filePath = program.filePath;
        const fileName = filePath.split("\\").pop();
        const fileExtension = fileName.split(".").pop();

        batContent += `if exist "${filePath}" (\n`;
        batContent += `    start "" "${filePath}"\n`;
        batContent += `    timeout /t ${this.waitTime} /nobreak > nul\n`;

        if (fileExtension === "exe") {
          batContent += `    for /f "tokens=2 delims=," %%i in ('tasklist /FI "IMAGENAME eq ${fileName}" /FO CSV ^| findstr /I "${fileName}"') do (\n`;
          batContent += `        set "pid_${index}=%%~i"\n`;
          batContent += `        set "status_${index}=1"\n`;
          batContent += `    )\n`;
          batContent += `    if defined pid_${index} (\n`;
          batContent += `        echo ${program.name} pid: !pid_${index}!\n`;
          batContent += `    ) else (\n`;
          batContent += `        echo ${program.name} pid: Not found\n`;
          batContent += `    )\n`;
        } else {
          batContent += `    echo ${program.name} is not an executable, skipping PID check.\n`;
          batContent += `    set "status_${index}=1"\n`;
        }
        batContent += `    echo ${program.name} status: !status_${index}!\n`;
        batContent += `) else (\n`;
        batContent += `    echo File not found: "${filePath}"\n`;
        batContent += `    set "status_${index}=0"\n`;
        batContent += `    echo ${program.name} status: !status_${index}!\n`;
        batContent += `    timeout /t ${this.waitTime} /nobreak > nul\n`;
        batContent += `)\n\n`;
      });

      batContent += '\necho All programs started.\npause\n';

      const blob = new Blob([batContent], { type: 'text/plain;charset=utf-8' });
      const url = URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = 'PC세팅자동화.bat';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      URL.revokeObjectURL(url);

      alert('PC세팅 자동화 파일생성이 완료되었습니다.');
    }
  }
};
</script>

<style scoped>
/* 로그인 사용자 정보를 상단에 표시하는 스타일 */
.user-info {
  text-align: right;
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: bold;
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
  font-size: 30px;
  padding: 20px 0;
}

.delay-container label {
  margin-right: 20px;
}

.wait-time-input {
  width: 60px;
  padding: 5px;
  text-align: center;
}

table {
  border-collapse: collapse;
  margin-top: 20px;
  width: 100%;
}

.fixed-table {
  table-layout: fixed;
}

th, td {
  border: 1px solid black;
  padding: 10px;
  text-align: center;
  width: 50%;
}

input {
  width: 50%;
  padding: 5px;
  text-align: center;
}

.header-cell {
  background-color: #fcf4c0;
  color: black;
}

.header-cell2 {
  background-color: #cefbc9;
  color: black;
}

button {
  margin-top: 20px;
  padding: 10px 20px;
  font-size: 16px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.save_button {
  background-color: #FFD700;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

h2 {
  margin-top: 30px;
  font-size: 30px;
  padding: 20px 0;
}
</style>
