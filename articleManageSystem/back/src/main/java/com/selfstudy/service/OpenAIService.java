package com.selfstudy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;


    private final RestTemplate restTemplate;

    public OpenAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getResponseFromOpenAI(String userQuestion) {
        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        // 构建请求体
        String requestBody = "{\n" +
                "  \"model\": \"moonshot-v1-8k\",\n" +
                "  \"messages\": [\n" +
                "    { \"role\": \"system\", \"content\": \"你是 Kimi，由 Moonshot AI 提供的人工智能助手，你更擅长中文和英文的对话。\" },\n" +
                "    { \"role\": \"user\", \"content\": \"" + userQuestion + "\" }\n" +
                "  ],\n" +
                "  \"temperature\": 0.3\n" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // 发送请求到 OpenAI API
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        // 提取和返回答案
        String responseBody = response.getBody();
        // 解析返回的 JSON 来提取答案（假设返回是 JSON 格式）
        return responseBody;  // 可以进一步处理 JSON 格式的返回
    }
}
