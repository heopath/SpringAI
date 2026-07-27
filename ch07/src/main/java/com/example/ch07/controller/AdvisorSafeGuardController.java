package com.example.ch07.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ch07.service.AdvisorChainService;
import com.example.ch07.service.AdvisorSafeGuardService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Controller
public class AdvisorSafeGuardController {
	
	private final AdvisorSafeGuardService service;
	
	@GetMapping("/ai/advisor-safe-guard")
	public String advisorSafeGuard() {
		return "/advisor-safe-guard";
	}
	
	@ResponseBody
	@PostMapping("/ai/advisor-safe-guard")
	public String advisorSafeGuard(@RequestParam("question") String question) {
		
		String answer = service.call(question);
		
		return answer;
	}
	
	
}