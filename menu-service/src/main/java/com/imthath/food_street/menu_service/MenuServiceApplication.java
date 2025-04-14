package com.imthath.food_street.menu_service;

import com.imthath.utils.guardrail.GlobalExceptionHandler;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class MenuServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MenuServiceApplication.class, args);
	}

	@Bean
	public GlobalExceptionHandler globalExceptionHandler() { return new GlobalExceptionHandler(); }

	@Bean
	public ToolCallbackProvider menuTools(MenuController menuController) {
		return MethodToolCallbackProvider.builder().toolObjects(menuController).build();
	}
}
