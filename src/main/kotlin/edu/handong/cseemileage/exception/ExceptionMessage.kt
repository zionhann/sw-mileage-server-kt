package edu.handong.cseemileage.exception

class ExceptionMessage {
    companion object {
        const val CATEGORY_TITLE_IS_EMPTY = "카테고리 제목이 없습니다."
        const val CATEGORY_POINTS_IS_EMPTY = "마일리지 인정 최댓값이 없습니다."
        const val CATEGORY_INVALID_POINTS = "마일리지 인정 최댓값이 음수입니다. 0 이상의 값을 넣어주세요."
        const val CATEGORY_NOT_FOUND = "카테고리를 찾을 수 없습니다."

        const val ITEM_NOT_FOUND = "항목을 찾을 수 없습니다."

        const val RECORD_SEMESTER_IS_EMPTY = "학기가 없습니다."
        const val RECORD_STUDENT_ID_IS_EMPTY = "학번이 없습니다."
        const val RECORD_INVALID_COUNTS = "마일리지 등록 개수가 음수입니다. 0 이상의 값을 넣어주세요."
        const val RECORD_NOT_FOUND = "해당하는 마일리지 기록을 찾을 수 없습니다."

        const val SEMESTER_NOT_FOUND = "해당 학기가 존재하지 않습니다."

        const val STUDENT_NOT_FOUND = "해당 학생이 존재하지 않습니다."
    }
}
