package edu.handong.cseemileage.mileage.mileageRecord.service

import edu.handong.cseemileage.mileage.mileageRecord.domain.MileageRecord
import edu.handong.cseemileage.mileage.mileageRecord.dto.MileageRecordForm
import edu.handong.cseemileage.mileage.mileageRecord.exception.MileageRecordNotFoundException
import edu.handong.cseemileage.mileage.mileageRecord.repository.MileageRecordRepository
import edu.handong.cseemileage.mileage.semesterItem.domain.SemesterItem
import edu.handong.cseemileage.mileage.semesterItem.exception.SemesterItemNotFoundException
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepository
import edu.handong.cseemileage.student.domain.Student
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
        val semesterItem = findSemesterItem(form)
        val student = findStudent(form)
        val record = MileageRecord.createMileageRecord(form, semesterItem!!, student!!)
        val saved = mileageRecordRepository.save(record)

        return saved.id!!
    }

    fun modifyMileageRecord(mileageRecordId: Int, form: MileageRecordForm): Int {
        val semesterItem = findSemesterItem(form)
        val student = findStudent(form)
        return mileageRecordRepository
            .findById(mileageRecordId)
            .orElseThrow { MileageRecordNotFoundException() }
            .update(form, semesterItem!!, student!!)
    }

    fun findStudent(form: MileageRecordForm): Student {
        val student = form.studentId?.let {
            studentRepository
                .findById(it)
                .orElseThrow(::StudentNotFoundException)
        }
        return student!!
    }

    fun findSemesterItem(form: MileageRecordForm): SemesterItem {
        val semesterItem = form.semesterItemId?.let {
            semesterItemRepository
                .findById(it)
                .orElseThrow(::SemesterItemNotFoundException)
        }
        return semesterItem!!
    }

    fun deleteMileageRecord(mileageRecordId: Int): Int {
        mileageRecordRepository.deleteById(mileageRecordId)
        return mileageRecordId
    }
}