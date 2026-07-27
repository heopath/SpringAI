package com.example.ch08.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ch08.service.DeleteDocumentService;
import com.example.ch08.service.DeleteDocumentService.DeleteResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class DeleteDocumentController {

    private final DeleteDocumentService service;

    @GetMapping("/ai/delete-document")
    public String deleteDocumentPage() {
        return "delete-document";
    }

    @ResponseBody
    @PostMapping(
            value = "/ai/delete-document",
            produces = "text/html;charset=UTF-8")
    public String deleteDocument(@RequestParam("question") String category) {
        if (category == null || category.isBlank()) {
            return "<div>삭제할 분류를 입력하세요.</div>";
        }

        try {
            DeleteResult result = service.deleteByCategory(category.trim());

            return """
                    <div><strong>삭제 조건:</strong> category = %s</div>
                    <div><strong>삭제 전:</strong> %d건</div>
                    <div><strong>삭제 건수:</strong> %d건</div>
                    <div><strong>삭제 후:</strong> %d건</div>
                    """.formatted(
                    result.category(),
                    result.before(),
                    result.deletedCount(),
                    result.after());
        }
        catch (IllegalArgumentException e) {
            return "<div>" + e.getMessage() + "</div>";
        }
    }
}
