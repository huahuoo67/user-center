import { defineStore } from "pinia";
import { ref } from "vue";
import { getCurrentUser } from "@/api/user";
import type { User } from "@/types/user";

export const useLoginUserStore = defineStore("loginUser", () => {
  const loginUser = ref<User>({
    username: "未登录",
  });

  async function fetchLoginUser() {
    const res = await getCurrentUser();
    if (res.data.code === "200" && res.data.data) {
      loginUser.value = res.data.data;
    }
  }

  function setLoginUser(newLoginUser: User) {
    loginUser.value = newLoginUser;
  }

  function clearLoginUser() {
    loginUser.value = {
      username: "未登录",
    };
  }

  return { loginUser, setLoginUser, clearLoginUser, fetchLoginUser };
});
