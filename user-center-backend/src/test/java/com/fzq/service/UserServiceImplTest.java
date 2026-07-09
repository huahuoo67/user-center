package com.fzq.service;

import com.fzq.common.ErrorCode;
import com.fzq.exception.BusinessException;
import com.fzq.model.entity.User;
import com.fzq.model.request.SafeUser;
import com.fzq.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class UserServiceImplTest {

	/**
	 * 这些测试只覆盖纯参数校验和脱敏逻辑，避免依赖本地 MySQL，保证任意环境都能快速运行。
	 */
	private final UserServiceImpl userService = new UserServiceImpl();

	@Test
	void userRegisterShouldRejectBlankParams() {
		// 空账号属于参数缺失，应该抛出统一业务异常。
		BusinessException exception = Assertions.assertThrows(BusinessException.class,
				() -> userService.userRegister("", "password123", "password123"));

		Assertions.assertEquals(ErrorCode.PARAMS_NULL_ERROR.getCode(), exception.getCode());
	}

	@Test
	void userRegisterShouldRejectInvalidAccount() {
		// 账号长度不满足规则时，不应该继续进入查库或保存逻辑。
		BusinessException exception = Assertions.assertThrows(BusinessException.class,
				() -> userService.userRegister("ab", "password123", "password123"));

		Assertions.assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
	}

	@Test
	void userRegisterShouldRejectPasswordMismatch() {
		// 两次密码不一致是常见注册错误，服务层需要兜底校验。
		BusinessException exception = Assertions.assertThrows(BusinessException.class,
				() -> userService.userRegister("testUser", "password123", "password456"));

		Assertions.assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
	}

	@Test
	void getSafeUserShouldHideSensitiveFields() {
		// 构造包含密码的实体，验证脱敏 DTO 不会暴露 userPassword 字段。
		User user = new User();
		user.setId(1L);
		user.setUsername("tester");
		user.setUserAccount("testUser");
		user.setUserPassword("secret-password");
		user.setUserRole(1);

		SafeUser safeUser = userService.getSafeUser(user);

		Assertions.assertEquals(user.getId(), safeUser.getId());
		Assertions.assertEquals(user.getUserAccount(), safeUser.getUserAccount());
		Assertions.assertEquals(user.getUserRole(), safeUser.getUserRole());
		Assertions.assertFalse(Arrays.stream(SafeUser.class.getDeclaredFields())
			.anyMatch(field -> "userPassword".equals(field.getName())));
	}

}
