<template>
  <div class="container">
    <!-- 로그인 사용자 정보 -->
    <div class="user-info">
      <p>로그인 사용자: {{ user.employeeNumber || "정보 없음" }}</p>
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

    <!-- 등록 모달 -->
    <div v-if="showModal && modalType === 'register'" class="modal">
      <div class="modal-content">
        <h2>프로그램 등록</h2>
        <table class="program-table">
          <thead>
            <tr>
              <th>프로그램명</th>
              <th>실행파일 경로</th>
              <th>실행 대기시간(초)</th>
              <th>삭제</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(program, index) in newPrograms" :key="index">
              <td><input type="text" v-model="program.pgmNm" placeholder="프로그램명을 입력하세요" required /></td>
              <td><input type="text" v-model="program.filePath" placeholder="파일 경로를 입력하세요" required /></td>
              <td>
                <input
                  type="text"
                  v-model="program.sleepTime"
                  placeholder="3초이상 입력하세요"
                  required
                />
              </td>
              <td><button type="button" @click="removeNewProgram(index)">삭제</button></td>
            </tr>
          </tbody>
        </table>
        <button type="button" @click="addProgram">프로그램 추가</button>
        <button type="button" @click="openConfirmationModal">등록</button>
        <button type="button" @click="closeModal">취소</button>
      </div>
    </div>

    <!-- 확인 모달 -->
    <div v-if="showConfirmationModal" class="modal">
      <div class="modal-content">
        <h2>프로그램 등록 확인</h2>
        <p>정말로 프로그램을 등록하시겠습니까?</p>
        <button type="button" @click="registerMultiplePrograms">확인</button>
        <button type="button" @click="closeConfirmationModal">취소</button>
      </div>
    </div>

    <!-- 수정 및 해제 모달 (ModalBase 사용) -->
    <ModalBase
      v-if="showModal && (modalType === 'update' || modalType === 'remove')"
      :modalType="modalType"
      :program="selectedProgram"
      @close="closeModal"
      @update="submitProgramUpdate"
      @remove="removeSelectedProgram"
    />
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useUserStore } from '@/stores/userStore';
import ModalBase from './ModalBase.vue';
import axios from 'axios';

export default {
  components: { ModalBase },
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
    const closeModal = () => {
      showModal.value = false;
      newPrograms.value = [{ pgmId: '', pgmNm: '', filePath: '', sleepTime: '' }];
    };
    const openConfirmationModal = () => { showConfirmationModal.value = true; };
    const closeConfirmationModal = () => { showConfirmationModal.value = false; };

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

    const registerMultiplePrograms = async () => {
    try {
      const englishOnlyRegex = /^[A-Za-z0-9_ ]*$/;
      const filePathRegex = /^[A-Za-z0-9_/\\:.-]*$/; // Allows English letters, numbers, and certain special characters

      for (const program of newPrograms.value) {
        // Check for any empty fields
        if (!program.pgmNm || !program.filePath || !program.sleepTime) {
          alert('입력되지 않은 항목이 존재합니다. 확인 후 다시 등록해주세요');
          return;
        }

        // Additional validation checks
        if (program.sleepTime < 3) {
          alert('대기시간은 3초 이상 입력해야 합니다.');
          return;
        }
        if (!englishOnlyRegex.test(program.pgmNm)) {
          alert('프로그램명은 영문만 가능합니다. 확인 후 다시 등록해주세요.');
          return;
        }
        if (!filePathRegex.test(program.filePath)) {
          alert('실행경로는 영문만 가능합니다. 확인 후 다시 등록해주세요.');
          return;
        }

        const programDto = {
          pgmNm: program.pgmNm,
          empNo: user.employeeNumber,
          filePath: program.filePath,
          sleepTime: program.sleepTime
        };
        await axios.post('/api/program/register', programDto);
      }

        fetchPrograms();
        closeModal();
        closeConfirmationModal();
      } catch (error) {
        console.error("Error registering programs:", error);
      }
    };

    const submitProgramUpdate = async (updatedProgram) => {
      try {
        updatedProgram.empNo = user.employeeNumber;
        updatedProgram.pgmId = selectedProgram.value.pgmId;

        await axios.post('/api/program/modify', updatedProgram);
        
        fetchPrograms();
        closeModal();
      } catch (error) {
        console.error("Error updating program:", error);
      }
    };

    const addProgram = () => { newPrograms.value.push({ pgmId: '', pgmNm: '', filePath: '', sleepTime: '' }); };
    const removeNewProgram = (index) => { newPrograms.value.splice(index, 1); };

    const removeSelectedProgram = async () => {
      if (selectedProgram.value) {
          try {
            const programDto = {
              pgmId: selectedProgram.value.pgmId,
              empNo: user.employeeNumber
           };
            await axios.post('/api/program/delete', programDto);
            programs.value = programs.value.filter(p => p !== selectedProgram.value);
            selectedProgram.value = null;
         } catch (error) {
            console.error("Error deleting program:", error);
        }
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
      closeModal,
      openConfirmationModal,
      closeConfirmationModal,
      addProgram,
      removeNewProgram,
      registerMultiplePrograms,
      submitProgramUpdate,
      removeSelectedProgram,
      fetchPrograms
    };
  }
};
</script>


<style scoped>
.user-info {
  text-align: right;
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: bold;
}

.container {
  width: 80%; /* Change from 60% to 80% or use a fixed width like 1200px */
  max-width: 1200px; /* Increase the max width if necessary */
  margin: 0 auto;
  padding: 80px 0;
  font-family: 'KCCMurukmuruk', sans-serif;
}

h1 {
  text-align: center;
  padding: 20px 0;
}

table thead th {
  background-color: #fcf4c0; /* Light purple color for headers */
  color: black; /* Black text for better contrast */
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
  background-color: #F9F9F9; /* Alternating row colors for better readability */
}

tbody tr:nth-child(even) {
  background-color: #ECECEC; /* Slightly darker alternating row */
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
