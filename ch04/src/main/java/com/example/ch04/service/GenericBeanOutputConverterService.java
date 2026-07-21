package com.example.ch04.service;

import java.util.List;

import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.example.ch04.dto.Hotel;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class GenericBeanOutputConverterService {

    private final NvidiaApiService nvidiaApiService;

    public List<Hotel> convert(String city) {

        BeanOutputConverter<List<Hotel>> converter =
                new BeanOutputConverter<>(
                        new ParameterizedTypeReference<List<Hotel>>() {
                        }
                );

        String prompt = """
                %s에서 유명한 호텔 5개를 알려주세요.

                %s
                """.formatted(
                        city,
                        converter.getFormat()
                );

        String answer =
                nvidiaApiService.call(prompt);

        log.info(
                "Generic Bean 원본 응답: {}",
                answer
        );

        List<Hotel> hotels =
                converter.convert(answer);

        log.info(
                "Generic Bean 변환 결과: {}",
                hotels
        );

        return hotels;
    }
}