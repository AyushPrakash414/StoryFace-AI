package com.storyfaceai.storyfaceai_backend.controller;

import com.storyfaceai.storyfaceai_backend.service.EmotionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class StoryController {
    private final EmotionService emotionService;

    public StoryController(EmotionService emotionService) {
        this.emotionService = emotionService;
    }

    @PostMapping("/story")
    public String generateStory(@RequestParam("file") MultipartFile file) throws Exception {
        return emotionService.processImage(file);
    }
}
