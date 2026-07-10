package com.fzq.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 容器健康检查接口。
 */
@RestController
public class HealthController {

	@GetMapping("/health")
	public Map<String, String> health() {
		return Map.of("status", "UP");
	}

}
