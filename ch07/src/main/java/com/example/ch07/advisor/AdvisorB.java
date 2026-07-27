package com.example.ch07.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.core.Ordered;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

@Log4j2
public class AdvisorB implements CallAdvisor, StreamAdvisor {

    @Override
    public String getName() {
        return AdvisorB.class.getSimpleName();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 2;
    }

    @Override
    public ChatClientResponse adviseCall(
            ChatClientRequest request,
            CallAdvisorChain chain) {

        log.info("[AdvisorB 전처리]");

        ChatClientResponse response = chain.nextCall(request);

        log.info("[AdvisorB 후처리]");

        return response;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(
            ChatClientRequest request,
            StreamAdvisorChain chain) {

        log.info("[AdvisorB 스트림 전처리]");

        return chain.nextStream(request)
                .doFinally(signalType ->
                        log.info("[AdvisorB 스트림 후처리] {}", signalType));
    }
}