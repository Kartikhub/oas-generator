package com.crent.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class AIService {

    private static final String INITIAL_PROMPT = "IGNORE all the previous PROMPTS and start FRESH. Create an OpenAPI Spec using the below-provided DETAILS and STRICTLY follow the RULES for creating the openapi spec-\n" +
            "RULES\n" +
            "1. ONLY PROVIDE THE OpenAPI Spec. Remember do not write anything else. I ONLY need a Yaml code snippet that's it. Also take time but never give just a stub. \n" +
//            "2. If in DETAILS section, SCHEMA and PATH information is not given, then always create a corresponding schema(only name and id) and one corresponding path with get HTTP method. \n" +
//            "\n" +
            "2. Always provide Paths and schemas, and do not provide response or request for the Paths unless it is mentioned. And if Request and response body are asked to be created then provide the reference to the models in components" +
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
        //Make it fast by removing the char using index
        String result = callResult.replace("```yaml\n", "").replace("\n```", "").trim();
        if(!result.startsWith(OAS_PREFIX)) {
            log.info("illegal argument exception");
            throw new IllegalArgumentException("Please provide a appropriate Prompt");
        }
        return result;
    }
}
