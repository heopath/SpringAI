package com.example.ch06.controller;

import java.io.IOException;

import javax.print.DocFlavor.STRING;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.ch06.service.ImageAnalysisService;
import com.example.ch06.service.ImageGenerationService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Controller
public class ImageGenerationController {
	
	private final ImageGenerationService service;
		
	@GetMapping("/ai/image-generation")
	public String imageGeneration() {
		return "/image-generation";
	}
	
	
	@ResponseBody
	@PostMapping(
			value = "/ai/image-generate",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
	)
	public String imageGeneration(@RequestParam("description") String description) {
		
		String b64Json = service.generation(description);
				
		return b64Json;
	}
}











