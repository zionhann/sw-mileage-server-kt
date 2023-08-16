package edu.handong.cseemileage.exception

class ExceptionMessage {
    companion object Category {
        const val CATEGORY_TITLE_IS_EMPTY = "제목이 없습니다."
        const val CATEGORY_POINTS_IS_EMPTY = "마일리지 값이 없습니다."
        const val CATEGORY_INVALID_POINTS = "마일리지 값이 음수입니다. 0 이상의 값을 넣어주세요."
    }
}
