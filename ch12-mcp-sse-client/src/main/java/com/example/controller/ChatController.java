package com.example.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Controller
public class ChatController {

	private final ChatService chatService;

	@GetMapping("/ai/chat")
	public String chat() {
		return "/chat";
	}

	@ResponseBody
	@PostMapping(
			value = "/ai/chat",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
	)
	public String chat(
			@RequestParam("question") String question) {

		log.info("질문 요청: {}", question);

		return chatService.chat(question);
	}
}