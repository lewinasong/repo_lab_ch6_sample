<template>
  <div class="container">
    <!-- 로그인 사용자 정보 -->
    <div class="user-info">
      <p>로그인 사용자: {{ user.name ? user.name + " (" + user.employeeNumber + ")" : "정보 없음" }}</p>
    </div>

    <h1>프로그램 목록</h1>

    <!-- 상단에 등록, 수정, 해제 버튼 추가 -->
    <div class="button-container">
      <button class="reg_button" @click="openModal('register')">등록</button>
      <button class="upd_button" @click="openModal('update')" :disabled="!selectedProgram">수정</button>
      <button class="del_button" @click="openModal('remove')" :disabled="!selectedProgram">해제</button>
    </div>

    <table>
      <colgroup>
        <col style="width: 10%;"> <!-- First column: 선택 -->
        <col style="width: 20%;"> <!-- Second column: 프로그램명 -->
        <col style="width: 30%;"> <!-- Third column: 실행파일 경로 -->
        <col style="width: 15%;"> <!-- Fourth column: 실행 대기시간 -->
        <col style="width: 10%;"> <!-- Fifth column: 등록일 -->
        <col style="width: 15%;"> <!-- Sixth column: 수정일 -->
      </colgroup>
      <thead>
        <tr>
          <th>선택</th>
          <th>프로그램명</th>
          <th>실행파일 경로</th>
          <th>실행 대기시간(초)</th>
          <th>등록일</th>
          <th>수정일</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(program, index) in programs" :key="index">
          <td><input type="radio" v-model="selectedProgram" :value="program" /></td>
          <td>{{ program.pgmNm }}</td>
          <td>{{ program.filePath }}</td>
          <td>{{ program.sleepTime }} 초</td>
          <td>{{ program.sysRegDtm }}</td>
          <td>{{ program.sysUpdDtm }}</td>
        </tr>
      </tbody>
    </table>

    <!-- 등록 모달 및 기타 모달 코드 생략 -->
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useUserStore } from '@/stores/userStore';
import axios from 'axios';

export default {
  setup() {
    const showModal = ref(false);
    const showConfirmationModal = ref(false);
    const modalType = ref('');
    const programs = ref([]);
    const selectedProgram = ref(null);
    const newPrograms = ref([{ pgmId: '', pgmNm: '', filePath: '', sleepTime: '' }]);

    const userStore = useUserStore();
    const user = userStore;

    const openModal = (type) => {
      modalType.value = type;
      showModal.value = true;
    };

    const fetchPrograms = async () => {
      try {
        if (user.employeeNumber) {
          const response = await axios.get(`/api/program/PagePgmBase/${user.employeeNumber}`);
          programs.value = response.data;
        }
      } catch (error) {
        console.error("Error fetching program list:", error);
      }
    };

    onMounted(() => {
      fetchPrograms();
    });

    return {
      showModal,
      showConfirmationModal,
      modalType,
      programs,
      selectedProgram,
      newPrograms,
      user,
      openModal,
      fetchPrograms,
    };
  }
};
</script>

<style scoped>
.container {
  width: 80%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 80px 0;
  font-family: 'KCCMurukmuruk', sans-serif;
}

/* 로그인 사용자 정보 스타일 */
.user-info {
  text-align: left;
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: bold;
}

h1 {
  text-align: center;
  padding: 20px 0;
}

table thead th {
  background-color: #fcf4c0;
  color: black;
  padding: 10px;
}

table {
  border-collapse: collapse;
  width: 100%;
}

table, th, td {
  border: 1px solid black;
}

th, td {
  padding: 8px;
  text-align: center;
}

tbody tr:nth-child(odd) {
  background-color: #F9F9F9;
}

tbody tr:nth-child(even) {
  background-color: #ECECEC;
}

.button-container {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

button {
  font-family: 'KCCMurukmuruk', sans-serif;
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
  border: none;
  border-radius: 4px;
  background-color: #4CAF50;
  color: white;
  margin-left: 10px;
}

button:first-child {
  margin-left: 0;
}

button:hover {
  background-color: #45a049;
}

button:disabled {
  background-color: grey;
  cursor: not-allowed;
}

button:disabled:hover {
  background-color: grey;
}
</style>
