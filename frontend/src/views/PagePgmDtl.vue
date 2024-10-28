<template>
  <div class="container">
    <!-- 로그인 사용자 정보 -->
    <div class="user-info">
      <p>로그인 사용자: {{ user.name ? user.name + " (" + user.employeeNumber + ")" : "정보 없음" }}</p>
    </div>

    <h1>프로그램 실행순서 설정</h1>

    <!-- 프로그램 순서 입력 폼 -->
    <table class="fixed-table">
      <thead>
        <tr>
          <th class="header-cell">순번 입력</th>
          <th class="header-cell">프로그램명</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(program, index) in programs" :key="index">
          <td>
            <input
              type="text"
              v-model="program.order"
              placeholder="순번 입력"
              @input="validateOrder(program)"
            />
          </td>
          <td>{{ program.pgmNm }}</td>
        </tr>
      </tbody>
    </table>

    <button @click="sortPrograms">변경 후 프로그램 순서 확인</button>

    <div v-if="sortedPrograms.length > 0">
      <h2>변경 후 프로그램 실행순서</h2>
      <table class="fixed-table">
        <thead>
          <tr>
            <th class="header-cell2">순번 입력</th>
            <th class="header-cell2">프로그램명</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(program, index) in sortedPrograms" :key="index">
            <td>{{ index + 1 }}</td>
            <td>{{ program.pgmNm }}</td>
          </tr>
        </tbody>
      </table>
      <button class="save_button" @click="openSaveModal">저장</button>
    </div>

    <ModalConfirm
      v-if="showSaveModal"
      @confirm="savePrograms"
      @close="showSaveModal = false"
    />
  </div>
</template>

<script>
import ModalConfirm from './ModalConfirm.vue'; 
import { useUserStore } from '@/stores/userStore'; 
import axios from 'axios'; 

export default {
  components: {
    ModalConfirm,
  },
  data() {
    return {
      programs: [], 
      sortedPrograms: [], 
      showSaveModal: false, 
      user: {
        name: '',
        employeeNumber: ''
      }
    };
  },
  computed: {
    userStore() {
      return useUserStore();
    },
  },
  mounted() {
    // 사용자 정보를 불러오거나 업데이트
    this.loadUserInfo();
    this.fetchPrograms();
  }, 
  methods: {
    loadUserInfo() {
      // localStorage에 저장된 사용자 정보가 있는지 확인 후, 업데이트
      const storedUser = localStorage.getItem('user');
      
      if (storedUser) {
        // localStorage에서 사용자 정보 가져와 data 속성에 반영
        this.user = JSON.parse(storedUser);
      } else {
        // 사용자 정보가 없을 경우 현재 사용자 정보를 가져와서 localStorage에 저장
        const currentUser = { name: this.userStore.name, employeeNumber: this.userStore.employeeNumber };
        this.user = currentUser;
        localStorage.setItem('user', JSON.stringify(currentUser));
      }
    },
    async fetchPrograms() {
      try {
        const response = await axios.get(
          `/api/program/PagePgmDtl/${this.user.employeeNumber}`
        ); 
        this.programs = response.data; 
      } catch (error) {
        console.error('프로그램 목록을 불러오는 중 에러 발생:', error);
      }
    },
    validateOrder(program) {
      if (
        !/^\d*$/.test(program.order) ||
        (program.order !== '' && parseInt(program.order) <= 0)
      ) {
        program.order = ''; 
        alert('0 이상의 숫자를 입력하세요.');
      }
    },
    hasEmptyOrders() {
      return this.programs.some((program) => program.order === '');
    },
    hasDuplicateOrders() {
      const orders = this.programs
        .map((program) => program.order)
        .filter((order) => order !== '');
      const uniqueOrders = new Set(orders);
      return uniqueOrders.size !== orders.length;
    },
    hasOrderExceedingProgramCount() {
      const maxOrder = Math.max(
        ...this.programs.map((program) => parseInt(program.order) || 0)
      );
      return maxOrder > this.programs.length;
    },
    sortPrograms() {
      console.log("정렬 전 programs:", this.programs);

      if (this.hasEmptyOrders()) {
        alert('순번입력이 안된 항목이 있습니다. 확인후 진행해주세요.');
        return;
      }

      if (this.hasDuplicateOrders()) {
        alert('중복된 순번이 있습니다. 순번을 확인해주세요.');
        return;
      }

      if (this.hasOrderExceedingProgramCount()) {
        alert('순번은 ' + this.programs.length + ' 이하로 입력해주세요');
        return;
      }

      this.sortedPrograms = this.programs
        .filter((program) => program.order !== '' && parseInt(program.order) > 0)
        .sort((a, b) => parseInt(a.order) - parseInt(b.order));

      console.log("정렬 후 sortedPrograms:", this.sortedPrograms);
    },
    openSaveModal() {
      if (this.sortedPrograms.length === 0) {
        alert('프로그램 순서를 먼저 확인해주세요.');
        return;
      }
      this.showSaveModal = true;
    },
    async savePrograms() {
      this.sortPrograms(); 

      const orderedProgramIds = this.sortedPrograms
        .map((program) => program.pgmId) 
        .filter((id) => id !== null && id !== undefined);

      if (orderedProgramIds.length === 0) {
        alert("프로그램 순서를 입력하고 정렬해 주세요.");
        console.log("정렬된 프로그램이 없습니다. sortedPrograms:", this.sortedPrograms);
        return;
      }

      try {
        console.log("empNo:", this.user.employeeNumber);
        console.log("programOrder:", orderedProgramIds);
        console.log("전송 데이터:", {
          empNo: this.user.employeeNumber,
          programOrder: orderedProgramIds,
        });

        await axios.put(`/api/program/updateOrder`, {
          empNo: this.user.employeeNumber,
          programOrder: orderedProgramIds,
        });
        
        alert("프로그램 실행순서가 저장되었습니다.");
      } catch (error) {
        console.error("프로그램 순서 업데이트 중 오류 발생:", error.response || error.message);
        alert("프로그램 실행순서 설정이 실패했습니다.");
      }

      this.showSaveModal = false;
    },
  },
};
</script>




<style scoped>
/* 스타일 정의 */
.user-info {
  text-align: left;
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
  font-size: 30px;
  padding: 20px 0;
}

table {
  border-collapse: collapse;
  margin-top: 20px;
  width: 100%;
}

.fixed-table {
  table-layout: fixed;
}

th,
td {
  border: 1px solid black;
  padding: 10px;
  text-align: center;
  width: 50%;
}

input {
  width: 50%;
  padding: 5px;
  text-align: center;
}

.header-cell {
  background-color: #fcf4c0;
  color: black;
}

.header-cell2 {
  background-color: #cefbc9;
  color: black;
}

button {
  margin-top: 20px;
  padding: 10px 20px;
  font-size: 16px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  font-family: 'KCCMurukmuruk', sans-serif;
}

.save_button {
  background-color: #ffd700;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

h2 {
  margin-top: 30px;
  font-size: 30px;
  padding: 20px 0;
}
</style>
