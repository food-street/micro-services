package com.imthath.food_street.court_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CourtServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
