package com.example.ch05.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.ch05.service.SttTtsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class SttTtsController {

	
	private final SttTtsService service;
	
	@GetMapping("/ai/stt-tts")
	public String sttTts() {
		return "/stt-tts";
	}
	
	
	@ResponseBody
	@PostMapping("/ai/stt")
	public String stt(@RequestParam("speech") MultipartFile speech) throws IOException {
	
		String fname = speech.getOriginalFilename();
		byte[] bytes = speech.getBytes();
		
		String text = service.stt(fname, bytes);
				
		return text;
	}
	
	@ResponseBody
	@PostMapping("/ai/tts")
	public byte[] tts(@RequestParam("text") String text) throws IOException {					
		byte[] bytes = service.tts(text);				
		return bytes;
	}
	
	
	
}
