package com.example.ch07.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ch07.service.AdvisorChainService;
import com.example.ch07.service.AdvisorContextService;
import com.example.ch07.service.AdvisorLoggingService;
import com.example.ch07.service.AdvisorSafeGuardService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Controller
public class AdvisorContextController {
	
	private final AdvisorContextService service;
	
	@GetMapping("/ai/advisor-context")
	public String advisorContext() {
		return "/advisor-context";
	}
	
	@ResponseBody
	@PostMapping("/ai/advisor-context")
	public String advisorContext(@RequestParam("question") String question) {		
		String answer = service.call(question);		
		return answer;
	}
	
	
}
