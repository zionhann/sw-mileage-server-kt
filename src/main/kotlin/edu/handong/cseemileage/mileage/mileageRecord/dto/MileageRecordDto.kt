package edu.handong.cseemileage.mileage.mileageRecord.dto

import edu.handong.cseemileage.mileage.mileageRecord.domain.MileageRecord

class MileageRecordDto(
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
        val regDate: String?,
        val modDate: String?
    ) {
        constructor(mileageRecord: MileageRecord) : this(
            semester = mileageRecord.semesterItem.semesterName,
            category = mileageRecord.semesterItem.category.name,
            subcategory = mileageRecord.semesterItem.item.name,
            studentId = mileageRecord.student.sid,
            studentName = mileageRecord.student.name,
            points = mileageRecord.semesterItem.pointValue,
            counts = mileageRecord.counts,
            regDate = mileageRecord.regDate.toString(),
            modDate = mileageRecord.modDate.toString()
        )
    }
}
