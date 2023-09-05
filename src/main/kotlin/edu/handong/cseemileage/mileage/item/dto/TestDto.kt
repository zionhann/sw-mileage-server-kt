package edu.handong.cseemileage.mileage.item.dto

import com.fasterxml.jackson.annotation.JsonInclude

class TestDto(
    // 리스트 반환에 사용
    val list: List<Info>? = null,
    val count: Int? = null,

    // 필수로 항상 반환해주기
    val description: String,

    // 단독 조회에 사용
    val data: Info? = null

) {

    /**
     * 반환에 사용될 수 있는 모든 필드를 하나의 inner class에 정리.
     * @JsonInclude(JsonInclude.Include.NON_NULL)을 통해 null인 필드는 반환하지 않음.
     * 널 허용(?), 기본 값 Null
     * */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class Info(
        val id: Int? = null,
        val counts: Float? = null,
        val points: Int? = null,
        val extraPoints: Int? = null,
        val description1: String? = null,
        val description2: String? = null
    )
}
