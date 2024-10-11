<template>
  <div class="container">
    <h1>프로그램 목록</h1>
    
    <!-- 상단에 버튼 추가 -->
    <div class="button-container">
      <button class="reg_button" @click="registerProgram">등록</button>
      <button class="upd_button" @click="updateProgram" :disabled="!selectedProgram">수정</button>
      <button class="del_button" @click="removeProgram" :disabled="!selectedProgram">해제</button>
    </div>

    <table>
      <thead>
        <tr>
          <th>선택</th>
          <th>프로그램명</th>
          <th>실행파일 경로</th>
          <th>등록일</th>
          <th>수정일</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(program, index) in programs" :key="index">
          <td>
            <input 
              type="radio" 
              v-model="selectedProgram" 
              :value="program" 
            />
          </td>
          <td>{{ program.name }}</td>
          <td>{{ program.filePath }}</td>
          <td>{{ program.registerDate }}</td>
          <td>{{ program.updateDate }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 등록된 프로그램 목록
      programs: [
        {
          name: "SG Client",
          filePath: "C:\\Users\\82103\\OneDrive\\바탕 화면",
          registerDate: "2024-09-27",
          updateDate: "2024-09-27"
        },
        {
          name: "Chakra",
          filePath: "C:\\Program Files\\Chakra",
          registerDate: "2024-09-01",
          updateDate: "2024-09-01"
        },
        {
          name: "NeoWorks",
          filePath: "C:\\NeoWorks\\neo.exe",
          registerDate: "2024-09-27",
          updateDate: "2024-09-27"
        },
        {
          name: "Slack",
          filePath: "C:\\Users\\82103\\AppData\\Local\\slack\\app-4.40.128",
          registerDate: "2024-09-27",
          updateDate: "2024-09-27"
        }
      ],
      // 단일 선택을 위한 변수
      selectedProgram: null
    };
  },
  methods: {
    // 프로그램 등록 메서드
    registerProgram() {
      const newProgram = {
        name: prompt("프로그램명을 입력하세요"),
        filePath: prompt("실행파일 경로를 입력하세요"),
        registerDate: new Date().toISOString().slice(0, 10),
        updateDate: new Date().toISOString().slice(0, 10)
      };
      if (newProgram.name && newProgram.filePath) {
        this.programs.push(newProgram);
      }
    },

    // 프로그램 수정 메서드
    updateProgram() {
      if (this.selectedProgram) {
        const newName = prompt("새 프로그램명을 입력하세요", this.selectedProgram.name);
        const newFilePath = prompt("새 실행파일 경로를 입력하세요", this.selectedProgram.filePath);
        if (newName && newFilePath) {
          this.selectedProgram.name = newName;
          this.selectedProgram.filePath = newFilePath;
          this.selectedProgram.updateDate = new Date().toISOString().slice(0, 10);
        }
      }
    },

    // 프로그램 삭제 메서드
    removeProgram() {
      if (this.selectedProgram) {
        // 사용자에게 해제 여부를 확인하는 메시지 추가
        const confirmDelete = confirm('선택한 프로그램을 해제하시겠습니까?');
        if (confirmDelete) {
          this.programs = this.programs.filter(p => p !== this.selectedProgram);
          this.selectedProgram = null;
        }
      }
    }
  }
};
</script>

<style scoped>

@font-face {
  font-family: 'KCCMurukmuruk';
  src: url('@/fonts/KCCMurukmuruk.ttf') format('truetype');
}

.container {  
  width: 60%; /* 너비를 60%로 설정 */
  max-width: 800px; /* 최대 너비를 800px로 제한 */
  margin: 0 auto; /* 가로 중앙 정렬 */
  padding: 80px 0; /* 상하 40px 패딩, 좌우 패딩은 0 */
  font-family: 'KCCMurukmuruk', sans-serif;
}

h1 {
  text-align: center;
  padding: 20px 0; /* 상하 20px 패딩, 좌우 패딩은 0 */
}

.button-container {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

button {
  margin: 0 10px;
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
}

button:disabled {
  background-color: grey;
  cursor: not-allowed;
}

.reg_button{
  font-family: 'KCCMurukmuruk', sans-serif;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 그림자 추가 */
}

.upd_button{
  font-family: 'KCCMurukmuruk', sans-serif;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 그림자 추가 */
}

.del_button{
  font-family: 'KCCMurukmuruk', sans-serif;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 그림자 추가 */
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
