<template>
  <div id="userLoginPage">
    <h2 class="title">用户登录</h2>
    <a-form
      style="max-width: 480px; margin: 0 auto"
      label-align="left"
      :label-col="{ span: 4 }"
      :wrapper-col="{ span: 20 }"
      :model="form"
      @finish="handleSubmit"
    >
      <a-form-item
        name="userAccount"
        label="账号"
        :rules="[{ required: true, message: '请输入账号' }]"
      >
        <a-input v-model:value="form.userAccount" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item
        name="userPassword"
        label="密码"
        :rules="[
          { required: true, message: '请输入密码' },
          { min: 8, message: '密码不少于 8 位' },
        ]"
      >
        <a-input-password
          v-model:value="form.userPassword"
          placeholder="请输入密码"
        />
      </a-form-item>
      <a-form-item :wrapper-col="{ offset: 4, span: 20 }">
        <a-button type="primary" html-type="submit" :loading="submitting">登录</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue';
import {useRouter} from "vue-router";
import {useLoginUserStore} from "@/store/userLoginUserStore.ts";
import {userLogin} from "@/api/user.ts";
import {message} from "ant-design-vue";
import type { LoginParams } from "@/types/user";

const form = reactive<LoginParams>({
  userAccount: '',
  userPassword: '',
});
const submitting = ref(false);

const router = useRouter();
const loginUserStore = useLoginUserStore();

/**
 * 提交表单
 * @param data
 */
const handleSubmit = async () => {
  try {
    submitting.value = true;
    const res = await userLogin(form);
    if (res.data.code === "200" && res.data.data) {
      await loginUserStore.fetchLoginUser();
      message.success("登录成功");
      await router.push({
        path: "/",
        replace: true,
      });
      return;
    }
    message.error(res.data.message || "登录失败");
  } finally {
    submitting.value = false;
  }
};

</script>

<style scoped>
#userLoginPage .title {
  text-align: center;
  margin-bottom: 16px;
}
</style>
