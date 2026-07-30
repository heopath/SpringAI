package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.service.CassandraChatService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequiredArgsConstructor
public class CassandraChatController {

    private final CassandraChatService service;

    @GetMapping("/ai/cassandra-chat")
    public String cassandraChat() {
        return "cassandra-chat";
    }

    @ResponseBody
    @PostMapping("/ai/cassandra-chat")
    public String cassandraChat(
            @RequestParam("question") String question,
            HttpSession session) {

        String sessionId = session.getId();

        log.info("sessionId: {}", sessionId);
        log.info("question: {}", question);

        return service.chat(question, sessionId);
    }
}