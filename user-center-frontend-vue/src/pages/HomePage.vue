<template>
  <div id="homePage">
    <section class="overview">
      <div>
        <h1>用户中心</h1>
        <p class="sub-title">账号、登录态和管理员权限的统一管理入口</p>
      </div>
      <a-space>
        <template v-if="!loginUserStore.loginUser.id">
          <a-button type="primary" @click="goTo('/user/register')">注册账号</a-button>
          <a-button @click="goTo('/user/login')">去登录</a-button>
        </template>
        <template v-else>
          <a-button type="primary" @click="goTo('/user/profile')">完善个人信息</a-button>
          <a-button
            v-if="loginUserStore.loginUser.userRole === 1"
            @click="goTo('/admin/userManage')"
          >
            用户管理
          </a-button>
        </template>
      </a-space>
    </section>

    <a-row :gutter="[16, 16]">
      <a-col :xs="24" :md="8">
        <a-card size="small" title="当前用户">
          <a-statistic
            :value="currentUserName"
            :value-style="{ fontSize: '20px' }"
          />
          <a-tag v-if="loginUserStore.loginUser.userRole === 1" color="green">管理员</a-tag>
          <a-tag v-else-if="loginUserStore.loginUser.id" color="blue">普通用户</a-tag>
          <a-tag v-else>未登录</a-tag>
        </a-card>
      </a-col>
      <a-col :xs="24" :md="8">
        <a-card size="small" title="登录态">
          <a-statistic :value="loginUserStore.loginUser.id ? '已登录' : '未登录'" />
          <a-button v-if="!loginUserStore.loginUser.id" type="link" @click="goTo('/user/login')">
            去登录
          </a-button>
        </a-card>
      </a-col>
      <a-col :xs="24" :md="8">
        <a-card size="small" title="权限">
          <a-statistic :value="permissionName" />
          <a-button
            v-if="loginUserStore.loginUser.userRole === 1"
            type="link"
            @click="goTo('/admin/userManage')"
          >
            查看用户列表
          </a-button>
        </a-card>
      </a-col>
    </a-row>

    <a-list class="feature-list" bordered :data-source="features">
      <template #header>
        <strong>系统能力</strong>
      </template>
      <template #renderItem="{ item }">
        <a-list-item>
          <a-list-item-meta :title="item.title" :description="item.description" />
          <a-tag :color="item.color">{{ item.status }}</a-tag>
        </a-list-item>
      </template>
    </a-list>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useRouter } from "vue-router";
import { useLoginUserStore } from "@/store/userLoginUserStore";

const router = useRouter();
const loginUserStore = useLoginUserStore();

const currentUserName = computed(() => {
  const user = loginUserStore.loginUser;
  if (!user.id) {
    return "未登录";
  }
  return user.username || user.userAccount || "用户";
});

const permissionName = computed(() => {
  if (!loginUserStore.loginUser.id) {
    return "未登录";
  }
  return loginUserStore.loginUser.userRole === 1 ? "管理权限" : "基础权限";
});

const features = [
  {
    title: "统一响应与异常处理",
    description: "接口返回结构一致，业务错误由全局异常处理器统一输出。",
    status: "已完成",
    color: "green",
  },
  {
    title: "Session 登录态",
    description: "登录后保存脱敏用户信息，前端刷新后可恢复当前用户。",
    status: "已完成",
    color: "green",
  },
  {
    title: "管理员权限",
    description: "管理员可进入用户管理页面，普通用户会被拦截。",
    status: "已完成",
    color: "green",
  },
];

const goTo = (path: string) => {
  router.push(path);
};
</script>

<style scoped>
#homePage {
  max-width: 1080px;
  margin: 0 auto;
}

.overview {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  align-items: center;
  margin-bottom: 20px;
}

.overview h1 {
  margin-bottom: 8px;
}

.sub-title {
  color: rgba(0, 0, 0, 0.55);
  margin-bottom: 0;
}

.feature-list {
  margin-top: 16px;
  background: #fff;
}

@media (max-width: 640px) {
  .overview {
    align-items: flex-start;
    flex-direction: column;
  }
}
</style>
