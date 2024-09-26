package com.example.backend;

import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import io.micrometer.observation.annotation.Observed;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class HelloController {

	private final RestClient restClient;

	public HelloController(RestClient.Builder restClientBuilder,
			LogbookClientHttpRequestInterceptor logbookClientHttpRequestInterceptor) {
		this.restClient = restClientBuilder.requestInterceptor(logbookClientHttpRequestInterceptor).build();
	}

	@GetMapping(path = "/")
	@Observed
	public String hello() {
		JsonNode response = this.restClient.post()
			.uri("https://httpbin.org/post")
			.body(Map.of("message", "Hello World!"))
			.contentType(MediaType.APPLICATION_JSON)
			.retrieve()
			.body(JsonNode.class);
		return Objects.requireNonNull(response).get("json").get("message").asText();
	}

}
