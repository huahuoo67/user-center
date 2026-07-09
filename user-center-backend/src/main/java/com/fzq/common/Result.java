package com.fzq.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

	@Serial
	private static final long serialVersionUID = 2998283936910030410L;

	/**
	 * 状态码
	 */
	private String code;

	/**
	 * 提示信息
	 */
	private String message;

	/**
	 * 返回体
	 */
	private T data;

	private Result() {
	}

	private Result(String code, String message) {
		this.code = code;
		this.message = message;
		this.data = null;
	}

	private Result(String code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public static <T> Result<T> success(String message) {
		return new Result<>("200", message);
	}

	public static <T> Result<T> success(String message, T data) {
		return new Result<>("200", message, data);
	}

	public static <T> Result<T> success(ErrorCode errorCode) {
		return new Result<>(errorCode.getCode(), errorCode.getMessage());
	}

	public static <T> Result<T> success(ErrorCode errorCode, T data) {
		return new Result<>(errorCode.getCode(), errorCode.getMessage(), data);
	}

	public static <T> Result<T> error(ErrorCode errorCode) {
		return new Result<>(errorCode.getCode(), errorCode.getMessage());
	}

	public static <T> Result<T> error(String code, String message) {
		return new Result<>(code, message);
	}

}