package com.example.ch07.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ch07.service.AdvisorChainService;

@Controller
public class AdvisorLoggingController {

	AdvisorChainService service;
	
	@GetMapping("/ai/advisor-logging")
	public String advisorChain() {
		
		return "advisor-logging";
	}
	
	@ResponseBody
	@PostMapping("/ai/advisor-logging/")
	public String advisorChain(@RequestParam("question") String question) {
		
		String answer = service.call(question);
		
		return answer;
	}
	
}
