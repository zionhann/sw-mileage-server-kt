package edu.handong.cseemileage.mileage.record.service

import edu.handong.cseemileage.mileage.record.dto.RecordDto
import edu.handong.cseemileage.mileage.record.exception.RecordNotFoundException
import edu.handong.cseemileage.mileage.record.repository.RecordRepository
import org.springframework.stereotype.Service
import kotlin.streams.toList

@Service
class RecordQueryService(
    val recordRepository: RecordRepository
) {
    fun getRecordById(id: Int): RecordDto.Info {
        val found = recordRepository
            .findById(id)
            .orElseThrow(::RecordNotFoundException)

        return RecordDto.Info(
            semester = found.semester.name,
            category = found.semester.category.name,
            subcategory = found.semester.item.name,
            studentId = found.student.sid,
            studentName = found.student.name,
            points = found.semester.weight,
            counts = found.counts,
            regDate = found.regDate.toString()
        )
    }

    fun getAll(): List<RecordDto.Info> {
        return recordRepository
            .findAll()
            .stream()
            .map { RecordDto.Info(it) }
            .toList()
    }
}
