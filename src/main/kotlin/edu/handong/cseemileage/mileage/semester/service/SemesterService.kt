package edu.handong.cseemileage.mileage.semester.service

import edu.handong.cseemileage.mileage.item.exception.ItemNotFoundException
import edu.handong.cseemileage.mileage.item.repository.ItemRepository
import edu.handong.cseemileage.mileage.semester.domain.Semester
import edu.handong.cseemileage.mileage.semester.dto.SemesterForm
import edu.handong.cseemileage.mileage.semester.dto.SemesterMultipleForm
import edu.handong.cseemileage.mileage.semester.repository.SemesterJdbcRepository
import edu.handong.cseemileage.mileage.semester.repository.SemesterRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SemesterService(
    val repository: SemesterRepository,
    val itemRepository: ItemRepository,
    val jdbcRepository: SemesterJdbcRepository,
    val modelMapper: ModelMapper
) {

    fun createOneSemester(form: SemesterForm): Semester {
        val item = itemRepository.findById(form.itemId)
            .orElseThrow { ItemNotFoundException() }
        println(item.category.name)
        return Semester.createSemester(form, item, item.category)
    }

    fun saveSemester(form: SemesterForm): Int {
        val saved = repository.save(createOneSemester(form))
        return saved.id!!
    }

    fun createSemesterMultiple(form: SemesterMultipleForm): MutableList<Semester> {
        val semesterList = mutableListOf<Semester>()
        form.semesterList.forEach {
            semesterList.add(createOneSemester(it))
        }
        return semesterList
    }

    fun saveSemesterMultiple(form: SemesterMultipleForm) {
        repository.saveAll(createSemesterMultiple(form))
    }

    fun saveSemesterMultipleBulkInsert(form: SemesterMultipleForm) {
        jdbcRepository.insertSemesterList(createSemesterMultiple(form))
    }
}
