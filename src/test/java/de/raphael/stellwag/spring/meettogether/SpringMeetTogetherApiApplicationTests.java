package de.raphael.stellwag.spring.meettogether;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SpringMeetTogetherApiApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringMeetTogetherApiApplicationTests {

	@Test
	void contextLoads() {
		Assert.assertTrue(true);
	}

}
