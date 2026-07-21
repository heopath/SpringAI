package com.example.ch04.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ch04.service.ListOutputConverterService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ListOutputConverterController {

    private final ListOutputConverterService
            listOutputConverterService;

    @GetMapping("/ai/list-output-converter")
    public String listOutputConverter() {

        return "/list-output-converter";
    }


    // Low Level
    @ResponseBody
    @PostMapping("/ai/list-output-converter")
    public List<String> listOutputConverter(
            @RequestParam("city") String city) {

        return listOutputConverterService
                .convertLowLevel(city);
    }


    // High Level
    @ResponseBody
    @PostMapping("/ai/list-output-converter/high")
    public List<String> listOutputConverterHigh(
            @RequestParam("city") String city) {

        return listOutputConverterService
                .convertHighLevel(city);
    }
}