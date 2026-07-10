package com.fzq.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 当前登录用户可修改的个人资料。
 */
@Data
public class UserProfileUpdateRequest implements Serializable {

	@Serial
	private static final long serialVersionUID = 4544697917389058976L;

	private String username;

	private String avatarUrl;

	private Integer gender;

	private String phone;

	private String email;

}
