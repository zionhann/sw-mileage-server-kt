package edu.handong.cseemileage.excel.strategy.header

enum class SemesterItemUpload(val ko: String) {
    INDEX("No"),
    CATEGORY_NAME("항목명"),
    SEMESTER("연도 및 학기 (YYYY-{1|2})"),
    ITEM_NAME("세부항목명"),
    SCORE("가중치"),
    MAX_SCORE("최대 마일리지"),
    IS_MULTI("중복 레코드 허용 (Y|N)")
}
