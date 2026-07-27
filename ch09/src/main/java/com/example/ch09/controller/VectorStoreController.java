package com.example.ch09.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ch09.service.InMemoryChatService;
import com.example.ch09.service.VectorStoreService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequiredArgsConstructor
public class VectorStoreController {

	private final VectorStoreService chatService;
	
	@GetMapping("/ai/vector-store-chat")
	public String vectorStoreChat() {
		return "/vector-store-chat";
	}
	
	@ResponseBody
	@PostMapping("/ai/vector-store-chat")
	public String vectorStoreChat(@RequestParam("question") String question, HttpSession session) {
		
		String sessionId = session.getId();
        log.info("question: {}", question);
        
        return chatService.chat(question, sessionId);
	}
	
}
