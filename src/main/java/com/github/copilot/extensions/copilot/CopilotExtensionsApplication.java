package com.github.copilot.extensions.copilot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CopilotExtensionsApplication {

	@Value("${api.key}")
	private String apiKeyString;

	public static void main(String[] args) {
		SpringApplication.run(CopilotExtensionsApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		
		RestTemplate restTemplate =  new RestTemplate();
		restTemplate.getInterceptors().add((request, body, execution) -> {
			request.getHeaders().add("Authorization", "Bearer " + apiKeyString);
			return execution.execute(request, body);
		});
		
		return restTemplate;
	}

}
