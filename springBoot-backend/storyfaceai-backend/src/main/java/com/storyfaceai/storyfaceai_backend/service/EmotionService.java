package com.storyfaceai.storyfaceai_backend.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@Service
public class EmotionService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String processImage(MultipartFile file) throws Exception {

        // Step 1 → call ML service
        String emotion = callMlService(file);

        // Step 2 → call Gemini
        String story = callGemini(emotion);

        return story;
    }

    private String callMlService(MultipartFile file) throws Exception {

        String url = "http://localhost:5000/predict";

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, request, Map.class);

        return response.getBody().get("emotion").toString();
    }

    private String callGemini(String emotion) {

        // For now mock
        return "Today you feel " + emotion + ". A short story begins...";
    }
}

