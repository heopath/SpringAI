package com.example.ch08.service;

import java.util.Set;

import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class DeleteDocumentService {

    private static final Set<String> ALLOWED_CATEGORIES = Set.of(
            "회원가입", "배송", "교환", "환불", "결제", "주문", "쿠폰");

    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public DeleteResult deleteByCategory(String category) {
        if (!ALLOWED_CATEGORIES.contains(category)) {
            throw new IllegalArgumentException(
                    "삭제 가능한 분류: " + String.join(", ", ALLOWED_CATEGORIES));
        }

        int before = countByCategory(category);
        vectorStore.delete("source == '쇼핑몰 이용안내' && category == '" + category + "'");
        int after = countByCategory(category);

        log.info("분류 삭제: category={}, before={}, after={}", category, before, after);
        return new DeleteResult(category, before, after);
    }

    private int countByCategory(String category) {
        Integer count = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM shopping_vector_store
                WHERE metadata ->> 'source' = '쇼핑몰 이용안내'
                  AND metadata ->> 'category' = ?
                """,
                Integer.class,
                category);

        return count == null ? 0 : count;
    }

    public record DeleteResult(String category, int before, int after) {

        public int deletedCount() {
            return before - after;
        }
    }
}
