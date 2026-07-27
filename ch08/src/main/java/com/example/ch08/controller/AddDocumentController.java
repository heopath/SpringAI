package com.example.ch08.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ch08.service.AddDocumentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AddDocumentController {

    private final AddDocumentService service;

    @GetMapping("/ai/add-document")
    public String addDocumentPage() {
        return "add-document";
    }

	@ResponseBody
	@PostMapping(
			value = "/ai/add-document",
			produces = "text/plain;charset=UTF-8")
	public String addDocument(@RequestParam("question") String question) {
		int count = service.addDocument();
		return "쇼핑몰 이용안내 Document %d건을 벡터 저장소에 저장했습니다.".formatted(count);
	}
}
