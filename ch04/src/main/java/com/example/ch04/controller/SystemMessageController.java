package com.example.ch04.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SystemMessageController {
	
	@GetMapping("/ai/system-message")
	public String systemMessage() {
		return "/system-message";
	}
}
