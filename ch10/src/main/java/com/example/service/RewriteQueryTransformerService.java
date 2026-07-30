package com.example.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class RewriteQueryTransformerService {

    private final ChatClient chatClient;
    private final ChatModel chatModel;
    private final ChatMemory chatMemory;
    private final VectorStore vectorStore;

    public RewriteQueryTransformerService(
            ChatClient.Builder chatClientBuilder,
            ChatModel chatModel,
            ChatMemory chatMemory,
            VectorStore vectorStore) {

        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        SimpleLoggerAdvisor.builder()
                                .order(
                                        Ordered.LOWEST_PRECEDENCE - 1
                                )
                                .build()
                )
                .build();

        this.chatModel = chatModel;
        this.chatMemory = chatMemory;
        this.vectorStore = vectorStore;
    }

    public RewriteQueryTransformer createRewriteQueryTransformer() {

        ChatClient.Builder rewriteClientBuilder =
                ChatClient.builder(chatModel)
                        .defaultAdvisors(
                                SimpleLoggerAdvisor.builder()
                                        .order(
                                                Ordered.LOWEST_PRECEDENCE - 1
                                        )
                                        .build()
                        );

        PromptTemplate rewritePrompt = new PromptTemplate("""
                다음 원본 질문에서 감정적 표현과 반복 내용을 제거하고,
                검색에 적합한 객관적인 질문 한 문장으로 바꾸세요.

                원본 질문:
                {query}

                검색 대상:
                {target}

                규칙:
                - 원본 질문의 핵심 주제를 유지하세요.
                - 검색 대상 자체를 설명하는 질문으로 바꾸지 마세요.
                - 반드시 한국어 질문 한 문장만 출력하세요.
                - 설명과 마크다운은 출력하지 마세요.

                출력 예시:
                대한민국 헌법에서 국회와 국회의원의 역할과 의무는 무엇입니까?

                재작성 결과:
                """);

        return RewriteQueryTransformer.builder()
                .chatClientBuilder(rewriteClientBuilder)
                .promptTemplate(rewritePrompt)
                .targetSearchSystem("대한민국 헌법 조문")
                .build();
    }

    public VectorStoreDocumentRetriever
            createVectorStoreDocumentRetriever(
                    double score,
                    String source) {

        var builder = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .similarityThreshold(score)
                .topK(5);

        if (StringUtils.hasText(source)) {
            FilterExpressionBuilder filterBuilder =
                    new FilterExpressionBuilder();

            builder.filterExpression(
                    filterBuilder.eq("source", source).build()
            );
        }

        return builder.build();
    }

    public String chatWithRewriteQuery(
            String question,
            double score,
            String source,
            String conversationId) {

        RetrievalAugmentationAdvisor retrievalAdvisor =
                RetrievalAugmentationAdvisor.builder()
                        .queryTransformers(
                                createRewriteQueryTransformer()
                        )
                        .documentRetriever(
                                createVectorStoreDocumentRetriever(
                                        score,
                                        source
                                )
                        )
                        .build();

        return chatClient.prompt()
                .user(question)
                .advisors(
                        MessageChatMemoryAdvisor.builder(chatMemory)
                                .build(),
                        retrievalAdvisor
                )
                .advisors(spec -> spec.param(
                        ChatMemory.CONVERSATION_ID,
                        conversationId
                ))
                .call()
                .content();
    }
}