package com.example.ch08.controller;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ch08.service.SearchDocument1Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class SearchDocument1Controller {

    private final SearchDocument1Service service;

    @GetMapping("/ai/search-document-1")
    public String searchDocument1() {
        return "search-document-1";
    }

    @ResponseBody
    @PostMapping(
            value = "/ai/search-document-1",
            produces = "text/html;charset=UTF-8")
    public String searchDocument1(@RequestParam("question") String question) {
        if (question == null || question.isBlank()) {
            return "<div>검색할 질문을 입력하세요.</div>";
        }

        List<Document> documents = service.searchDocument(question.trim());
        if (documents.isEmpty()) {
            return "<div>유사한 쇼핑몰 이용안내 문서를 찾지 못했습니다.</div>";
        }

        StringBuilder html = new StringBuilder();
        for (Document document : documents) {
            html.append("<div class='border-bottom mb-3 pb-2'>")
                    .append("<div><strong>유사도 점수:</strong> ")
                    .append("%.4f".formatted(document.getScore()))
                    .append("</div>")
                    .append("<div><strong>문서 내용:</strong> ")
                    .append(document.getText())
                    .append("</div>")
                    .append("<div><strong>분류:</strong> ")
                    .append(document.getMetadata().get("category"))
                    .append(" / <strong>연도:</strong> ")
                    .append(document.getMetadata().get("year"))
                    .append("</div>")
                    .append("</div>");
        }

        return html.toString();
    }
}
