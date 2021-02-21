package de.raphael.stellwag.spring.meettogether;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringMeetTogetherApiApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringMeetTogetherApiApplicationTests {

	@Test
	void contextLoads() {
		Assert.assertTrue(true);
	}

}
