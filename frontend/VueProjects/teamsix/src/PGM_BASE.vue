<template>
  <div class="container">
    <h1>PC 세팅 자동화</h1>

    <!-- 프로그램 등록 및 수정 폼 -->
    <div class="form">
      <h2>프로그램 등록</h2>
      <form @submit.prevent="프로그램등록">
        <div class="form-group">
          <label for="programName">프로그램명</label>
          <input v-model="새프로그램.이름" type="text" id="programName" placeholder="프로그램명을 입력하세요" required />
        </div>
        <div class="form-group">
          <label for="filePath">실행파일 경로</label>
          <input v-model="새프로그램.파일경로" type="text" id="filePath" placeholder="실행파일 경로를 입력하세요" required />
        </div>
        <button type="submit">등록</button>
      </form>
    </div>

    <!-- 등록된 프로그램 목록 -->
    <div class="program-list">
      <h2>등록된 프로그램</h2>
      <table>
        <thead>
          <tr>
            <th>프로그램명</th>
            <th>파일 경로</th>
            <th>등록일</th>
            <th>수정일</th>
            <th>작업</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(program, index) in 프로그램들" :key="index">
            <td>{{ program.이름 }}</td>
            <td>{{ program.파일경로 }}</td>
            <td>{{ program.등록일 }}</td>
            <td>{{ program.수정일 }}</td>
            <td>
              <button @click="프로그램수정(index)">수정</button>
              <button @click="프로그램삭제(index)">삭제</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 프로그램 실행 순서 설정 -->
    <div class="execution-order">
      <h2>프로그램 실행 순서 설정</h2>
      <ul>
        <li v-for="(program, index) in 프로그램들" :key="index">
          {{ index + 1 }}. {{ program.이름 }}
          <button @click="위로(index)" :disabled="index === 0">위로</button>
          <button @click="아래로(index)" :disabled="index === 프로그램들.length - 1">아래로</button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      새프로그램: {
        이름: "",
        파일경로: "",
        등록일: new Date().toISOString().slice(0, 10),
        수정일: new Date().toISOString().slice(0, 10),
      },
      프로그램들: [
        { 이름: "SG Client", 파일경로: "C:\\Users\\82103\\OneDrive\\바탕 화면", 등록일: "2024-09-27", 수정일: "2024-09-27" },
        { 이름: "Slack", 파일경로: "C:\\Users\\82103\\AppData\\Local\\slack\\app-4.40.128", 등록일: "2024-09-27", 수정일: "2024-09-27" }
      ],
    };
  },
  methods: {
    프로그램등록() {
      this.프로그램들.push({ ...this.새프로그램 });
      this.새프로그램.이름 = "";
      this.새프로그램.파일경로 = "";
      this.새프로그램.수정일 = new Date().toISOString().slice(0, 10);
    },
    프로그램수정(index) {
      this.새프로그램 = { ...this.프로그램들[index] };
      this.프로그램들.splice(index, 1);
    },
    프로그램삭제(index) {
      this.프로그램들.splice(index, 1);
    },
    위로(index) {
      if (index > 0) {
        const temp = this.프로그램들[index - 1];
        this.프로그램들[index - 1] = this.프로그램들[index];
        this.프로그램들[index] = temp;
      }
    },
    아래로(index) {
      if (index < this.프로그램들.length - 1) {
        const temp = this.프로그램들[index + 1];
        this.프로그램들[index + 1] = this.프로그램들[index];
        this.프로그램들[index] = temp;
      }
    },
  },
};
</script>

<style scoped>
.container {
  max-width: 800px;
  margin: auto;
}
.form-group {
  margin-bottom: 1em;
}
button {
  margin-right: 0.5em;
}
table {
  width: 100%;
  margin-top: 1em;
  border-collapse: collapse;
}
table, th, td {
  border: 1px solid black;
}
th, td {
  padding: 8px;
  text-align: left;
}
.execution-order {
  margin-top: 2em;
}
</style>
