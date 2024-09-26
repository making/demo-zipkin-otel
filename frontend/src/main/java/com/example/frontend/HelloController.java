package com.example.frontend;

import io.micrometer.observation.annotation.Observed;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClient;

@Controller
public class HelloController {

	private final RestClient restClient;

	public HelloController(RestClient.Builder restClientBuilder, BackendProps props,
			LogbookClientHttpRequestInterceptor logbookClientHttpRequestInterceptor) {
		this.restClient = restClientBuilder.requestInterceptor(logbookClientHttpRequestInterceptor)
			.baseUrl(props.url())
			.build();
	}

	@GetMapping(path = "/")
	@Observed
	public String index(Model model) {
		String message = this.restClient.get().uri("/").retrieve().body(String.class);
		model.addAttribute("message", message);
		return "index";
	}

}
