package edu.handong.cseemileage.mileage.record.service

import edu.handong.cseemileage.mileage.record.domain.Record
import edu.handong.cseemileage.mileage.record.dto.RecordForm
import edu.handong.cseemileage.mileage.record.repository.RecordRepository
import edu.handong.cseemileage.mileage.semester.exception.SemesterNotFoundException
import edu.handong.cseemileage.mileage.semester.repository.SemesterRepository
import edu.handong.cseemileage.student.exception.StudentNotFoundException
import edu.handong.cseemileage.student.repository.StudentRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class RecordService(
    val semesterRepository: SemesterRepository,
    val studentRepository: StudentRepository,
    val recordRepository: RecordRepository
) {
    fun add(form: RecordForm): Int {
        val semester = semesterRepository
            .findById(form.semesterId)
            .orElseThrow(::SemesterNotFoundException)
        val student = studentRepository
            .findBySid(form.studentId)
            .orElseThrow(::StudentNotFoundException)
        val entity = Record(
            semester = semester,
            student = student,
            description1 = form.description1,
            description2 = form.description2
        )
        val saved = recordRepository.save(entity)

        return saved.id!!
    }
}
