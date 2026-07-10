package com.fzq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzq.common.ErrorCode;
import com.fzq.exception.BusinessException;
import com.fzq.mapper.UserMapper;
import com.fzq.model.entity.User;
import com.fzq.model.request.SafeUser;
import com.fzq.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.fzq.contant.UserContant.USER_LOGIN_STATE;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

	private static final String USER_ACCOUNT_PATTERN = "^[a-zA-Z0-9_]{4,18}$";

	@Override
	public long userRegister(String userAccount, String username, String userPassword, String checkPassword) {
		if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
			throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
		}
		if (!isValidUserAccount(userAccount)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		if (userPassword.length() < 8 || checkPassword.length() < 8) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		if (!checkPassword.equals(userPassword)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		if (StringUtils.length(username) > 50) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "昵称不能超过 50 个字符");
		}

		// 先完成本地参数校验，再查库，减少无效数据库访问。
		if (this.query().eq("userAccount", userAccount).exists()) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");
		}

		User user = new User();
		user.setUserAccount(userAccount);
		user.setUsername(StringUtils.trimToNull(username));
		user.setUserPassword(PASSWORD_ENCODER.encode(userPassword));
		boolean result = this.save(user);
		if (!result) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR);
		}
		return user.getId();
	}

	@Override
	public SafeUser userLogin(String userAccount, String userPassword, HttpServletRequest request) {
		if (StringUtils.isAnyBlank(userAccount, userPassword)) {
			throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
		}
		if (!isValidUserAccount(userAccount) || userPassword.length() < 8) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}

		User user = this.lambdaQuery().eq(User::getUserAccount, userAccount).one();
		if (user == null || !PASSWORD_ENCODER.matches(userPassword, user.getUserPassword())) {
			log.info("user login failed,userAccount cannot match userPassword");
			throw new BusinessException(ErrorCode.NOT_LOGIN);
		}

		SafeUser safeUser = getSafeUser(user);

		// Session 只保存脱敏用户，避免后续误把密码等实体字段暴露给前端。
		if (request != null) {
			HttpSession session = request.getSession();
			session.setAttribute(USER_LOGIN_STATE, safeUser);
			log.info("Session设置成功，Session ID: {}", session.getId());
		}
		else {
			log.error("HttpServletRequest为null，无法设置Session");
		}
		return safeUser;

	}

	@Override
	public SafeUser getSafeUser(User originUser) {
		if (originUser == null) {
			throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
		}
		SafeUser safeUser = new SafeUser();
		safeUser.setId(originUser.getId());
		safeUser.setUsername(originUser.getUsername());
		safeUser.setUserAccount(originUser.getUserAccount());
		safeUser.setAvatarUrl(originUser.getAvatarUrl());
		safeUser.setGender(originUser.getGender());
		safeUser.setPhone(originUser.getPhone());
		safeUser.setEmail(originUser.getEmail());
		safeUser.setUserRole(originUser.getUserRole());
		safeUser.setUserStatus(originUser.getUserStatus());
		safeUser.setCreateTime(originUser.getCreateTime());

		return safeUser;
	}

	@Override
	public void userLogOut(HttpServletRequest request) {
		request.getSession().invalidate();
	}

	private boolean isValidUserAccount(String userAccount) {
		// 账号只允许字母、数字、下划线，长度 4-18，方便前后端保持一致校验。
		return userAccount != null && userAccount.matches(USER_ACCOUNT_PATTERN);
	}

}
