package com.github.copilot.extensions.copilot.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ChatCompletionRequest {

    private String model;

    private List<ChatCompletionMessage> messages;

    public ChatCompletionRequest(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<ChatCompletionMessage>();
        this.messages.add(new ChatCompletionMessage("user", prompt));
    }


}
