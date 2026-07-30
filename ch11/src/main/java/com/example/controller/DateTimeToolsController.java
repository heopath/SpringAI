package com.example.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.service.DateTimeToolsService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DateTimeToolsController {

    private final DateTimeToolsService service;

    @GetMapping("/ai/date-time-tools")
    public String dateTimeTools() {
        return "date-time-tools";
    }

    @ResponseBody
    @PostMapping(
            value = "/ai/date-time-tools",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String dateTimeTools(
            @RequestParam("question") String question) {

        return service.chat(question);
    }
}