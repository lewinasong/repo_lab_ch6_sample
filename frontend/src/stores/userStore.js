// src/stores/userStore.js
import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    employeeNumber: null,
    name: null
  }),
  actions: {
    setUser(user) {
      this.employeeNumber = user.employeeNumber;
      this.name = user.name;
    }
  }
});
