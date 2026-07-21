package com.example.ch04.service;

import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import com.example.ch04.dto.CityInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class BeanOutputConverterService {

    private final NvidiaApiService nvidiaApiService;

    public CityInfo convert(String city) {

        BeanOutputConverter<CityInfo> converter =
                new BeanOutputConverter<>(CityInfo.class);

        String prompt = """
                %s 도시에 대한 정보를 알려주세요.

                %s
                """.formatted(
                        city,
                        converter.getFormat()
                );

        String answer =
                nvidiaApiService.call(prompt);

        log.info("Bean 원본 응답: {}", answer);

        CityInfo cityInfo =
                converter.convert(answer);

        log.info("Bean 변환 결과: {}", cityInfo);

        return cityInfo;
    }
}