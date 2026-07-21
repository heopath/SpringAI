package com.example.ch04.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BeanOutputConverterController {
	
	@GetMapping("/ai/bean-output-converter")
	public String beanOutputConverter() {
		return "/bean-output-converter";
	}
}
