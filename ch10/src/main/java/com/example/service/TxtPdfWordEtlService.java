package com.example.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class TxtPdfWordEtlService {

    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of("txt", "pdf", "doc", "docx");

    private final VectorStore vectorStore;

    public TxtPdfWordEtlService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public String etlFromFile(
            String title,
            String author,
            MultipartFile attach) throws IOException {

        validateFile(attach);

        // Extract: 문서에서 텍스트 추출
        List<Document> extractedDocuments =
                extractFromFile(attach);

        // Transform: 메타데이터 추가
        List<Document> metadataDocuments =
                addMetadata(
                        extractedDocuments,
                        title,
                        author,
                        attach);

        // Transform: 임베딩에 적합한 크기로 분할
        TokenTextSplitter splitter =
                TokenTextSplitter.builder()
                        .withChunkSize(700)
                        .withMinChunkSizeChars(200)
                        .withMinChunkLengthToEmbed(10)
                        .withMaxNumChunks(5000)
                        .withKeepSeparator(true)
                        .build();

        List<Document> splitDocuments =
                splitter.apply(metadataDocuments);

        if (splitDocuments.isEmpty()) {
            throw new IllegalArgumentException(
                    "문서에서 저장할 텍스트를 찾지 못했습니다.");
        }

        // Load: 임베딩 생성 후 VectorStore에 저장
        vectorStore.add(splitDocuments);

        log.info(
                "ETL 완료: file={}, extracted={}, chunks={}",
                attach.getOriginalFilename(),
                extractedDocuments.size(),
                splitDocuments.size());

        return String.format(
                "'%s' 문서의 추출·변환·적재를 완료했습니다. 총 %d개 청크가 저장되었습니다.",
                title,
                splitDocuments.size());
    }

    private List<Document> extractFromFile(
            MultipartFile attach) {

        TikaDocumentReader reader =
                new TikaDocumentReader(attach.getResource());

        return reader.get();
    }

    private List<Document> addMetadata(
            List<Document> documents,
            String title,
            String author,
            MultipartFile attach) {

        String filename = attach.getOriginalFilename();
        String contentType = attach.getContentType();

        return documents.stream()
                .map(document -> {
                    Map<String, Object> metadata =
                            new HashMap<>(document.getMetadata());

                    metadata.put("title", title);
                    metadata.put("author", author);
                    metadata.put("filename", filename);
                    metadata.put(
                            "contentType",
                            contentType == null
                                    ? "unknown"
                                    : contentType);

                    return Document.builder()
                            .text(document.getText())
                            .metadata(metadata)
                            .build();
                })
                .toList();
    }

    private void validateFile(MultipartFile attach) {
        if (attach == null || attach.isEmpty()) {
            throw new IllegalArgumentException(
                    "업로드된 문서가 없습니다.");
        }

        String filename = attach.getOriginalFilename();

        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException(
                    "파일 확장자를 확인할 수 없습니다.");
        }

        String extension = filename
                .substring(filename.lastIndexOf('.') + 1)
                .toLowerCase(Locale.ROOT);

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException(
                    "TXT, PDF, DOC, DOCX 파일만 업로드할 수 있습니다.");
        }
    }
}