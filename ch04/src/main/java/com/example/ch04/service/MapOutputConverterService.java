package com.example.ch04.service;

import java.util.Map;

import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class MapOutputConverterService {

    private final NvidiaApiService nvidiaApiService;

    public Map<String, Object> convert(String city) {

        MapOutputConverter converter =
                new MapOutputConverter();

        String prompt = """
                %s 도시에 대한 정보를 출력하세요.

                다음 정보를 포함하세요.

                city
                country
                population
                famousFood

                %s
                """.formatted(
                        city,
                        converter.getFormat()
                );

        String answer =
                nvidiaApiService.call(prompt);

        log.info(
                "Map 원본 응답: {}",
                answer
        );

        Map<String, Object> result =
                converter.convert(answer);

        log.info(
                "Map 변환 결과: {}",
                result
        );

        return result;
    }
}