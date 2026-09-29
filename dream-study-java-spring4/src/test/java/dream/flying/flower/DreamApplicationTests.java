package dream.flying.flower;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.RestTestClient;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class DreamApplicationTests {

	@Autowired
	private RestTestClient restTestClient;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void contextLoads() {
		restTestClient.get()
				.uri("/api/users/1")
				.header("X-API-Version", "1")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody()
				.jsonPath("$.name")
				.isEqualTo("张三");

		webTestClient.get()
				.uri("/api/users/1")
				.header("X-API-Version", "1")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody()
				.jsonPath("$.name")
				.isEqualTo("张三");
	}
}