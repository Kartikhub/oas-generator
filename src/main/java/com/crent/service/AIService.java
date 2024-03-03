package com.crent.service;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class AIService {

    private static final String INITIAL_PROMPT = "Create an OpenAPI Spec using the below-provided DETAILS and STRICTLY follow the RULES for creating the openapi spec-\n" +
            "RULES\n" +
            "1. ONLY PROVIDE THE OpenAPI Spec without any other detail. Remember do not write anything else. I ONLY need a Yaml code snippet that's it. \n" +
            "2. If in DETAIL section, SCHEMA and PATH information is not given, then create a user schema(only name and id) and one corresponding path with get HTTP method. \n" +
            "\n" +
            "EXCEPTION OF RULES-\n" +
            "If in the DETAILS section, the information provided is not about how to create a OpenAPI Spec or there is no detail at all then skip all the Rules and provide the result as ERROR-Not a valid prompt for OpenAPI Spec. - ( THIS WILL BE THE ONLY EXCEPTION OF RULE 1)\n" +
            "\n" +
            "DETAILS-\n";

    private static final String DEFAULT_PROMPT= "Create an OpenAPI Spec which has below details\n" +
            "1. Schemas\n" +
            "  a. Car having properties like engine string type, speed integer type, id integer type\n" +
            "2. Paths\n" +
            "a. Post method to add car \n" +
            "b. get method to get the car details using the car id\n" +
            "\n" +
            "ONLY PROVIDE THE OpenAPI Spec without any other detail. Remember do not write anything else. I ONLY need a String of it that's it";

    private static final String OAS_PREFIX = "openapi";

    private final ChatClient chatClient;

    @Autowired
    public AIService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    public String getOpenAPISpec(String message) {
        String promptMessage = INITIAL_PROMPT + message;
        String callResult = chatClient.call(promptMessage);
        log.info("Result- {}", callResult);
        String result = callResult.replace("```yaml\n", "").replace("\n```", "");
        if(!result.startsWith(OAS_PREFIX)); {
            throw new IllegalArgumentException(message);
        }
    }
}
