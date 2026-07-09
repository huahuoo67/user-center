package com.fzq.exception;

import com.fzq.common.ErrorCode;
import com.fzq.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public Result<?> businessExceptionHandler(BusinessException e) {
		log.warn("business exception: {}", e.getMessage());
		return Result.error(e.getCode(), e.getMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	public Result<?> runtimeExceptionHandler(RuntimeException e) {
		log.error("runtimeException", e);
		return Result.error(ErrorCode.SYSTEM_ERROR);
	}

}
