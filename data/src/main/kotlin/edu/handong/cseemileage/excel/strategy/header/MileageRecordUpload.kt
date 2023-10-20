package edu.handong.cseemileage.excel.strategy.header

enum class MileageRecordUpload(val ko: String) {
    INDEX("번호"),
    CATEGORY_NAME("카테고리"),
    SEMESTER("학기"),
    ITEM_NAME("항목명"),
    SID("학번"),
    STUDENT_NAME("이름"),
    COUNT("등록횟수"),
    EXTRA_POINTS("가산점"),
    DESCRIPTION1("추가설명1"),
    DESCRIPTION2("추가설명2")
}
