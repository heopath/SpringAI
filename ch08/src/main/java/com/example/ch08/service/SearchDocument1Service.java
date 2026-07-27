package com.example.ch08.service;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class SearchDocument1Service {

    private final VectorStore vectorStore;

    public List<Document> searchDocument(String question) {
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(3)
                        .similarityThreshold(0.35)
                        .filterExpression("source == '쇼핑몰 이용안내'")
                        .build());

        log.info("질문: {}, 검색 결과 수: {}", question, documents.size());
        return documents;
    }
}
