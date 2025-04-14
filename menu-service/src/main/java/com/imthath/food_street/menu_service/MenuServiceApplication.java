package com.imthath.food_street.menu_service;

import com.imthath.utils.guardrail.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.ai.tool.util.ToolUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootApplication
@EnableMongoAuditing
public class MenuServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MenuServiceApplication.class, args);
	}

	@Bean
	public GlobalExceptionHandler globalExceptionHandler() { return new GlobalExceptionHandler(); }

	@Bean
	public ToolCallbackProvider menuTools(ApplicationContext applicationContext) {
		List<Object> controllers = getRestControllerBeans(applicationContext);
		List<Method> toolMethods = controllers
				.stream()
				.flatMap(obj -> Arrays.stream(obj.getClass().getDeclaredMethods()).filter(this::isRequestMethod))
				.toList();
		log.info("Found {} tool methods from {} controllers", toolMethods.size(), controllers.size());

		List<ToolCallback> toolCallbacks = new ArrayList<>();
		for (Method method: toolMethods) {
			log.info("Tool method: {}.{}()", method.getDeclaringClass().getSimpleName(), method.getName());
			// Register the method as a tool callback
			toolCallbacks.add(toolCallback(method, controllers.getFirst()));
		}
		return ToolCallbackProvider.from(toolCallbacks);
	}

	private MethodToolCallback toolCallback(Method method, Object bean) {
		return MethodToolCallback.builder()
				.toolDefinition(ToolDefinition.from(method))
				.toolMethod(method)
				.toolCallResultConverter(ToolUtils.getToolCallResultConverter(method))
				.toolObject(bean)
				.build();
	}

	private Boolean isRequestMethod(Method method) {
		return method.isAnnotationPresent(RequestMapping.class) ||
			method.isAnnotationPresent(GetMapping.class) ||
			method.isAnnotationPresent(PostMapping.class) ||
			method.isAnnotationPresent(PutMapping.class) ||
			method.isAnnotationPresent(DeleteMapping.class) ||
			method.isAnnotationPresent(PatchMapping.class);
	}

	private List<Object> getRestControllerBeans(ApplicationContext applicationContext) {
		String[] beanNames = applicationContext.getBeanDefinitionNames();
		List<Object> controllers = new ArrayList<>();
		
		// Filter for RestController annotated beans
		for (String beanName : beanNames) {
			// Skip the menuTools bean itself to avoid circular dependency
			if ("menuTools".equals(beanName)) {
				continue;
			}
			
			try {
				Object bean = applicationContext.getBean(beanName);
				if (bean.getClass().isAnnotationPresent(RestController.class)) {
					controllers.add(bean);
				}
			} catch (Exception e) {
				// Skip if there's an error getting the bean
			}
		}
		
		return controllers;
	}
}
