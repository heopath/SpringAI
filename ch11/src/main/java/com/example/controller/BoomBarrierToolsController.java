package com.example.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.service.BoomBarrierToolsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Controller
public class BoomBarrierToolsController {

    private final BoomBarrierToolsService service;

    @GetMapping("/ai/boom-barrier-tools")
    public String boomBarrierTools() {
        return "/boom-barrier-tools";
    }

    @ResponseBody
    @PostMapping("/ai/boom-barrier-tools")
    public String boomBarrierTools(
            @RequestParam("attach") MultipartFile attach) {

        log.info("차량 이미지 요청 도착");
        log.info("파일명: {}", attach.getOriginalFilename());
        log.info("파일 형식: {}", attach.getContentType());
        log.info("파일 크기: {}", attach.getSize());

        try {
            String answer = service.chat(
                    attach.getContentType(),
                    attach.getBytes()
            );

            log.info("최종 답변: {}", answer);
            return answer;

        } catch (Exception e) {
            log.error("차량 이미지 처리 실패", e);
            return "이미지 처리 실패: " + e.getMessage();
        }
    }
}