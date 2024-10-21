<template>
  <div class="container">
    <!-- 로그인 사용자 정보 -->
    <div class="user-info">
      <p>로그인 사용자: {{ user?.employeeNumber || "정보 없음" }}</p> <!-- 사용자 정보가 없을 때 "정보 없음" 출력 -->
    </div>

    <h1>프로그램 목록</h1>

    <!-- 상단에 등록, 수정, 해제 버튼 추가 -->
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

    <!-- 등록 모달 -->
    <div v-if="showModal && modalType === 'register'" class="modal">
      <div class="modal-content">
        <h2>프로그램 등록</h2>

        <!-- 프로그램 목록 입력 폼 -->
        <table class="program-table">
          <thead>
            <tr>
              <th>프로그램명</th>
              <th>실행파일 경로</th>
              <th>삭제</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(program, index) in newPrograms" :key="index">
              <td>
                <input type="text" v-model="program.name" placeholder="프로그램명을 입력하세요" required />
              </td>
              <td>
                <input type="text" v-model="program.filePath" placeholder="파일 경로를 입력하세요" required />
              </td>
              <td>
                <button type="button" @click="removeNewProgram(index)">삭제</button>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- 프로그램 추가 버튼 -->
        <button type="button" @click="addProgram">프로그램 추가</button>

        <!-- 등록 버튼 (모달로 연결) -->
        <button type="button" @click="openConfirmationModal">등록</button>
        <!-- 취소 버튼 -->
        <button type="button" @click="closeModal">취소</button>
      </div>
    </div>

    <!-- 두 번째 확인 모달 (등록 확인 모달) -->
    <div v-if="showConfirmationModal" class="modal">
      <div class="modal-content">
        <h2>프로그램 등록 확인</h2>
        <p>정말로 프로그램을 등록하시겠습니까?</p>
        <button type="button" @click="registerMultiplePrograms">확인</button>
        <button type="button" @click="closeConfirmationModal">취소</button>
      </div>
    </div>

    <!-- 수정 모달 -->
    <ModalBase
      v-if="showModal && modalType === 'update'"
      :modalType="modalType"
      :program="selectedProgram"
      @close="closeModal"
      @update="updateProgram"
    />

    <!-- 해제 모달 -->
    <ModalBase
      v-if="showModal && modalType === 'remove'"
      :modalType="modalType"
      :program="selectedProgram"
      @close="closeModal"
      @remove="removeSelectedProgram"
    />
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useUserStore } from '@/stores/userStore'; // Pinia 스토어 가져오기
import ModalBase from './ModalBase.vue';
import axios from 'axios'; // API 호출을 위한 axios

export default {
  components: {
    ModalBase
  },
  setup() {
    const showModal = ref(false); // 첫 번째 모달 표시 상태
    const showConfirmationModal = ref(false); // 두 번째 확인 모달 상태
    const modalType = ref(''); // 모달 유형 ('register', 'update', 'remove')

    // 프로그램 목록
    const programs = ref([]);
    const selectedProgram = ref(null); // 선택된 프로그램

    // 새로 등록할 프로그램 목록
    const newPrograms = ref([{ name: '', filePath: '' }]);

    // Pinia 스토어에서 로그인한 사용자 정보 가져오기
    const userStore = useUserStore();
    const user = userStore.user;

    // 첫 번째 모달 열기 메서드
    const openModal = (type) => {
      modalType.value = type;
      showModal.value = true;
    };

    // 첫 번째 모달 닫기 메서드
    const closeModal = () => {
      showModal.value = false;
      newPrograms.value = [{ name: '', filePath: '' }]; // 폼 초기화
    };

    // 두 번째 확인 모달 열기 메서드
    const openConfirmationModal = () => {
      showConfirmationModal.value = true;
    };

    // 두 번째 확인 모달 닫기 메서드
    const closeConfirmationModal = () => {
      showConfirmationModal.value = false;
    };

    // 프로그램 목록 API로부터 가져오기
    const fetchPrograms = async () => {
      try {
        const response = await axios.get('/api/programs'); // 프로그램 목록을 API로부터 가져옴
        programs.value = response.data; // 응답 데이터를 programs 배열에 저장
      } catch (error) {
        console.error("프로그램 목록을 불러오는 중 오류 발생:", error);
      }
    };

    // 새 프로그램 추가 필드 생성 메서드
    const addProgram = () => {
      newPrograms.value.push({ name: '', filePath: '' });
    };

    // 특정 새 프로그램 입력 필드를 삭제하는 메서드
    const removeNewProgram = (index) => {
      newPrograms.value.splice(index, 1);
    };

    // 프로그램 등록 메서드 (API로 등록 요청)
    const registerMultiplePrograms = async () => {
      try {
        // 각 프로그램을 서버에 등록
        for (const program of newPrograms.value) {
          if (!program.name || !program.filePath) {
            alert('모든 프로그램명과 파일 경로를 입력해주세요.');
            return;
          }
          await axios.post('/api/programs', program);
        }
        alert('프로그램이 성공적으로 등록되었습니다.');
        fetchPrograms(); // 프로그램 목록 갱신
        closeModal(); // 첫 번째 모달 닫기
        closeConfirmationModal(); // 두 번째 모달 닫기
      } catch (error) {
        console.error("프로그램 등록 중 오류 발생:", error);
      }
    };

    // 선택된 프로그램 삭제 메서드 (API로 삭제 요청)
    const removeSelectedProgram = async () => {
      if (selectedProgram.value) {
        const confirmDelete = confirm('선택한 프로그램을 해제하시겠습니까?');
        if (confirmDelete) {
          try {
            // 프로그램 삭제 API 요청
            await axios.delete(`/api/programs/${selectedProgram.value.id}`);
            // 삭제된 프로그램을 목록에서 제거
            programs.value = programs.value.filter(p => p !== selectedProgram.value);
            selectedProgram.value = null;
          } catch (error) {
            console.error("프로그램 삭제 중 오류 발생:", error);
          }
        }
      }
    };

    // 프로그램 수정 메서드 (API로 수정 요청)
    const updateProgram = async () => {
      if (selectedProgram.value) {
        const newName = prompt("새 프로그램명을 입력하세요", selectedProgram.value.name);
        const newFilePath = prompt("새 실행파일 경로를 입력하세요", selectedProgram.value.filePath);

        const updatedProgram = {
          ...selectedProgram.value,
          name: newName,
          filePath: newFilePath,
          updateDate: new Date().toISOString().slice(0, 10)
        };

        try {
          // 프로그램 수정 API 요청
          await axios.put(`/api/programs/${selectedProgram.value.id}`, updatedProgram);
          // 수정된 내용 반영
          selectedProgram.value.name = updatedProgram.name;
          selectedProgram.value.filePath = updatedProgram.filePath;
          selectedProgram.value.updateDate = updatedProgram.updateDate;
        } catch (error) {
          console.error("프로그램 수정 중 오류 발생:", error);
        }
      }
    };

    // 페이지가 로드될 때 프로그램 목록을 가져옴
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
      openModal,
      closeModal,
      openConfirmationModal,
      closeConfirmationModal,
      addProgram,
      removeNewProgram,
      registerMultiplePrograms,
      updateProgram,
      removeSelectedProgram, // 함수 이름 변경
      fetchPrograms,
      user
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
  width: 60%;
  max-width: 800px;
  margin: 0 auto;
  padding: 80px 0;
  font-family: 'KCCMurukmuruk', sans-serif;
}

h1 {
  text-align: center;
  padding: 20px 0;
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

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: rgba(0, 0, 0, 0.5);
}

.modal-content {
  background: white;
  padding: 30px;
  border-radius: 8px;
  width: 800px;
  max-width: 90%;
  min-height: 400px;
}

/* 표 스타일 */
.program-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.program-table th, .program-table td {
  border: 1px solid #ccc;
  padding: 10px;
  text-align: center;
}

.button-container {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

button {
  font-family: 'KCCMurukmuruk', sans-serif; /* 버튼 글씨체 유지 */
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
  border: none;
  border-radius: 4px;
  background-color: #4CAF50; /* 기본 버튼 배경색 */
  color: white;
  margin-left: 10px; /* 각 버튼 사이의 간격 */
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
  cursor: not-allowed;
}
</style>
