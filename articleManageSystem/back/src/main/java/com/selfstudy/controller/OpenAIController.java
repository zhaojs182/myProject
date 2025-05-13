package com.selfstudy.controller;

import com.selfstudy.service.OpenAIService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/openai")
public class OpenAIController {

    private final OpenAIService openAIService;

    public OpenAIController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/ask")
    public String askOpenAI(@RequestBody String userQuestion) {
        return openAIService.getResponseFromOpenAI(userQuestion);
    }
}
