package edu.handong.cseemileage.service.record

import edu.handong.cseemileage.domain.mileage.MileageRecord
import edu.handong.cseemileage.domain.mileage.SemesterItem
import edu.handong.cseemileage.dto.mileage.record.MileageRecordForm
import edu.handong.cseemileage.exception.mileage.record.InvalidDuplicateMileageRecordException
import edu.handong.cseemileage.exception.mileage.record.InvalidMileageRecordException
import edu.handong.cseemileage.exception.mileage.record.MileageRecordNotFoundException
import edu.handong.cseemileage.exception.mileage.semesterItem.SemesterItemNotFoundException
import edu.handong.cseemileage.repository.mileage.MileageRecordRepository
import edu.handong.cseemileage.repository.mileage.SemesterItemRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class MileageRecordService(
    val semesterItemRepository: SemesterItemRepository,
    val mileageRecordRepository: MileageRecordRepository
) {
    fun add(form: MileageRecordForm): Int {
        val semesterItem = findSemesterItem(form)
        validateMileageRecord(form, semesterItem)
        val record = MileageRecord.createMileageRecord(form, semesterItem, form.studentName!!, form.sid!!)
        val saved = mileageRecordRepository.save(record)

        return saved.id
    }

    private fun validateMileageRecord(form: MileageRecordForm, semesterItem: SemesterItem) {
        if (semesterItem.isMulti == "N") {
            if (form.counts!! > 1) {
                throw InvalidMileageRecordException()
            } else if (mileageRecordRepository.findBySidAndSemesterItem(form.sid!!, semesterItem) != null) throw InvalidDuplicateMileageRecordException()
        }
    }

    fun modifyMileageRecord(mileageRecordId: Int, form: MileageRecordForm): Int {
        val semesterItem = findSemesterItem(form)
        return mileageRecordRepository
            .findById(mileageRecordId)
            .orElseThrow { MileageRecordNotFoundException() }
            .update(form, semesterItem, form.studentName!!, form.sid!!)
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
        try {
            mileageRecordRepository.deleteById(mileageRecordId)
        } catch (e: Exception) {
            throw MileageRecordNotFoundException()
        }
        return mileageRecordId
    }
}
