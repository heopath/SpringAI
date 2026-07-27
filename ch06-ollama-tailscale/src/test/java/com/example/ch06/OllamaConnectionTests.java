package com.example.ch06;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.ai.openai.api-key=test-key")
@EnabledIfEnvironmentVariable(named = "RUN_OLLAMA_INTEGRATION_TEST", matches = "true")
class OllamaConnectionTests {

	@Value("${spring.ai.ollama.base-url}")
	private String baseUrl;

	@Test
	@Timeout(40)
	void reachesOllamaOverTailscale() throws Exception {
		HttpClient client = HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(10))
				.build();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl + "/api/tags"))
				.timeout(Duration.ofSeconds(30))
				.GET()
				.build();

		HttpResponse<String> response = client.send(
				request, HttpResponse.BodyHandlers.ofString());

		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(response.body()).contains("\"models\"");
	}
}
