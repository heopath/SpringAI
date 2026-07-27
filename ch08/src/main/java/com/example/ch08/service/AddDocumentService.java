package com.example.ch08.service;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class AddDocumentService {

    private static final String SOURCE = "쇼핑몰 이용안내";

    private final VectorStore vectorStore;

    public int addDocument() {
        // 실습을 반복해도 동일한 데이터가 중복 저장되지 않도록 기존 실습 데이터를 교체한다.
        vectorStore.delete("source == '" + SOURCE + "'");

        List<Document> documents = List.of(
                document(
                        "회원가입은 이메일 주소와 휴대전화 본인 인증을 완료한 뒤 비밀번호를 설정하면 됩니다.",
                        "회원가입",
                        2026),
                document(
                        "만 14세 미만 회원은 법정대리인의 동의를 받은 후 회원가입을 진행할 수 있습니다.",
                        "회원가입",
                        2026),
                document(
                        "상품은 결제 완료 후 보통 1~3영업일 안에 출고되며 배송 현황은 주문 내역에서 확인할 수 있습니다.",
                        "배송",
                        2026),
                document(
                        "도서산간 지역은 일반 지역보다 배송이 2~3일 더 걸릴 수 있으며 추가 배송비가 발생할 수 있습니다.",
                        "배송",
                        2026),
                document(
                        "교환은 상품을 받은 날부터 7일 이내에 주문 내역에서 신청할 수 있습니다.",
                        "교환",
                        2026),
                document(
                        "단순 변심으로 교환하는 경우 왕복 배송비는 구매자가 부담합니다.",
                        "교환",
                        2026),
                document(
                        "환불은 반품 상품 검수가 끝난 후 3영업일 이내에 처리됩니다.",
                        "환불",
                        2026),
                document(
                        "카드 결제 취소 금액은 카드사 사정에 따라 영업일 기준 3~7일 후 반영될 수 있습니다.",
                        "환불",
                        2026),
                document(
                        "결제 수단으로 신용카드, 실시간 계좌이체, 무통장입금과 간편결제를 사용할 수 있습니다.",
                        "결제",
                        2025),
                document(
                        "무통장입금 주문은 주문 후 24시간 안에 입금하지 않으면 자동으로 취소됩니다.",
                        "결제",
                        2025),
                document(
                        "주문이 완료되면 마이페이지의 주문 내역에서 상품, 수량, 결제 금액을 확인할 수 있습니다.",
                        "주문",
                        2026),
                document(
                        "쿠폰은 결제 화면에서 적용할 수 있으며 일부 할인 상품에는 쿠폰 사용이 제한될 수 있습니다.",
                        "쿠폰",
                        2026));

        vectorStore.add(documents);
        log.info("쇼핑몰 이용안내 문서 {}건 저장 완료", documents.size());

        return documents.size();
    }

    private Document document(String content, String category, int year) {
        return Document.builder()
                .text(content)
                .metadata("source", SOURCE)
                .metadata("category", category)
                .metadata("year", year)
                .build();
    }
}
