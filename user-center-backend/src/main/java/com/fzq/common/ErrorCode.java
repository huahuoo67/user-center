package com.fzq.common;

public enum ErrorCode {

	SUCCESS("200", "OK"), PARAMS_ERROR("40000", "请求参数错误"), PARAMS_NULL_ERROR("40001", "请求数据为空"),
	NOT_LOGIN("40100", "未登录"), NO_AUTH("40101", "无权限"), SYSTEM_ERROR("50000", "系统内部异常");

	private final String Code;

	private final String Message;

	ErrorCode(String Code, String Message) {
		this.Code = Code;
		this.Message = Message;
	}

	public String getCode() {
		return Code;
	}

	public String getMessage() {
		return Message;
	}

}
