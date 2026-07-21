package com.example.ch04.controller;

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

    private final SystemMessageService service;

    @GetMapping("/ai/system-message")
    public String systemMessage() {
        return "/system-message";
    }

    @ResponseBody
    @PostMapping("/ai/system-message")
    public String systemMessage(
            @RequestParam("review") String question) {

        return service.chat(question);
    }
}