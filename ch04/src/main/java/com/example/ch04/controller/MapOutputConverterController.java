package com.example.ch04.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapOutputConverterController {
	
	@GetMapping("/ai/map-output-converter")
	public String mapOutputConverter() {
		return "/map-output-converter";
	}
}
