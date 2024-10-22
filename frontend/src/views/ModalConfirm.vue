<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal-content">
      <h2>저장 확인</h2>
      <p>설정한 순서대로 프로그램 실행 파일을 생성하시겠습니까?</p>
      <div class="modal-buttons">
        <button @click="confirmSave">확인</button>
        <button @click="closeModal">취소</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    confirmSave() {
      this.$emit('confirm');
      this.closeModal();
    },
    closeModal() {
      this.$emit('close');
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
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  width: 300px;
  text-align: center;
}

.modal-buttons {
  margin-top: 20px;
}

button {
  margin: 0 10px;
  padding: 10px;
}
</style>
