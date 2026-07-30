package com.example.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.service.TxtPdfWordEtlService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequiredArgsConstructor
public class TxtPdfWordEtlController {

    private final TxtPdfWordEtlService service;

    @GetMapping("/ai/txt-pdf-word-etl")
    public String etlPage() {
        return "txt-pdf-word-etl";
    }

    @ResponseBody
    @PostMapping(
            value = "/ai/txt-pdf-docx-etl",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public String uploadDocument(
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("attach") MultipartFile attach)
            throws IOException {

        return service.etlFromFile(title, author, attach);
    }
}