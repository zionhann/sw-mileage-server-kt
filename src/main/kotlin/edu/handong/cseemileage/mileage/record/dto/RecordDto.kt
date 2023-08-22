package edu.handong.cseemileage.mileage.record.dto

import edu.handong.cseemileage.mileage.record.domain.Record

class RecordDto(
    val records: List<Info>
) {

    class Info(
        val semester: String?,
        val category: String?,
        val subcategory: String?,
        val studentId: String?,
        val studentName: String?,
        val points: Float?,
        val counts: Int?,
        val regDate: String?
    ) {
        constructor(record: Record) : this(
            semester = record.semester.name,
            category = record.semester.category.name,
            subcategory = record.semester.item.name,
            studentId = record.student.sid,
            studentName = record.student.name,
            points = record.semester.weight,
            counts = record.counts,
            regDate = record.regDate.toString()
        )
    }
}
