<template>
  <div id="userProfilePage">
    <a-card title="完善个人信息" :loading="loading">
      <a-row :gutter="32">
        <a-col :xs="24" :md="16">
          <a-form
            label-align="left"
            :label-col="{ span: 5 }"
            :wrapper-col="{ span: 19 }"
            :model="form"
            @finish="handleSubmit"
          >
            <a-form-item label="账号">
              <a-input :value="loginUserStore.loginUser.userAccount" disabled />
            </a-form-item>
            <a-form-item
              name="username"
              label="昵称"
              :rules="[{ max: 50, message: '昵称不能超过 50 个字符' }]"
            >
              <a-input v-model:value="form.username" placeholder="请输入昵称" />
            </a-form-item>
            <a-form-item name="avatarUrl" label="头像地址">
              <a-input v-model:value="form.avatarUrl" placeholder="请输入 http(s) 图片地址" />
            </a-form-item>
            <a-form-item name="gender" label="性别">
              <a-select v-model:value="form.gender" allow-clear placeholder="请选择">
                <a-select-option :value="0">保密</a-select-option>
                <a-select-option :value="1">男</a-select-option>
                <a-select-option :value="2">女</a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item name="phone" label="电话">
              <a-input v-model:value="form.phone" placeholder="请输入联系电话" />
            </a-form-item>
            <a-form-item
              name="email"
              label="邮箱"
              :rules="[{ type: 'email', message: '请输入正确的邮箱地址' }]"
            >
              <a-input v-model:value="form.email" placeholder="请输入邮箱" />
            </a-form-item>
            <a-form-item :wrapper-col="{ offset: 5, span: 19 }">
              <a-button type="primary" html-type="submit" :loading="submitting">
                保存个人信息
              </a-button>
            </a-form-item>
          </a-form>
        </a-col>
        <a-col :xs="24" :md="8" class="avatar-preview">
          <a-avatar :size="128" :src="form.avatarUrl">
            {{ avatarFallback }}
          </a-avatar>
          <div class="preview-tip">头像预览</div>
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from "vue";
import { message } from "ant-design-vue";
import { updateUserProfile } from "@/api/user";
import { useLoginUserStore } from "@/store/userLoginUserStore";
import type { ProfileUpdateParams } from "@/types/user";

const loginUserStore = useLoginUserStore();
const loading = ref(true);
const submitting = ref(false);
const form = reactive<ProfileUpdateParams>({
  username: "",
  avatarUrl: "",
  gender: undefined,
  phone: "",
  email: "",
});

const avatarFallback = computed(() => {
  const name = form.username || loginUserStore.loginUser.userAccount || "用户";
  return name.slice(0, 1).toUpperCase();
});

const fillForm = () => {
  const user = loginUserStore.loginUser;
  form.username = user.username || "";
  form.avatarUrl = user.avatarUrl || "";
  form.gender = user.gender;
  form.phone = user.phone || "";
  form.email = user.email || "";
};

onMounted(async () => {
  try {
    if (!loginUserStore.loginUser.id) {
      await loginUserStore.fetchLoginUser();
    }
    fillForm();
  } finally {
    loading.value = false;
  }
});

const handleSubmit = async () => {
  try {
    submitting.value = true;
    const res = await updateUserProfile(form);
    if (res.data.code === "200" && res.data.data) {
      loginUserStore.setLoginUser(res.data.data);
      fillForm();
      message.success("个人信息已保存");
      return;
    }
    message.error(res.data.message || "保存失败");
  } finally {
    submitting.value = false;
  }
};
</script>

<style scoped>
#userProfilePage {
  max-width: 900px;
  margin: 0 auto;
}

.avatar-preview {
  text-align: center;
  padding-top: 24px;
}

.preview-tip {
  margin-top: 12px;
  color: rgba(0, 0, 0, 0.45);
}
</style>
