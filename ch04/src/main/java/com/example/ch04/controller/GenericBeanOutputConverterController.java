package com.example.ch04.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GenericBeanOutputConverterController {
	
	@GetMapping("/ai/generic-bean-output-converter")
	public String genericBeanOutputConverter() {
		return "/generic-bean-output-converter";
	}
}
