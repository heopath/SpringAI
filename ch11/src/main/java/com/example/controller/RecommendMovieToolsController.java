package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.service.RecommendMovieToolsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class RecommendMovieToolsController {

    private final RecommendMovieToolsService service;

    @GetMapping("/ai/recommend-movie-tools")
    public String recommendMovieTools() {
        return "/recommend-movie-tools";
    }

    @ResponseBody
    @PostMapping("/ai/recommend-movie-tools")
    public String recommendMovieTools(
            @RequestParam("question") String question) {

        return service.chat(question);
    }
}