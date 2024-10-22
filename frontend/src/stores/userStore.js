import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null, // 사용자 정보를 저장할 객체 (초기값 null)
  }),
  actions: {
    setUser(userInfo) {
      this.user = userInfo; // 전달받은 사용자 정보를 저장
    },
  },
});