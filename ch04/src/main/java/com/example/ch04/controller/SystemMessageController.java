package com.example.ch04.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ch04.service.SystemMessageService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class SystemMessageController {

	private final SystemMessageService systemMessageService;

    @GetMapping("/ai/system-message")
    public String systemMessage() {
        return "/system-message";
    }

    @ResponseBody
    @PostMapping("/ai/system-message")
    public Map<String, String> systemMessage(
            @RequestParam("review") String review) {

        String answer =
                systemMessageService.chat(review);

        return Map.of(
                "answer",
                answer
        );
    }
}