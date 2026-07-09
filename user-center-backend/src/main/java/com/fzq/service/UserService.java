package com.fzq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fzq.model.entity.User;
import com.fzq.model.request.SafeUser;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {

	/**
	 * 用户注册 1.这里因为user表中的id是long类型，所以这里返回long，不是int 2.如果只是用一下，就直接用，没必要使用包装类
	 * @param userAccount 用户账户
	 * @param userPassword 用户密码
	 * @param checkPassword 校验密码
	 * @return 新用户id
	 */
	long userRegister(String userAccount, String userPassword, String checkPassword);

	/**
	 * 用户登录
	 * @param userAccount 用户账户
	 * @param userPassword 用户密码
	 * @param request 前端请求
	 * @return 脱敏后的用户信息
	 */
	SafeUser userLogin(String userAccount, String userPassword, HttpServletRequest request);

	/**
	 * 用户脱敏
	 * @param originUser 原始用户user
	 * @return safeUser 安全脱敏用户
	 */
	SafeUser getSafeUser(User originUser);

	/**
	 * 用户登出
	 * @param request 当前 HTTP 请求，用于清理 Session 中保存的登录态
	 */
	void userLogOut(HttpServletRequest request);

}
