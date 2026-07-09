package com.fzq.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {

	@Serial
	private static final long serialVersionUID = 4512393019248514513L;

	String userAccount;

	String userPassword;

}
