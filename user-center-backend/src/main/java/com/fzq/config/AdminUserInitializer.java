package com.fzq.config;

import com.fzq.model.entity.User;
import com.fzq.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import static com.fzq.contant.UserContant.ADMIN_ROLE;

/**
 * 管理员账号初始化。
 *
 * <p>
 * 仅在账号不存在时创建管理员，不会在应用重启时重置已有管理员的密码。
 */
@Component
@Slf4j
@ConditionalOnProperty(name = "app.admin.enabled", havingValue = "true")
public class AdminUserInitializer implements CommandLineRunner {

	private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

	@Resource
	private UserService userService;

	@Value("${app.admin.account}")
	private String adminAccount;

	@Value("${app.admin.password}")
	private String adminPassword;

	@Override
	public void run(String... args) {
		if (adminAccount == null || !adminAccount.matches("^[a-zA-Z0-9_]{4,18}$")) {
			throw new IllegalStateException("ADMIN_ACCOUNT 必须为 4-18 位字母、数字或下划线");
		}
		if (adminPassword == null || adminPassword.length() < 8) {
			throw new IllegalStateException("ADMIN_PASSWORD 不能少于 8 位");
		}
		User adminUser = userService.lambdaQuery().eq(User::getUserAccount, adminAccount).one();
		if (adminUser != null) {
			log.info("管理员账号 {} 已存在，跳过初始化", adminAccount);
			return;
		}
		createAdminUser();
	}

	private void createAdminUser() {
		User adminUser = new User();
		adminUser.setUsername("系统管理员");
		adminUser.setUserAccount(adminAccount);
		adminUser.setUserPassword(PASSWORD_ENCODER.encode(adminPassword));
		adminUser.setUserRole(ADMIN_ROLE);
		adminUser.setUserStatus(0);
		if (!userService.save(adminUser)) {
			throw new IllegalStateException("初始化管理员账号失败");
		}
		log.info("管理员账号 {} 初始化成功", adminAccount);
	}

}
