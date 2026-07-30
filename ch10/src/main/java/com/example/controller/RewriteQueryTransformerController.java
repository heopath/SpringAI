package com.example.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.service.CompressionQueryTransformerService;
import com.example.service.RagService;
import com.example.service.RewriteQueryTransformerService;
import com.example.service.TxtPdfWordEtlService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Controller
public class RewriteQueryTransformerController {

	private final RewriteQueryTransformerService service;
	
	
	@GetMapping("/ai/rewrite-query-transformer")
	public String rewriteQueryTransformer() {		
		return "/rewrite-query-transformer";				
	}
		
	@ResponseBody
	@PostMapping("/ai/rewrite-query-transformer")
	public String rewriteQueryTransformer(@RequestParam("question") String question, 
										      @RequestParam("score") double score, 
										      @RequestParam("source") String source,
										      HttpSession session) {
		
		String answer = service.chatWithRewriteQuery(question, score, source, session.getId());
		
		
		return answer;
	}
}