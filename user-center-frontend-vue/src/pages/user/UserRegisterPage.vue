<template>
  <div id="userRegisterPage">
    <div class="form-panel">
      <h2 class="title">用户注册</h2>
      <a-form
        label-align="left"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 19 }"
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
          name="username"
          label="昵称"
          :rules="[{ max: 50, message: '昵称不能超过 50 个字符' }]"
        >
          <a-input v-model:value="form.username" placeholder="选填，之后也可以完善" />
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
        <a-form-item :wrapper-col="{ offset: 5, span: 19 }">
          <a-button type="primary" html-type="submit" block :loading="submitting">注册</a-button>
        </a-form-item>
      </a-form>
    </div>
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
  username: "",
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

<style scoped>
#userRegisterPage {
  display: flex;
  justify-content: center;
  min-height: calc(100vh - 180px);
  padding-top: 48px;
}

.form-panel {
  width: 100%;
  max-width: 480px;
  height: fit-content;
  padding: 28px 28px 20px;
  background: #fff;
  border: 1px solid #f0f0f0;
  border-radius: 8px;
}

.title {
  text-align: center;
  margin-bottom: 24px;
}
</style>
