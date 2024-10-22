<template>
  <div class="modal-overlay" @click.self="close">
    <div class="modal-content">
      <h2>{{ modalTitle }}</h2>
      <div v-if="modalType === 'register'">
        <input type="text" v-model="programName" placeholder="프로그램명 입력" />
        <input type="text" v-model="filePath" placeholder="실행파일 경로 입력" />
      </div>
      <div v-if="modalType === 'update'">
        <input type="text" v-model="programName" placeholder="프로그램명 입력" />
        <input type="text" v-model="filePath" placeholder="실행파일 경로 입력" />
      </div>
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
      programName: this.program ? this.program.name : '',
      filePath: this.program ? this.program.filePath : '',
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
        this.$emit('register', { name: this.programName, filePath: this.filePath });
      } else if (this.modalType === 'update') {
        this.$emit('update', { name: this.programName, filePath: this.filePath });
      } else if (this.modalType === 'remove') {
        this.$emit('remove');
      }
      this.close();
    },
    close() {
      this.$emit('close');
    }
  }
};
</script>

<style scoped>
/* 모달 배경을 어둡게 하고 화면 중앙에 정렬하는 스타일 */
.modal-overlay {
  position: fixed; /* 화면에 고정 */
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5); /* 반투명한 검은 배경 */
  display: flex; /* 중앙 정렬을 위한 flexbox */
  justify-content: center; /* 가로 중앙 정렬 */
  align-items: center; /* 세로 중앙 정렬 */
}

/* 모달 박스의 스타일 */
.modal-content {
  background-color: #ffffff; /* 흰색 배경 */
  padding: 30px; /* 모달 안쪽 여백 */
  border-radius: 12px; /* 모서리를 둥글게 처리 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); /* 살짝의 그림자 효과 */
  width: 400px; /* 모달 너비 */
  text-align: center; /* 텍스트 중앙 정렬 */
  border: 2px solid #e0e0e0; /* 연한 테두리 */
}

/* 모달 제목 스타일 */
h2 {
  margin-bottom: 20px; /* 아래쪽 여백 */
  font-size: 1.5em; /* 글자 크기 */
  color: #333333; /* 어두운 회색 글자색 */
}

/* 입력 필드 스타일 */
.input-field {
  width: 100%; /* 입력 필드가 모달의 전체 너비를 차지 */
  padding: 20px; /* 입력 필드 안쪽 여백 */
  margin-bottom: 15px; /* 입력 필드 아래쪽 여백 */
  border: 1px solid #ddd; /* 연한 테두리 */
  border-radius: 4px; /* 입력 필드 모서리 둥글게 */
  font-size: 20px; /* 글자 크기 */
  box-sizing: border-box; /* padding과 border 포함한 전체 크기 설정 */
}

/* 모달 버튼 컨테이너: 버튼 사이에 간격을 주는 flexbox 스타일 */
.modal-buttons {
  display: flex; /* 버튼을 가로로 나열 */
  justify-content: center; /* 중앙 정렬 */
  gap: 10px; /* 버튼 사이의 간격 */
  margin-top: 20px; /* 버튼 위쪽 여백 */
}

/* 기본 모달 버튼 스타일 */
.modal-button {
  padding: 10px 20px; /* 버튼 안쪽 여백 */
  border: none; /* 버튼 테두리 제거 */
  border-radius: 4px; /* 모서리를 둥글게 처리 */
  background-color: #4CAF50; /* 기본 버튼 색상 (초록) */
  color: white; /* 버튼 텍스트 색상 */
  font-size: 16px; /* 글자 크기 */
  cursor: pointer; /* 마우스 포인터가 버튼 위에 있을 때 손 모양으로 변경 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 살짝의 그림자 효과 */
}

/* 기본 버튼 hover 시 색상 변경 */
.modal-button:hover {
  background-color: #45a049; /* 초록색 버튼이 더 어두워짐 */
}

/* 취소 버튼 스타일 */
.modal-button.cancel {
  background-color: #f44336; /* 빨간색 버튼 */
}

/* 취소 버튼 hover 시 색상 변경 */
.modal-button.cancel:hover {
  background-color: #d32f2f; /* hover 시 더 어두운 빨간색 */
}
</style>