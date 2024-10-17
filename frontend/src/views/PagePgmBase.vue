<template>
  <div class="container">
    <!-- 로그인 사용자 정보 -->
    <div class="user-info">
      <p>로그인 사용자: {{ user?.employeeNumber || "정보 없음" }}</p> <!-- 사용자 정보가 없을 때 "정보 없음" 출력 -->
    </div>

    <h1>프로그램 목록</h1>
    
    <!-- 상단에 버튼 추가 -->
    <div class="button-container">
      <button class="reg_button" @click="openModal('register')">등록</button>
      <button class="upd_button" @click="openModal('update')" :disabled="!selectedProgram">수정</button>
      <button class="del_button" @click="openModal('remove')" :disabled="!selectedProgram">해제</button>
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
    <ModalBase 
      v-if="showModal" 
      :modalType="modalType" 
      :program="selectedProgram"
      @close="showModal = false" 
      @register="registerProgram" 
      @update="updateProgram" 
      @remove="removeProgram"
    />
  </div>
</template>

<script>
import { ref } from 'vue';
import { useUserStore } from '@/stores/userStore'; // Pinia 스토어 가져오기
import ModalBase from './ModalBase.vue';

export default {
  components: {
    ModalBase
  },
  setup() {
    // 모달 상태
    const showModal = ref(false);
    const modalType = ref('');

    // 프로그램 목록
    const programs = ref([
      {
        name: "SG Client",
        filePath: "C:\\Users\\82103\\OneDrive\\SG Client.exe",
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
    ]);

    // 단일 선택을 위한 변수
    const selectedProgram = ref(null);

    // Pinia 스토어에서 로그인한 사용자 정보 가져오기
    const userStore = useUserStore();
    const user = userStore.user;

    // 한글 포함 여부 검사 메서드
    const containsKorean = (text) => {
      const koreanRegex = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/;
      return koreanRegex.test(text);
    };

    // 모달 열기 메서드
    const openModal = (type) => {
      modalType.value = type;
      showModal.value = true;
    };

    // 프로그램 등록 메서드
    const registerProgram = () => {
      const name = prompt("프로그램명을 입력하세요");
      const filePath = prompt("실행파일 경로를 입력하세요");

      if (containsKorean(filePath)) {
        alert("실행파일경로는 영문 및 숫자만 가능합니다. 파일경로를 수정 후 재등록해주세요");
        return;
      }

      const newProgram = {
        name,
        filePath,
        registerDate: new Date().toISOString().slice(0, 10),
        updateDate: new Date().toISOString().slice(0, 10)
      };

      if (newProgram.name && newProgram.filePath) {
        programs.value.push(newProgram);
      }
    };

    // 프로그램 수정 메서드
    const updateProgram = () => {
      if (selectedProgram.value) {
        const newName = prompt("새 프로그램명을 입력하세요", selectedProgram.value.name);
        const newFilePath = prompt("새 실행파일 경로를 입력하세요", selectedProgram.value.filePath);

        if (containsKorean(newFilePath)) {
          alert("프로그램경로에 한글이 포함되어있습니다. 영문경로로 수정 후 재입력해주세요.");
          return;
        }

        if (newName && newFilePath) {
          selectedProgram.value.name = newName;
          selectedProgram.value.filePath = newFilePath;
          selectedProgram.value.updateDate = new Date().toISOString().slice(0, 10);
        }
      }
    };

    // 프로그램 삭제 메서드
    const removeProgram = () => {
      if (selectedProgram.value) {
        const confirmDelete = confirm('선택한 프로그램을 해제하시겠습니까?');
        if (confirmDelete) {
          programs.value = programs.value.filter(p => p !== selectedProgram.value);
          selectedProgram.value = null;
        }
      }
    };

    return {
      showModal,
      modalType,
      programs,
      selectedProgram,
      openModal,
      registerProgram,
      updateProgram,
      removeProgram,
      containsKorean,
      user // 로그인 사용자 정보
    };
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

.reg_button, .upd_button, .del_button {
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
