import { useLoginUserStore } from "@/store/userLoginUserStore.ts";
import { message } from "ant-design-vue";
import router from "@/router";

/**
 * 全局权限校验
 */
router.beforeEach(async (to, _from, next) => {
  const loginUserStore = useLoginUserStore();
  if (to.path.startsWith("/admin")) {
    // 页面刷新后 Pinia 会丢失内存状态，进入管理页前先尝试恢复登录用户。
    if (!loginUserStore.loginUser.id) {
      await loginUserStore.fetchLoginUser();
    }
    if (loginUserStore.loginUser.userRole !== 1) {
      message.error("没有权限");
      next(`/user/login?redirect=${to.fullPath}`);
      return;
    }
  }
  next();
});
