package com.example.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.CompressionQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CompressionQueryTransformerService {

    private final ChatClient chatClient;
    private final ChatModel chatModel;
    private final ChatMemory chatMemory;
    private final VectorStore vectorStore;

    public CompressionQueryTransformerService(
            ChatClient.Builder chatClientBuilder,
            ChatModel chatModel,
            ChatMemory chatMemory,
            VectorStore vectorStore) {

        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        SimpleLoggerAdvisor.builder()
                                .order(Ordered.LOWEST_PRECEDENCE - 1)
                                .build()
                )
                .build();

        this.chatModel = chatModel;
        this.chatMemory = chatMemory;
        this.vectorStore = vectorStore;
    }

    public CompressionQueryTransformer createCompressionQueryTransformer() {

        ChatClient.Builder transformerClientBuilder =
                ChatClient.builder(chatModel)
                        .defaultAdvisors(
                                SimpleLoggerAdvisor.builder()
                                        .order(Ordered.LOWEST_PRECEDENCE - 1)
                                        .build()
                        );

        PromptTemplate compressionPrompt = new PromptTemplate("""
                대화 기록과 후속 질문을 참고하여,
                문맥 없이도 이해할 수 있는 독립적인 검색 질문을 만드세요.

                규칙:
                1. 검색 질문 한 문장만 출력하세요.
                2. 반드시 후속 질문과 같은 언어를 사용하세요.
                3. 한국어 질문을 영어로 번역하지 마세요.
                4. 후속 질문의 단어와 전문 용어를 그대로 유지하세요.
                5. 의미를 설명하거나 표현을 길게 풀어 쓰지 마세요.
                6. 생략된 주어와 대상만 대화 기록에서 찾아 추가하세요.
                7. 후속 질문이 이미 독립적인 질문이면 그대로 반환하세요.
                8. 같은 의미의 단어로 불필요하게 바꾸지 마세요.

                변환 예시:

                대화 기록:
                USER: 대한민국 대통령의 임기는 몇 년입니까?
                ASSISTANT: 5년입니다.

                후속 질문:
                중임할 수 있습니까?

                출력:
                대한민국 대통령은 중임할 수 있습니까?

                대화 기록:
                {history}

                후속 질문:
                {query}

                출력:
                """);

        return CompressionQueryTransformer.builder()
                .chatClientBuilder(transformerClientBuilder)
                .promptTemplate(compressionPrompt)
                .build();
    }
    
    public VectorStoreDocumentRetriever
            createVectorStoreDocumentRetriever(
                    double score,
                    String source) {

        var builder = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .similarityThreshold(score)
                .topK(3);

        if (StringUtils.hasText(source)) {
            FilterExpressionBuilder filterBuilder =
                    new FilterExpressionBuilder();

            builder.filterExpression(
                    filterBuilder.eq("source", source).build()
            );
        }

        return builder.build();
    }

    public String chatWithCompression(
            String question,
            double score,
            String source,
            String conversationId) {

        RetrievalAugmentationAdvisor retrievalAdvisor =
                RetrievalAugmentationAdvisor.builder()
                        .queryTransformers(
                                createCompressionQueryTransformer()
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
                .advisors(advisorSpec ->
                        advisorSpec.param(
                                ChatMemory.CONVERSATION_ID,
                                conversationId
                        )
                )
                .call()
                .content();
    }
}