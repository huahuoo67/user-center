package com.fzq.exception;

import com.fzq.common.ErrorCode;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String code;

	private String description;

	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.code = errorCode.getCode();
	}

	public BusinessException(ErrorCode errorCode, String description) {
		super(description);
		this.code = errorCode.getCode();
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

}
