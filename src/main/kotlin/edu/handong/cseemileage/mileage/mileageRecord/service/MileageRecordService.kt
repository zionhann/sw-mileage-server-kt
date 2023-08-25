package edu.handong.cseemileage.mileage.mileageRecord.service

import edu.handong.cseemileage.mileage.mileageRecord.domain.MileageRecord
import edu.handong.cseemileage.mileage.mileageRecord.dto.MileageRecordForm
import edu.handong.cseemileage.mileage.mileageRecord.repository.MileageRecordRepository
import edu.handong.cseemileage.mileage.semesterItem.exception.SemesterItemNotFoundException
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepository
import edu.handong.cseemileage.student.exception.StudentNotFoundException
import edu.handong.cseemileage.student.repository.StudentRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class MileageRecordService(
    val semesterItemRepository: SemesterItemRepository,
    val studentRepository: StudentRepository,
    val mileageRecordRepository: MileageRecordRepository
) {
    fun add(form: MileageRecordForm): Int {
        val semesterItem = semesterItemRepository
            .findById(form.semesterItemId)
            .orElseThrow(::SemesterItemNotFoundException)
        val student = studentRepository
            .findBySid(form.studentId)
            .orElseThrow(::StudentNotFoundException)
        val entity = MileageRecord(
            semesterItem = semesterItem,
            student = student,
            description1 = form.description1,
            description2 = form.description2,
            counts = form.counts
        )
        val saved = mileageRecordRepository.save(entity)

        return saved.id!!
    }
}
