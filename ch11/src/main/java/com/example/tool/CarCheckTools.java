package com.example.tool;

import java.util.Set;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CarCheckTools {

    private final Set<String> carNumbers = Set.of(
            "23가4567",
            "234부8372",
            "345가6789"
    );

    @Tool(description = """
            이미지에서 인식한 차량 번호가
            등록된 차량 번호인지 확인합니다.
            """)
    public String checkCarNumber(
            @ToolParam(
                    description = "공백을 제외한 차량 번호",
                    required = true
            )
            String carNumber) {

        String normalizedNumber =
                carNumber.replaceAll("\\s+", "");

        log.info("LLM이 인식한 차량 번호: {}",
                normalizedNumber);

        // 숫자 2~3개 + 한글 한 글자 + 숫자 4개
        if (!normalizedNumber.matches(
                "^\\d{2,3}[가-힣]\\d{4}$")) {

            log.warn("올바르지 않은 차량 번호 형식: {}",
                    normalizedNumber);

            return "미등록번호";
        }

        return carNumbers.contains(normalizedNumber)
                ? "등록번호"
                : "미등록번호";
    }
}