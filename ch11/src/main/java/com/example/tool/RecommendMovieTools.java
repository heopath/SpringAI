package com.example.tool;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class RecommendMovieTools {

    @Tool(description = """
            사용자 ID를 이용하여 해당 사용자가 이전에 관람한 영화 목록을 조회합니다.
            사용자의 영화 취향을 분석할 때 사용합니다.
            """)
    public List<String> getMovieListByUserId(
            @ToolParam(
                    description = "관람 영화 목록을 조회할 사용자 ID",
                    required = true
            )
            String userId) {

        log.info("getMovieListByUserId: {}", userId);

        // 데이터베이스 조회 결과라고 가정
        return List.of(
                "엣지 오브 투모로우",
                "투모로우",
                "아이언맨",
                "혹성탈출",
                "타이타닉",
                "인터스텔라",
                "아바타",
                "마션"
        );
    }

    @Tool(
            description = "입력받은 장르에 해당하는 추천 영화 목록을 조회합니다.",
            returnDirect = true
    )
    public List<String> recommendMovie(
            @ToolParam(
                    description = "추천받고 싶은 영화 장르",
                    required = true
            )
            String genre) {

        log.info("recommendMovie: {}", genre);

        // 데이터베이스 조회 결과라고 가정
        return List.of("크레이븐", "베놈", "메이드");
    }
}