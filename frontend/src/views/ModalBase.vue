<template>
  <div class="modal-overlay">
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
            <td><input type="text" v-model="programName" placeholder="프로그램명 입력" @input="validateProgramName" /></td>
            <td><input type="text" v-model="filePath" placeholder="실행파일 경로 입력" @input="validateFilePath" /></td>
            <td><input type="text" v-model="sleepTime" placeholder="대기시간(초)" @blur="validateSleepTime" /></td>
          </tr>
          <tr v-if="modalType === 'remove'">
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
      programName: this.program ? this.program.pgmNm : '',
      filePath: this.program ? this.program.filePath : '',
      sleepTime: this.program ? this.program.sleepTime : ''
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
    },
    validateSleepTime() {
      const sleepTimeNumber = parseInt(this.sleepTime, 10);
      if (isNaN(sleepTimeNumber) || sleepTimeNumber < 3) {
        alert("실행대기시간은 3초 이상 입력 가능합니다.");
        this.sleepTime = "3";
      } else if (sleepTimeNumber > 60) {
        alert("실행대기시간은 60초 이내로 입력 가능합니다.");
        this.sleepTime = "60";
      }
    },
    validateProgramName() {
      if (/\s/.test(this.programName)) {
        alert('프로그램명은 공백없이 입력해야합니다.');
        this.programName = this.programName.replace(/\s/g, '');
      } else {
        const allowedChars = /^[A-Za-z0-9_]*$/;
        if (!allowedChars.test(this.programName)) {
          alert("프로그램명은 영문, 숫자, 언더스코어(_)만 가능합니다.");
          this.programName = this.programName.replace(/[^A-Za-z0-9_]/g, '');
        }
      }
    },
    validateFilePath() {
      if (/\s/.test(this.filePath)) {
        alert('실행 경로는 공백없이 입력해야합니다.');
        this.filePath = this.filePath.replace(/\s/g, '');
      } else {
        const allowedChars = /^[A-Za-z0-9_/\\:.-]*$/;
        if (!allowedChars.test(this.filePath)) {
          alert("실행파일 경로는 영문, 숫자, /, \\, :, ., -만 입력 가능합니다.");
          this.filePath = this.filePath.replace(/[^A-Za-z0-9_/\\:.-]/g, '');
        }
      }
    }
  },
  watch: {
    program: {
      immediate: true,
      handler(newProgram) {
        if (newProgram) {
          this.programName = newProgram.pgmNm || '';
          this.filePath = newProgram.filePath || '';
          this.sleepTime = newProgram.sleepTime || '';
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

.program-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.program-table th {
  background-color: #FCF4C0;
  color: black;
  padding: 10px;
}

.program-table th, .program-table td {
  border: 1px solid black;
  padding: 10px;
  text-align: center;
}

tbody tr:nth-child(odd) {
  background-color: #F9F9F9;
}

tbody tr:nth-child(even) {
  background-color: #ECECEC;
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
  font-family: 'KCCMurukmuruk', sans-serif; /* 폰트 적용 */
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
