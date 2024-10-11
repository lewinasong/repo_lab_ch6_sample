<template>
    <div class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <h2>모달 제목</h2>
        <p>모달 내용이 여기에 들어갑니다.</p>
        <button @click="closeModal">닫기</button>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'ModalSample',
    emits: ['close'],
    setup(props, { emit }) {
      const closeModal = () => {
        emit('close');
      };
  
      return {
        closeModal,
      };
    },
  };
  </script>
  
  <style scoped>
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5); /* 반투명 배경 */
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }
  
  .modal-content {
    background-color: #fff;
    padding: 20px;
    border-radius: 5px;
    max-width: 500px;
    width: 100%;
  }
  </style>