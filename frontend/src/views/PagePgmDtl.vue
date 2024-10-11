<!-- PagePgmDtl.vue -->
<template>
  <div class="container">
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
          <td>{{ program.name }}</td>
        </tr>
      </tbody>
    </table>

    <button @click="sortPrograms">변경후 프로그램 실행순서 확인</button>

    <!-- 정렬된 프로그램 목록 표시 -->
    <div v-if="sortedPrograms.length > 0">
      <h2>정렬된 프로그램 목록</h2>
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
            <td>{{ program.name }}</td>
          </tr>
        </tbody>
      </table>
      <!-- 저장 버튼 추가 -->
      <button class="save_button" @click="savePrograms">저장</button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 프로그램 목록
      programs: [
        { id: 1, name: "SG Client", order: '' },
        { id: 2, name: "Chakra", order: '' },
        { id: 3, name: "NeoWorks", order: '' },
        { id: 4, name: "Slack", order: '' }
      ],
      sortedPrograms: []
    };
  },
  methods: {
    // 순번 유효성 검사 (0 이상의 숫자인지 확인)
    validateOrder(program) {
      if (!/^\d*$/.test(program.order) || (program.order !== '' && parseInt(program.order) <= 0)) {
        program.order = '';
        alert('0 이상의 숫자를 입력하세요.');
      }
    },
    // 중복된 순번이 있는지 확인하는 메서드
    hasDuplicateOrders() {
      const orders = this.programs.map(program => program.order).filter(order => order !== '');
      const uniqueOrders = new Set(orders);
      return uniqueOrders.size !== orders.length;
    },
    // 순번이 입력되지 않은 프로그램이 있는지 확인하는 메서드
    hasEmptyOrders() {
      return this.programs.some(program => program.order === '');
    },
    // 입력된 순번 중 프로그램 수보다 큰 값이 있는지 확인하는 메서드
    hasOrderExceedingProgramCount() {
      const maxOrder = Math.max(...this.programs.map(program => parseInt(program.order) || 0));
      return maxOrder > this.programs.length;
    },
    // 입력된 순번에 따라 프로그램 목록 정렬
    sortPrograms() {
      // 순번 입력 완료 여부 체크
      if (this.hasEmptyOrders()) {
        alert('순번입력이 완료되지 않았습니다. 확인 후 진행해주세요.');
        return;
      }

      // 중복된 순번 체크
      if (this.hasDuplicateOrders()) {
        alert('중복된 순번이 있습니다. 순번을 확인해주세요.');
        return;
      }

      // 프로그램 수보다 순번이 큰지 체크
      if (this.hasOrderExceedingProgramCount()) {
        alert('순번은 '+ this.programs.length +'이하로 입력해주세요');
        return;
      }

      // 순번이 유효한 프로그램만 필터링하여 정렬
      this.sortedPrograms = this.programs
        .filter(program => program.order !== '' && parseInt(program.order) > 0)
        .sort((a, b) => parseInt(a.order) - parseInt(b.order));
    }
  }
};
</script>

<style scoped>
/* @font-face를 사용하여 폰트를 정의 */
@font-face {
  font-family: 'KCCMurukmuruk';
  src: url('@/fonts/KCCMurukmuruk.ttf') format('truetype');
}

.container {  
  width: 60%; /* 너비를 60%로 설정 */
  max-width: 800px; /* 최대 너비를 800px로 제한 */
  margin: 0 auto; /* 가로 중앙 정렬 */
  padding: 80px 0; /* 상하 80px 패딩, 좌우 패딩은 0 */
  font-family: 'KCCMurukmuruk', sans-serif;
}

h1 {
  text-align: center;
  font-size : 30px;
  padding: 20px 0; /* 상하 20px 패딩, 좌우 패딩은 0 */
}

table {
  border-collapse: collapse;
  margin-top: 20px;
  width: 100%;
}

.fixed-table {
  table-layout: fixed; /* 열 너비를 고정 */
}

th, td {
  border: 1px solid black;
  padding: 10px;
  text-align: center;
  width: 50%; /* 두 열의 너비를 동일하게 설정 */
}

input {
  width: 50%;
  padding: 5px;
  text-align: center;
}

/* 테이블 헤더에 배경색과 텍스트 색상 추가 */
.header-cell {
  background-color: #fcf4c0; /* 배경색 */
  color: black; /* 텍스트 색상 */
  font-family: 'KCCMurukmuruk', sans-serif;
}

.header-cell2 {
  background-color: #cefbc9; /* 배경색 */
  color: black; /* 텍스트 색상 */
  font-family: 'KCCMurukmuruk', sans-serif;
}

button {
  margin-top: 20px;
  padding: 10px 20px;
  font-size: 16px;
  font-family: 'KCCMurukmuruk', sans-serif;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 그림자 추가 */
}

.save_button {
  background-color:#FFD700; /* 배경색 */
  font-family: 'KCCMurukmuruk', sans-serif;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 그림자 추가 */

}

h2 {
  margin-top: 30px;
  font-size : 30px;
  padding: 20px 0; /* 상하 20px 패딩, 좌우 패딩은 0 */
}

.sorted-table {
  margin-top: 20px;
}
</style>


