<template>
  <div id="userRegisterPage">
    <h2 class="title">用户注册</h2>
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
      <a-form-item
        name="checkPassword"
        label="确认密码"
        :rules="[
          { required: true, message: '请输入确认密码' },
          { min: 8, message: '确认密码不少于 8 位' },
        ]"
      >
        <a-input-password
          v-model:value="form.checkPassword"
          placeholder="请输入密码"
        />
      </a-form-item>
      <a-form-item :wrapper-col="{ offset: 4, span: 20 }">
        <a-button type="primary" html-type="submit" :loading="submitting">注册</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang="ts" setup>
import {message} from "ant-design-vue";
import router from "@/router";
import {userRegister} from "@/api/user.ts";
import { reactive, ref } from "vue";
import type { RegisterParams } from "@/types/user";

const form = reactive<RegisterParams>({
  userAccount: "",
  userPassword: "",
  checkPassword: "",
});
const submitting = ref(false);

const handleSubmit = async () => {
  if (form.checkPassword !== form.userPassword) {
    message.error("二次输入的密码不一致");
    return;
  }
  try {
    submitting.value = true;
    const res = await userRegister(form);
    if (res.data.code === "200" && res.data.data) {
      message.success("注册成功");
      await router.push({
        path: "/user/login",
        replace: true,
      });
      return;
    }
    message.error(res.data.message || "注册失败");
  } finally {
    submitting.value = false;
  }
};

</script>
