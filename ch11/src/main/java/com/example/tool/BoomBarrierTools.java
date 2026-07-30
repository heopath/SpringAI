package com.example.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class BoomBarrierTools {

    @Tool(description = """
            등록된 차량의 입차를 허용하기 위해
            차단기를 올립니다.
            """)
    public String boomBarrierUp() {
        log.info("차단기를 올립니다.");
        return "차단기 올림";
    }

    @Tool(description = """
            미등록 차량의 입차를 차단하기 위해
            차단기를 내립니다.
            """)
    public String boomBarrierDown() {
        log.info("차단기를 내립니다.");
        return "차단기 내림";
    }
}