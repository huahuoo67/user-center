package com.fzq.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {

	@Serial
	private static final long serialVersionUID = -4629743970878075169L;

	private String userAccount;

	private String userPassword;

	private String checkPassword;

}
