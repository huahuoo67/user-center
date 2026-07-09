<template>
  <div id="userLoginPage">
    <div class="form-panel">
      <h2 class="title">用户登录</h2>
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
        <a-form-item :wrapper-col="{ offset: 5, span: 19 }">
          <a-button type="primary" html-type="submit" block :loading="submitting">登录</a-button>
        </a-form-item>
      </a-form>
    </div>
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
#userLoginPage {
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
