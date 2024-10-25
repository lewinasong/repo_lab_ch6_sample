import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    employeeNumber: '',
    name: '',
  }),
  actions: {
    setUser(user) {
      this.employeeNumber = user.employeeNumber;
      this.name = user.name;
    },
  },
});