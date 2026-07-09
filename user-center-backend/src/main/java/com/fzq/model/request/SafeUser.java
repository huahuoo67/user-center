package com.fzq.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 返回给前端的安全用户信息。
 *
 * <p>
 * 这个对象只保留页面展示和权限判断需要的字段，刻意不包含 userPassword、isDelete 等敏感或内部字段。
 */
@Data
public class SafeUser implements Serializable {

	@Serial
	private static final long serialVersionUID = -7581470543618609240L;

	private Long id;

	private String username;

	private String userAccount;

	private String avatarUrl;

	private Integer gender;

	private String phone;

	private String email;

	private Integer userRole;

	private Integer userStatus;

	private Date createTime;

}
