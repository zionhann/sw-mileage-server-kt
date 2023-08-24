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
        val semester = semesterItemRepository
            .findById(form.semesterId)
            .orElseThrow(::SemesterItemNotFoundException)
        val student = studentRepository
            .findBySid(form.studentId)
            .orElseThrow(::StudentNotFoundException)
        val entity = MileageRecord(
            semesterItem = semester,
            student = student,
            description1 = form.description1,
            description2 = form.description2
        )
        val saved = mileageRecordRepository.save(entity)

        return saved.id!!
    }
}
