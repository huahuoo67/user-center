<template>
  <div id="globalHeader">
    <a-row :wrap="false">
      <a-col flex="200px">
        <div class="title-bar">
          <img class="logo" src="../assets/logo.png" alt="logo" />
          <div class="title">鱼皮用户中心</div>
        </div>
      </a-col>
      <a-col flex="auto">
        <a-menu
          v-model:selectedKeys="current"
          mode="horizontal"
          :items="items"
          @click="doMenuClick"
        />
      </a-col>
      <a-col flex="160px">
        <div class="user-login-status">
          <a-dropdown v-if="loginUserStore.loginUser.id" placement="bottomRight">
            <a class="user-menu-trigger" @click.prevent>
              {{ currentUserName }}
              <DownOutlined />
            </a>
            <template #overlay>
              <a-menu @click="handleUserMenuClick">
                <a-menu-item key="profile">
                  <UserOutlined />
                  个人信息
                </a-menu-item>
                <a-menu-divider />
                <a-menu-item key="logout" danger>
                  <LogoutOutlined />
                  退出登录
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
          <div v-else>
            <a-button type="primary" href="/user/login">登录</a-button>
          </div>
        </div>
      </a-col>
    </a-row>
  </div>
</template>
<script lang="ts" setup>
import { computed, h, ref } from "vue";
import {
  CrownOutlined,
  DownOutlined,
  HomeOutlined,
  LogoutOutlined,
  UserOutlined,
} from "@ant-design/icons-vue";
import type { MenuProps } from "ant-design-vue";
import { message } from "ant-design-vue";
import {useRouter } from "vue-router";
import {useLoginUserStore} from "@/store/userLoginUserStore.ts";
import { userLogout } from "@/api/user";

const loginUserStore = useLoginUserStore();

const router = useRouter();
const currentUserName = computed(
  () =>
    loginUserStore.loginUser.username || loginUserStore.loginUser.userAccount || "用户",
);

const handleUserMenuClick: MenuProps["onClick"] = async ({ key }) => {
  if (key === "profile") {
    await router.push("/user/profile");
    return;
  }
  if (key === "logout") {
    const res = await userLogout();
    if (res.data.code !== "200") {
      message.error(res.data.message || "退出失败");
      return;
    }
    loginUserStore.clearLoginUser();
    message.success("已退出登录");
    await router.replace("/user/login");
  }
};
// 路由跳转事件
const doMenuClick = ({ key }: { key: string }) => {
  router.push({
    path: key,
  });
};

const current = ref<string[]>([]);
// 监听路由变化，更新当前选中菜单
router.afterEach((to) => {
  current.value = [to.path];
});

const items = computed<MenuProps["items"]>(() => {
  const menuItems: MenuProps["items"] = [
    {
      key: "/",
      icon: () => h(HomeOutlined),
      label: "主页",
      title: "主页",
    },
  ];
  if (loginUserStore.loginUser.id) {
    menuItems.push({
      key: "/user/profile",
      icon: () => h(UserOutlined),
      label: "个人信息",
      title: "个人信息",
    });
  } else {
    menuItems.push(
      {
        key: "/user/login",
        label: "用户登录",
        title: "用户登录",
      },
      {
        key: "/user/register",
        label: "用户注册",
        title: "用户注册",
      },
    );
  }
  if (loginUserStore.loginUser.userRole === 1) {
    menuItems.push({
      key: "/admin/userManage",
      icon: () => h(CrownOutlined),
      label: "用户管理",
      title: "用户管理",
    });
  }
  menuItems.push({
    key: "others",
    label: h(
      "a",
      { href: "https://www.codefather.cn", target: "_blank" },
      "编程导航",
    ),
    title: "编程导航",
  });
  return menuItems;
});
</script>

<style scoped>
.title-bar {
  display: flex;
  align-items: center;
}

.title {
  color: black;
  font-size: 18px;
  margin-left: 16px;
}

.logo {
  height: 48px;
}

.user-login-status {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  height: 100%;
}

.user-menu-trigger {
  display: inline-flex;
  gap: 6px;
  align-items: center;
  max-width: 150px;
  color: rgba(0, 0, 0, 0.88);
  white-space: nowrap;
  cursor: pointer;
}
</style>
