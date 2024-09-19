package com.github.copilot.extensions.copilot.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.github.copilot.extensions.copilot.helper.AdvisoryDataExtractor;
import com.github.copilot.extensions.copilot.model.ChatCompletionRequest;
import com.github.copilot.extensions.copilot.model.ChatCompletionResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class MainController {

    @Autowired
    RestTemplate restTemplate;

    @Value("${api.key}")
	private String apiKeyString;

    private String contextString = ". Extract only the package name and version from the promt and respond in a json format. Package name key should be packageName and version name key should be version";

    @PostMapping("/copilotGHASExtension")
    public String getCopilotExtensionResponse(@RequestBody String prompt) {

        ChatCompletionRequest chatCompletionRequest = new ChatCompletionRequest("gpt-4o-mini", prompt+contextString);

        ChatCompletionResponse response = restTemplate.postForObject(
                "https://models.inference.ai.azure.com/chat/completions", chatCompletionRequest,
                ChatCompletionResponse.class);

        System.out.println("Message rfeceived from Open AI service is : "
                + response.getChoices().get(0).getMessage().getContent());

        String message = response.getChoices().get(0).getMessage().getContent();

        String messageToReturn = message.replace("```json", "").replace("```", "");

        System.out.println("Formatted data is : " + messageToReturn);

        JSONObject jsonObject = new JSONObject(messageToReturn);
        // Extract the values associated with keys
        String packageName = jsonObject.getString("packageName");
        String version = jsonObject.getString("version");

        // Print the extracted values
        System.out.println("Package Name: " + packageName);
        System.out.println("Version: " + version);


        String advisoryResponse = getSecurityAdvisories(packageName, version);
        System.out.println("Advisory is : " + advisoryResponse);

        AdvisoryDataExtractor dataExtractor = new AdvisoryDataExtractor();
        String advisoryMessage = dataExtractor.getSecurityAdvisories(advisoryResponse);

        System.out.println("Markdown data is : " + advisoryMessage);


        HttpHeaders headers = new HttpHeaders();
        //headers.add("Custom-Header", "CustomHeaderValue");
       headers.add("Content-Type", "application/json");

        //return new ResponseEntity<>(data, headers, HttpStatus.OK);
        //return advisoryMessage;

        String dataStop = "{"
    + "\"id\": \"chatcmpl-123\","
    + "\"object\": \"chat.completion.chunk\","
    + "\"created\": " + System.currentTimeMillis() + ","
    + "\"model\": \"gpt-4-1106-preview\","
    + "\"system_fingerprint\": \"fp_44709d6fcb\","
    + "\"choices\": ["
    + "  {"
    + "    \"index\": 0,"
    + "    \"delta\": {"
    + "      \"content\": "+"`test`"
    + "    },"
    + "    \"logprobs\": null,"
    + "    \"finish_reason\": \"stop\""
    + "  }"
    + "]"
    + "}";

    //System.out.println("`data:"+dataStop.toString()+"\n\n`");
        
        return advisoryMessage;

    }
    
    public String getSecurityAdvisories(String packageName, String version) {
        String url = "https://api.github.com/advisories?affects="+packageName+"@"+version;
        
        RestTemplate restTemplate =  new RestTemplate();
		restTemplate.getInterceptors().add((request, body, execution) -> {
			request.getHeaders().add("Authorization", "Bearer " + apiKeyString);
            request.getHeaders().add("Accept", "application/vnd.github+json");
            request.getHeaders().add("X-GitHub-Api-Version", "2022-11-28");

			return execution.execute(request, body);
		});

        String advisoryResponse = restTemplate.getForObject(url, String.class);

        return advisoryResponse;
    }

}   
