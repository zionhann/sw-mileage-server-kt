package edu.handong.cseemileage.exception

class ExceptionMessage {
    companion object {
        const val CATEGORY_TITLE_IS_EMPTY = "카테고리 제목이 없습니다."
        const val CATEGORY_POINTS_IS_EMPTY = "마일리지 인정 최댓값이 없습니다."
        const val CATEGORY_INVALID_POINTS = "마일리지 인정 최댓값이 음수입니다. 0 이상의 값을 넣어주세요."
        const val CATEGORY_NOT_FOUND = "카테고리를 찾을 수 없습니다."
        const val CATEGORY_DUPLICATE = "이미 존재하는 카테고리 입니다."
        const val CATEGORY_CANNOT_DELETE = "하위 항목이 존재하는 카테고리는 삭제할 수 없습니다."
        const val CATEGORY_TYPE_IS_EMPTY = "카테고리의 타입이 없습니다."
        const val CATEGORY_MAX_POINTS_IS_NEGATIVE = "마일리지 인정 최댓값이 음수입니다. 0 이상의 값을 넣어주세요."

        const val ITEM_NOT_FOUND = "항목을 찾을 수 없습니다."
        const val ITEM_DUPLICATE = "이미 존재하는 항목 입니다."
        const val ITEM_CATEGORY_ID_NOT_POSITIVE = "카테고리 ID가 양수가 아닙니다."
        const val ITEM_NAME_IS_EMPTY = "항목 이름이 없습니다."
        const val ITEM_CANNOT_DELETE = "학기별 항목이 존재하는 글로벌 항목은 삭제할 수 없습니다."
        const val ITEM_INVALID_STU_TYPE = "학생 타입이 잘못되었습니다.(char(3), C, F, CF)"
        const val ITEM_MAX_POINTS_IS_NEGATIVE = "항목 인정 최댓값이 음수입니다. 0 이상의 값을 넣어주세요."

        const val RECORD_SEMESTER_IS_EMPTY = "학기가 없습니다."
        const val RECORD_STUDENT_ID_IS_EMPTY = "학번이 없습니다."
        const val RECORD_INVALID_COUNTS = "마일리지 등록 개수가 0입니다. 1 이상의 값을 넣어주세요."
        const val RECORD_NOT_FOUND = "해당하는 마일리지 기록을 찾을 수 없습니다."
        const val RECORD_SEMESTER_IS_NOT_POSITIVE = "학기 ID가 양수가 아닙니다."
        const val RECORD_INVALID_EXTRA_POINTS = "가산점이 음수입니다. 0 이상의 값을 넣어주세요."
        const val INVALID_MILEAGE_RECORD = "중복 적립이 불가한 항목입니다. (count = 1만 허용)"
        const val INVALID_DUPLICATE_MILEAGE_RECORD = "중복 적립이 불가한 항목입니다. 해당 학생은 이미 등록되었습니다."

        const val SEMESTER_ITEM_NOT_FOUND = "해당 학기가 존재하지 않습니다."
        const val SEMESTER_ITEM_ID_IS_NOT_POSITIVE = "글로벌 항목 ID가 양수가 아닙니다."
        const val SEMESTER_ITEM_CANNOT_DELETE = "이미 마일리지가 등록된 항목은 삭제할 수 없습니다."
        const val SEMESTER_ITEM_DUPLICATE = "해당 학기에 이미 등록된 항목입니다."
        const val SEMESTER_NAME_NOT_FOUND = "수정 시 학기 이름은 필수 요청 항목입니다."
        const val SEMESTER_NAME_COPY_TO_NOT_FOUND = "복사할 학기(copy to)를 입력하세요."
        const val SEMESTER_ITEM_POINTS_IS_NEGATIVE = "학기별 항목 인정 마일리지가 음수입니다. 0 이상의 값을 넣어주세요."

        const val STUDENT_NOT_FOUND = "해당 학생이 존재하지 않습니다."
        const val STUDENT_DUPLICATE = "이미 존재하는 학생 입니다."
        const val STUDENT_SID_IS_EMPTY = "학번은 필수 입력 항목입니다."
        const val STUDENT_CANNOT_DELETE = "이미 마일리지가 등록된 학생은 삭제할 수 없습니다."
        const val STUDENT_ID_IS_NOT_POSITIVE = "학생 ID 양수가 아닙니다."
        const val STUDENT_INVALID_SID = "학번 형식이 잘못되었습니다. (8자리 숫자)"
        const val STUDENT_NAME_IS_EMPTY = "학생 이름은 필수 입력 항목입니다."

        const val ADMIN_NAME_IS_EMPTY = "관리자 이름이 없습니다."
        const val ADMIN_EMAIL_IS_EMPTY = "관리자 이메일이 없습니다."
        const val ADMIN_NOT_FOUND_EXCEPTION = "해당 관리자가 존재하지 않습니다."
        const val DUPLICATE_ADMIN_EXCEPTION = "해당 관리자가 이미 존재합니다."
        const val ADMIN_INVALID_LEVEL = "관리자 권한이 잘못되었습니다. (-127 ~ 127)"

        const val DUPLICATE_APPLY_EXCEPTION = "해당 학기에 이미 신청한 학생입니다."
        const val APPLY_NOT_FOUND_EXCEPTION = "해당 신청 정보가 존재하지 않습니다."

        const val INVALID_EMAIL = "이메일 형식이 잘못되었습니다."
        const val INVALID_SEMESTER_NAME = "학기 형식이 잘못되었습니다. (YYYY-01 / YYYY-02)"
    }
}
