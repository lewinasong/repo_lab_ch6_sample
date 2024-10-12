<template>
  <div class="container">
    <h1>프로그램 실행순서 설정</h1>

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
      <h2>정렬된 프로그램 목록</h2>
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
      <!-- 저장 버튼 추가 -->
      <button class="save_button" @click="confirmSave">저장</button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 프로그램 목록
      programs: [
        { id: 1, name: "SG Client", order: '' },
        { id: 2, name: "Chakra", order: '' },
        { id: 3, name: "NeoWorks", order: '' },
        { id: 4, name: "Slack", order: '' }
      ],
      sortedPrograms: []
    };
  },
  methods: {
    // 순번 유효성 검사 (0 이상의 숫자인지 확인)
    validateOrder(program) {
      if (!/^\d*$/.test(program.order) || (program.order !== '' && parseInt(program.order) <= 0)) {
        program.order = '';
        alert('0 이상의 숫자를 입력하세요.');
      }
    },
    // 중복된 순번이 있는지 확인하는 메서드
    hasDuplicateOrders() {
      const orders = this.programs.map(program => program.order).filter(order => order !== '');
      const uniqueOrders = new Set(orders);
      return uniqueOrders.size !== orders.length;
    },
    // 순번이 입력되지 않은 프로그램이 있는지 확인하는 메서드
    hasEmptyOrders() {
      return this.programs.some(program => program.order === '');
    },
    // 입력된 순번 중 프로그램 수보다 큰 값이 있는지 확인하는 메서드
    hasOrderExceedingProgramCount() {
      const maxOrder = Math.max(...this.programs.map(program => parseInt(program.order) || 0));
      return maxOrder > this.programs.length;
    },
    // 입력된 순번에 따라 프로그램 목록 정렬
    sortPrograms() {
      // 순번 입력 완료 여부 체크
      if (this.hasEmptyOrders()) {
        alert('순번입력이 완료되지 않았습니다. 확인 후 진행해주세요.');
        return;
      }

      // 중복된 순번 체크
      if (this.hasDuplicateOrders()) {
        alert('중복된 순번이 있습니다. 순번을 확인해주세요.');
        return;
      }

      // 프로그램 수보다 순번이 큰지 체크
      if (this.hasOrderExceedingProgramCount()) {
        alert('순번은 ' + this.programs.length + '이하로 입력해주세요');
        return;
      }

      // 순번이 유효한 프로그램만 필터링하여 정렬
      this.sortedPrograms = this.programs
        .filter(program => program.order !== '' && parseInt(program.order) > 0)
        .sort((a, b) => parseInt(a.order) - parseInt(b.order));
    },
    // 저장 버튼 클릭 시 확인 메시지
    confirmSave() {
      const confirmation = confirm('설정한 순서로 실행파일이 생성됩니다. 계속하시겠습니까?');
      if (confirmation) {
        this.savePrograms();
      } else {
        alert('작업이 취소되었습니다.');
      }
    },
    // .bat 파일 생성
    savePrograms() {
      // .bat 파일의 내용을 작성
      let batContent = '@echo off\nchcp 65001 > nul\n\necho Starting all programs...\n\n';

      // 예시 프로그램 실행 명령어 추가
      batContent += 'start "" "C:\\SET_PC\\Visual Studio Code.lnk"\n';
      batContent += 'timeout /t 3 /nobreak > nul\n';
      batContent += 'start "" "C:\\SET_PC\\Git Bash.lnk"\n';
      batContent += 'timeout /t 3 /nobreak > nul\n';
      batContent += 'start "" "C:\\SET_PC\\Slack.lnk"\n';

      batContent += '\necho All programs started.\npause\n';

      // UTF-8 BOM 추가
      const utf8Bom = '\uFEFF';

      // Blob을 생성하여 .bat 파일로 저장 (UTF-8 BOM 포함)
      const blob = new Blob([utf8Bom + batContent], { type: 'text/plain;charset=utf-8' });
      const url = URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = 'start_programs.bat';
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
/* 스타일 설정은 이전과 동일 */
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
  font-size: 30px;
  padding: 20px 0;
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
  font-family: 'KCCMurukmuruk', sans-serif;
}

.header-cell2 {
  background-color: #cefbc9;
  color: black;
  font-family: 'KCCMurukmuruk', sans-serif;
}

button {
  margin-top: 20px;
  padding: 10px 20px;
  font-size: 16px;
  font-family: 'KCCMurukmuruk', sans-serif;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.save_button {
  background-color: #FFD700;
  font-family: 'KCCMurukmuruk', sans-serif;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

h2 {
  margin-top: 30px;
  font-size: 30px;
  padding: 20px 0;
}

.sorted-table {
  margin-top: 20px;
}
</style>
