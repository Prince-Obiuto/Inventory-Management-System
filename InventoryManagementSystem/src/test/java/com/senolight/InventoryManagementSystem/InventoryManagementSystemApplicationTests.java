package com.senolight.InventoryManagementSystem;

import com.senolight.InventoryManagementSystem.security.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Import(TestSecurityConfig.class)
class InventoryManagementSystemApplicationTests {

	@Test
	void contextLoads() {
	}

}
