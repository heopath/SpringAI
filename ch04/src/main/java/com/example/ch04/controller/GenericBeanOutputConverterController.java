package com.example.ch04.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ch04.dto.Hotel;
import com.example.ch04.service.GenericBeanOutputConverterService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class GenericBeanOutputConverterController {

    private final GenericBeanOutputConverterService service;

    @GetMapping("/ai/generic-bean-output-converter")
    public String genericBeanOutputConverter() {
        return "/generic-bean-output-converter";
    }

    @ResponseBody
    @PostMapping("/ai/generic-bean-output-converter")
    public List<Hotel> genericBeanOutputConverter(
            @RequestParam("cities") String city) {

        return service.convert(city);
    }
}