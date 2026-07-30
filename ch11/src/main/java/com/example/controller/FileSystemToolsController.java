package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.service.DateTimeToolsService;
import com.example.service.FileSystemToolsService;
import com.example.service.HeatingSystemToolsService;
import com.example.service.RecommendMovieToolsService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Controller
public class FileSystemToolsController {
	
	private final FileSystemToolsService service;
	
	@GetMapping("/ai/file-system-tools")
	public String fileSystemTools() {
		return "/file-system-tools";
	}
	
	@ResponseBody
	@PostMapping("/ai/file-system-tools")
	public String fileSystemTools(@RequestParam("question") String question, HttpSession session) {
		String answer = service.chat(question, session.getId());
		return answer;
	}
	
}
