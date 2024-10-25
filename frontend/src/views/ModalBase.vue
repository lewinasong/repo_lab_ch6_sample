<template>
  <div class="modal-overlay" @click.self="close">
    <div class="modal-content">
      <h2>{{ modalTitle }}</h2>

      <table class="program-table">
        <colgroup>
          <col style="width: 40%;">
          <col style="width: 40%;">
          <col style="width: 20%;">
        </colgroup>
        <thead>
          <tr>
            <th>프로그램명</th>
            <th>실행파일 경로</th>
            <th>실행 대기시간(초)</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="modalType === 'register' || modalType === 'update'">
            <td><input type="text" v-model="programName" placeholder="프로그램명 입력" /></td>
            <td><input type="text" v-model="filePath" placeholder="실행파일 경로 입력" /></td>
            <td><input type="number" v-model="sleepTime" placeholder="대기시간(초)" /></td>
          </tr>
          <tr v-if="modalType === 'remove'">
            <!-- 선택한 프로그램 정보를 표시합니다. -->
            <td>{{ programName }}</td>
            <td>{{ filePath }}</td>
            <td>{{ sleepTime }} 초</td>
          </tr>
        </tbody>
      </table>

      <div v-if="modalType === 'remove'">
        <p>선택한 프로그램을 해제하시겠습니까?</p>
      </div>

      <div class="modal-buttons">
        <button @click="confirmAction">확인</button>
        <button @click="close">취소</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    modalType: String,
    program: Object
  },
  data() {
    return {
      programName: this.program ? this.program.pgmNm : '', // 프로그램명이 보이도록 수정
      filePath: this.program ? this.program.filePath : '',
      sleepTime: this.program ? this.program.sleepTime : 0,
    };
  },
  computed: {
    modalTitle() {
      switch (this.modalType) {
        case 'register':
          return '프로그램 등록';
        case 'update':
          return '프로그램 수정';
        case 'remove':
          return '프로그램 해제';
        default:
          return '';
      }
    }
  },
  methods: {
    confirmAction() {
      if (this.modalType === 'register') {
        this.$emit('register', { pgmNm: this.programName, filePath: this.filePath, sleepTime: this.sleepTime });
      } else if (this.modalType === 'update') {
        this.$emit('update', { pgmNm: this.programName, filePath: this.filePath, sleepTime: this.sleepTime });
      } else if (this.modalType === 'remove') {
        this.$emit('remove');
      }
      this.close();
    },
    close() {
      this.$emit('close');
    }
  },
  watch: {
    // 모달이 열릴 때마다 프로그램 데이터가 새로 반영되도록 설정
    program: {
      immediate: true,
      handler(newProgram) {
        if (newProgram) {
          this.programName = newProgram.pgmNm || '';
          this.filePath = newProgram.filePath || '';
          this.sleepTime = newProgram.sleepTime || 0;
        }
      }
    }
  }
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: #ffffff;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  width: 800px;
  max-width: 90%;
  min-height: 400px;
}

h2 {
  margin-bottom: 20px;
  font-size: 1.5em;
  color: #333333;
}

/* Apply the same table styling from the main table */
.program-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.program-table th {
  background-color: #C1B6E6; /* Light purple color for headers */
  color: black; /* Black text for better contrast */
  padding: 10px;
}

.program-table th, .program-table td {
  border: 1px solid black;
  padding: 10px;
  text-align: center;
}

tbody tr:nth-child(odd) {
  background-color: #F9F9F9; /* Alternating row colors */
}

tbody tr:nth-child(even) {
  background-color: #ECECEC; /* Slightly darker alternating row */
}

.modal-buttons {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 20px;
}

button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  background-color: #4CAF50;
  color: white;
  font-size: 16px;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}

button.cancel {
  background-color: #f44336;
}

button.cancel:hover {
  background-color: #d32f2f;
}
</style>
