package com.example.ch06.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.ch06.service.ImageAnalysisService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Controller
public class ImageAnalysisController {

    private final ImageAnalysisService service;

    @GetMapping("/ai/image-analysis")
    public String imageAnalysis() {
        return "image-analysis";
    }

    @ResponseBody
    @PostMapping(
        value = "/ai/image-analysis",
        produces = MediaType.APPLICATION_NDJSON_VALUE
    )
    public Flux<String> imageAnalysis(
            @RequestParam("question") String question,
            @RequestParam("attach") MultipartFile attach)
            throws IOException {

        return service.analysis(
                question,
                attach.getContentType(),
                attach.getBytes()
        );
    }
}