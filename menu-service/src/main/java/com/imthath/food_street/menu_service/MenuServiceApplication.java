package com.imthath.food_street.menu_service;

import com.imthath.utils.guardrail.GlobalExceptionHandler;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
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

import java.util.ArrayList;
import java.util.List;

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
		
		return MethodToolCallbackProvider.builder()
			.toolObjects(controllers.toArray())
			.build();
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

	private void printRequestMappingInfo(ApplicationContext context) {
		System.out.println("\n=== Request Mapping Information ===");
		List<Object> controllers = getRestControllerBeans(context);
		for (Object bean : controllers) {
			Class<?> beanClass = bean.getClass();
			System.out.println("\nController: " + beanClass.getSimpleName());
			// Get class-level RequestMapping
			RequestMapping classMapping = beanClass.getAnnotation(RequestMapping.class);
			String basePath = "";
			if (classMapping != null && classMapping.value().length > 0) {
				basePath = classMapping.value()[0];
			}
			// Get all methods with RequestMapping annotations
			for (Method method : beanClass.getDeclaredMethods()) {
				String path = basePath;
				String httpMethod = "UNKNOWN";
				if (method.isAnnotationPresent(RequestMapping.class)) {
					RequestMapping mapping = method.getAnnotation(RequestMapping.class);
					if (mapping.value().length > 0) {
						path += mapping.value()[0];
					}
					if (mapping.method().length > 0) {
						httpMethod = mapping.method()[0].name();
					}
				} else if (method.isAnnotationPresent(GetMapping.class)) {
					GetMapping mapping = method.getAnnotation(GetMapping.class);
					if (mapping.value().length > 0) {
						path += mapping.value()[0];
					}
					httpMethod = "GET";
				} else if (method.isAnnotationPresent(PostMapping.class)) {
					PostMapping mapping = method.getAnnotation(PostMapping.class);
					if (mapping.value().length > 0) {
						path += mapping.value()[0];
					}
					httpMethod = "POST";
				} else if (method.isAnnotationPresent(PutMapping.class)) {
					PutMapping mapping = method.getAnnotation(PutMapping.class);
					if (mapping.value().length > 0) {
						path += mapping.value()[0];
					}
					httpMethod = "PUT";
				} else if (method.isAnnotationPresent(DeleteMapping.class)) {
					DeleteMapping mapping = method.getAnnotation(DeleteMapping.class);
					if (mapping.value().length > 0) {
						path += mapping.value()[0];
					}
					httpMethod = "DELETE";
				} else if (method.isAnnotationPresent(PatchMapping.class)) {
					PatchMapping mapping = method.getAnnotation(PatchMapping.class);
					if (mapping.value().length > 0) {
						path += mapping.value()[0];
					}
					httpMethod = "PATCH";
				}
				if (!httpMethod.equals("UNKNOWN")) {
					System.out.printf("  %-7s %s%n", httpMethod, path);
				}
			}
		}
		System.out.println("\n================================");
	}
}
