package com.example.ch08.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.ch08.service.AddDocumentService;
import com.example.ch08.service.ImageEmbeddingService;
import com.example.ch08.service.TextEmbeddingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Controller
public class ImageEmbeddingController {
	
	private final ImageEmbeddingService service;
	
	@GetMapping("/ai/image-embedding")
	public String imageEmbedding() {
		return "/image-embedding";
	}
	
	@ResponseBody
	@PostMapping("/ai/add-face")
	public String addFace(@RequestParam("personName") String personName,
						  @RequestParam("attach") MultipartFile[] attach) throws IOException {
		
		for(MultipartFile mfile : attach) {
			service.addFace(personName, mfile);	
		}
		
		
		return "얼굴을 저장했습니다.";
	}
	
	@ResponseBody
	@PostMapping("/ai/find-face")
	public String findFace(@RequestParam("attach") MultipartFile attach) throws IOException {		
		String personName = service.findFace(attach);
		
		return personName;
	}
	

}







