package com.github.copilot.extensions.copilot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CopilotExtensionChatResponse {

    private String id;
    private String object;
    private long created;
    private String model;
    private String systemFingerprint;
    private List<Choice> choices;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        private int index;
        private Delta delta;
        private Object logprobs;
        private Object finishReason;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Delta {
            private String content;
        }

}

}
