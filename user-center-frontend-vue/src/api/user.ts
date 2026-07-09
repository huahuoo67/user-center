import myAxios from "@/request";
import type { ApiResponse, DeleteUserParams, LoginParams, RegisterParams, User } from "@/types/user";

/**
 * 用户注册
 * @param params
 */
export const userRegister = async (params: RegisterParams) => {
  return myAxios.request<ApiResponse<number>>({
    url: "/user/register",
    method: "POST",
    data: params,
  });
};

/**
 * 用户登录
 * @param params
 */
export const userLogin = async (params: LoginParams) => {
  return myAxios.request<ApiResponse<User>>({
    url: "/user/login",
    method: "POST",
    data: params,
  });
};

/**
 * 用户注销
 */
export const userLogout = async () => {
  return myAxios.request<ApiResponse<void>>({
    url: "/user/logout",
    method: "POST",
  });
};

/**
 * 获取当前用户
 */
export const getCurrentUser = async () => {
  return myAxios.request<ApiResponse<User>>({
    url: "/user/current",
    method: "GET",
  });
};

/**
 * 获取用户列表
 * @param username
 */
export const searchUsers = async (username = "") => {
  return myAxios.request<ApiResponse<User[]>>({
    url: "/user/search",
    method: "GET",
    params: {
      username,
    },
  });
};

/**
 * 删除用户
 * @param params
 */
export const deleteUser = async (params: DeleteUserParams) => {
  return myAxios.request<ApiResponse<boolean>>({
    url: "/user/delete",
    method: "POST",
    data: params,
  });
};
