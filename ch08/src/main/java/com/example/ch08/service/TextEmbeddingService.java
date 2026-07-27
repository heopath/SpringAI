package com.example.ch08.service;

import java.util.List;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class TextEmbeddingService {

    private final EmbeddingModel embeddingModel;

    // 임베딩 처리
    public float[] embedding(String question) {
        EmbeddingResponse response =
                embeddingModel.embedForResponse(List.of(question));

        float[] vector = response
                .getResults()
                .get(0)
                .getOutput();

        log.info("임베딩 차원: {}", vector.length);

        return vector;
    }

    // 임베딩 모델 확인
    public String getEmbeddingModelName() {
        EmbeddingResponse response =
                embeddingModel.embedForResponse(List.of("test"));

        return response.getMetadata().getModel();
    }
}