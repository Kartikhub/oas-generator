package com.crent.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class AIService {
    private static final SystemMessage SYSTEM_MESSAGE = new SystemMessage(
            "1. You are required to create an OpenAPI Spec and nothing else.\n" +
                    "2. Your output should strictly adhere to the provided rules.\n" +
                    "3. Provide only a YAML code snippet of the OpenAPI Spec containing Paths, schemas, and components.\n" +
                    "4. Do not include response or request bodies for the Paths unless specified; in such cases, provide references to the models in components.\n" +
                    "5. If the provided details do not pertain to creating an OpenAPI Spec, or if there's no detail at all, skip all rules and output \"ERROR-Not a valid prompt for OpenAPI Spec.\"\n" +
                    "6. Clearly specify the required OpenAPI version (e.g., OpenAPI 3.x).\n" +
                    "7. Include information about the base path and any API servers if applicable.\n" +
                    "8. Clearly define the HTTP methods (GET, POST, PUT, DELETE, etc.) for each path.\n" +
                    "9. Specify the expected data types and formats for request and response payloads.\n" +
                    "10. Clearly mention any authentication mechanisms or security requirements.\n" +
                    "11. If applicable, include details about query parameters, path parameters, and request headers.\n" +
                    "12. Clearly define the status codes and their meanings for each path.\n" +
                    "13. Use appropriate OpenAPI conventions for naming and structuring components.\n" +
                    "14. Ensure consistency in the naming of paths, parameters, description, summary and schemas.\n" +
                    "15. Handle edge cases or error scenarios if specified in the DETAILS section.\n" +
                    "16. If there are specific rules or conventions to follow in the DETAILS section, incorporate them in the OpenAPI Spec.\n" +
                    "\n" +
                    "DETAILS-\n"
    );

    private static final String OAS_PREFIX = "openapi";

    private final ChatClient chatClient;

    @Autowired
    public AIService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    public String getOpenAPISpec(String message) {
        Prompt prompt = new Prompt(List.of(SYSTEM_MESSAGE, new UserMessage(message)));
        ChatResponse chatResponse = chatClient.call(prompt);
        String rawMessage = chatResponse.getResults().get(0).getOutput().getContent();
        log.info("Result {}: ", rawMessage);
        String result = rawMessage.replace("```yaml\n", "").replace("\n```", "").trim();
        if(!result.startsWith(OAS_PREFIX)) {
            log.info("Illegal Argument Exception - Provided prompt was not correct.");
            throw new IllegalArgumentException("Please provide a appropriate Prompt");
        }
        return result;
    }
}
