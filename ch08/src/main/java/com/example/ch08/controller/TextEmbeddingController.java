package com.example.ch08.controller;

import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ch08.service.TextEmbeddingService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class TextEmbeddingController {

    private final TextEmbeddingService textEmbeddingService;

    @GetMapping("/ai/text-embedding")
    public String textEmbedding() {
        return "text-embedding";
    }

    @ResponseBody
    @PostMapping(
        value = "/ai/text-embedding",
        produces = "text/plain;charset=UTF-8"
    )
    public String textEmbedding(
            @RequestParam("question") String question) {

        float[] vector = textEmbeddingService.embedding(question);

        return """
               임베딩 차원: %d

               임베딩 결과:
               %s
               """.formatted(
                   vector.length,
                   Arrays.toString(vector)
               );
    }
}