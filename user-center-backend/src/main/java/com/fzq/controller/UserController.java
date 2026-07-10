package com.fzq.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fzq.common.ErrorCode;
import com.fzq.common.Result;
import com.fzq.exception.BusinessException;
import com.fzq.model.entity.User;
import com.fzq.model.request.SafeUser;
import com.fzq.model.request.UserDeleteRequest;
import com.fzq.model.request.UserLoginRequest;
import com.fzq.model.request.UserProfileUpdateRequest;
import com.fzq.model.request.UserRegisterRequest;
import com.fzq.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.fzq.contant.UserContant.ADMIN_ROLE;
import static com.fzq.contant.UserContant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
public class UserController {

	@Resource
	private UserService userService;

	@PostMapping("/register")
	public Result<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest, HttpServletRequest request) {
		if (userRegisterRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		SafeUser loginUser = (SafeUser) request.getSession().getAttribute(USER_LOGIN_STATE);
		if (loginUser != null && loginUser.getUserRole() != ADMIN_ROLE) {
			throw new BusinessException(ErrorCode.NO_AUTH, "普通用户不能创建其他账号");
		}
		String userAccount = userRegisterRequest.getUserAccount();
		String username = userRegisterRequest.getUsername();
		String userPassword = userRegisterRequest.getUserPassword();
		String checkPassword = userRegisterRequest.getCheckPassword();
		if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		return Result.success(ErrorCode.SUCCESS,
				userService.userRegister(userAccount, username, userPassword, checkPassword));
	}

	@PostMapping("/login")
	public Result<SafeUser> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
		if (userLoginRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
		}
		String userAccount = userLoginRequest.getUserAccount();
		String userPassword = userLoginRequest.getUserPassword();
		if (StringUtils.isAnyBlank(userAccount, userPassword)) {
			throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
		}
		return Result.success(ErrorCode.SUCCESS, userService.userLogin(userAccount, userPassword, request));
	}

	@GetMapping("/current")
	public Result<SafeUser> getCurrentUser(HttpServletRequest request) {
		SafeUser user = (SafeUser) request.getSession().getAttribute(USER_LOGIN_STATE);
		if (user == null) {
			throw new BusinessException(ErrorCode.NOT_LOGIN);
		}
		return Result.success(ErrorCode.SUCCESS, user);
	}

	@PostMapping("/profile/update")
	public Result<SafeUser> updateProfile(@RequestBody UserProfileUpdateRequest updateRequest,
			HttpServletRequest request) {
		SafeUser loginUser = (SafeUser) request.getSession().getAttribute(USER_LOGIN_STATE);
		if (loginUser == null) {
			throw new BusinessException(ErrorCode.NOT_LOGIN);
		}
		if (updateRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}

		String username = StringUtils.trimToNull(updateRequest.getUsername());
		String avatarUrl = StringUtils.trimToNull(updateRequest.getAvatarUrl());
		String phone = StringUtils.trimToNull(updateRequest.getPhone());
		String email = StringUtils.trimToNull(updateRequest.getEmail());
		Integer gender = updateRequest.getGender();
		if (StringUtils.length(username) > 50 || StringUtils.length(avatarUrl) > 1024 || StringUtils.length(phone) > 128
				|| StringUtils.length(email) > 512 || (gender != null && (gender < 0 || gender > 2))) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		if (email != null && !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式不正确");
		}

		User user = userService.getById(loginUser.getId());
		if (user == null) {
			throw new BusinessException(ErrorCode.NOT_LOGIN);
		}
		user.setUsername(username);
		user.setAvatarUrl(avatarUrl);
		user.setGender(gender);
		user.setPhone(phone);
		user.setEmail(email);
		if (!userService.updateById(user)) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR);
		}

		SafeUser safeUser = userService.getSafeUser(user);
		request.getSession().setAttribute(USER_LOGIN_STATE, safeUser);
		return Result.success(ErrorCode.SUCCESS, safeUser);
	}

	/**
	 * 用户登出
	 * @param request HTTP请求对象，用于获取当前会话
	 * @return 统一返回结果封装，由于登出通常不返回具体业务数据，泛型使用 Void
	 */
	@PostMapping("/logout")
	public Result<Void> userLogOut(HttpServletRequest request) {
		if (request == null) {
			throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
		}
		userService.userLogOut(request);

		return Result.success(ErrorCode.SUCCESS);
	}

	@GetMapping("/search")
	public Result<List<SafeUser>> searchUsersByName(String username, HttpServletRequest request) {
		if (noAdmin(request)) {
			throw new BusinessException(ErrorCode.NO_AUTH);
		}
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(username)) {
			queryWrapper.and(wrapper -> wrapper.like("username", username).or().like("userAccount", username));
		}
		// 管理员列表也只返回脱敏字段，避免 userPassword 等实体字段出现在响应中。
		List<User> list = userService.list(queryWrapper);
		return Result.success(ErrorCode.SUCCESS, list.stream().map(user -> userService.getSafeUser(user)).toList());
	}

	@PostMapping("/delete")
	public Result<Boolean> deleteUser(@RequestBody UserDeleteRequest deleteRequest, HttpServletRequest request) {
		if (noAdmin(request)) {
			throw new BusinessException(ErrorCode.NO_AUTH);
		}
		SafeUser loginUser = (SafeUser) request.getSession().getAttribute(USER_LOGIN_STATE);
		Long userId = deleteRequest == null ? null : deleteRequest.getId();
		if (userId == null || userId <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		if (userId.equals(loginUser.getId())) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能删除当前登录的管理员账号");
		}
		if (!userService.removeById(userId)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		return Result.success(ErrorCode.SUCCESS);
	}

	private boolean noAdmin(HttpServletRequest request) {
		SafeUser user = (SafeUser) request.getSession().getAttribute(USER_LOGIN_STATE);
		return user == null || user.getUserRole() != ADMIN_ROLE;
	}

}
