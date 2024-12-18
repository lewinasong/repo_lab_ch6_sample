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
        <col style="width: 10%;">
        <col style="width: 20%;">
        <col style="width: 30%;">
        <col style="width: 15%;">
        <col style="width: 12.5%;">
        <col style="width: 12.5%;">
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
          <td>{{ formatDate(program.sysRegDtm) }}</td>
          <td>{{ formatDate(program.sysUpdDtm) }}</td>
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
              <td><input type="text" v-model="program.pgmNm" placeholder="프로그램명을 입력하세요" required @input="validateProgramName(program)" /></td>
              <td><input type="text" v-model="program.filePath" placeholder="파일 경로를 입력하세요" required @input="validateFilePath(program)" /></td>
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
        <button type="button" @click="registerMultiplePrograms">등록</button>
        <button type="button" @click="closeModal">취소</button>
      </div>
    </div>

    <!-- 수정 및 해제 모달 -->
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
    const modalType = ref('');
    const programs = ref([]);
    const selectedProgram = ref(null);
    const newPrograms = ref([{ pgmId: '', pgmNm: '', filePath: '', sleepTime: '' }]);

    const userStore = useUserStore();
    const user = ref({ name: '', employeeNumber: '' });

    const updateUserInfo = () => {
      // 새로운 사용자 정보가 있을 때 localStorage 업데이트
      user.value = {
        name: userStore.name,
        employeeNumber: userStore.employeeNumber,
      };
      localStorage.setItem('user', JSON.stringify(user.value));
    };

    onMounted(() => {
      const storedUser = localStorage.getItem('user');
      if (storedUser) {
        user.value = JSON.parse(storedUser);
      } else {
        updateUserInfo();
      }
      fetchPrograms();
    });

    const openModal = (type) => {
      modalType.value = type;
      showModal.value = true;
      if (type === 'update' && selectedProgram.value) {
        selectedProgram.value = { ...selectedProgram.value };
      }
    };

    const closeModal = () => {
      showModal.value = false;
      newPrograms.value = [{ pgmId: '', pgmNm: '', filePath: '', sleepTime: '' }];
    };

    const fetchPrograms = async () => {
      try {
        if (user.value.employeeNumber) {
          const response = await axios.get(`/api/program/PagePgmBase/${user.value.employeeNumber}`);
          programs.value = response.data;
        }
      } catch (error) {
        console.error("Error fetching program list:", error);
      }
    };

    const validateFilePath = (program) => {
      const filePathRegex = /^[A-Za-z0-9_/\\:.-]*$/;
      if (/\s/.test(program.filePath)) {
        alert('실행 경로는 공백없이 입력해야합니다.');
        program.filePath = '';
      } else if (!filePathRegex.test(program.filePath)) {
        alert('실행 경로는 영문, 숫자, /, \\, :, ., -만 입력 가능합니다.');
        program.filePath = program.filePath.replace(/[^A-Za-z0-9_/\\:.-]/g, '');
      }
    };

    const validateProgramName = (program) => {
      const allowedChars = /^[A-Za-z0-9_.-]*$/;
      if (/\s/.test(program.pgmNm)) {
        alert('프로그램명은 공백 없이 입력해야 합니다.');
        program.pgmNm = program.pgmNm.replace(/\s/g, '');
      } else if (!allowedChars.test(program.pgmNm)) {
        alert('프로그램명은 영문, 숫자, ., _, - 만 입력 가능합니다.');
        program.pgmNm = program.pgmNm.replace(/[^A-Za-z0-9_.-]/g, '');
      }
    };

    const registerMultiplePrograms = async () => {
      try {
        for (const program of newPrograms.value) {
          if (!program.pgmNm || !program.filePath || !program.sleepTime) {
            alert('입력되지 않은 항목이 존재합니다. 확인 후 다시 등록해주세요');
            return;
          }

          const sleepTimeNumber = parseInt(program.sleepTime, 10);
          if (sleepTimeNumber < 3 || sleepTimeNumber > 60) {
            alert('대기시간은 3초 이상 60초 이내로 입력해야 합니다.');
            return;
          }

          const programDto = {
            pgmNm: program.pgmNm,
            empNo: user.value.employeeNumber,
            filePath: program.filePath,
            sleepTime: program.sleepTime
          };
          await axios.post('/api/program/register', programDto);
        }

        fetchPrograms();
        closeModal();
      } catch (error) {
        console.error("Error registering programs:", error);
      }
    };

    const submitProgramUpdate = async (updatedProgram) => {
      try {
        updatedProgram.empNo = user.value.employeeNumber;
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
              empNo: user.value.employeeNumber
           };
            await axios.post('/api/program/delete', programDto);
            programs.value = programs.value.filter(p => p !== selectedProgram.value);

            await fetchPrograms();
            selectedProgram.value = null;
         } catch (error) {
            console.error("Error deleting program:", error);
        }
      }
    };

    const formatDate = (dateString) => {
      const date = new Date(dateString);
      return date.toISOString().split('T')[0];
    };

    return {
      showModal,
      modalType,
      programs,
      selectedProgram,
      newPrograms,
      user,
      openModal,
      closeModal,
      addProgram,
      removeNewProgram,
      registerMultiplePrograms,
      submitProgramUpdate,
      removeSelectedProgram,
      fetchPrograms,
      validateFilePath,
      validateProgramName,
      formatDate,
      updateUserInfo // 사용자 정보 업데이트 함수
    };
  }
};
</script>

<style scoped>
.user-info {
  text-align: left;
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: bold;
}

.container {
  width: 80%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 80px 0;
  font-family: 'KCCMurukmuruk', sans-serif;
}

h1 {
  text-align: center;
  padding: 20px 0;
}

table thead th {
  background-color: #FCF4C0;
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

button:disabled {
  background-color: grey;
  cursor: not-allowed;
}
</style>
