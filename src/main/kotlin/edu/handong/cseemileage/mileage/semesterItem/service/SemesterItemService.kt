package edu.handong.cseemileage.mileage.semesterItem.service

import edu.handong.cseemileage.mileage.item.exception.ItemNotFoundException
import edu.handong.cseemileage.mileage.item.repository.ItemRepository
import edu.handong.cseemileage.mileage.semesterItem.domain.SemesterItem
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemForm
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemMultipleForm
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemJdbcRepository
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SemesterItemService(
    val repository: SemesterItemRepository,
    val itemRepository: ItemRepository,
    val jdbcRepository: SemesterItemJdbcRepository,
    val modelMapper: ModelMapper
) {

    fun createOneSemesterItem(form: SemesterItemForm): SemesterItem {
        val item = itemRepository.findById(form.itemId)
            .orElseThrow { ItemNotFoundException() }
        println(item.category.name)
        return SemesterItem.createSemesterItem(form, item, item.category)
    }

    fun saveSemesterItem(form: SemesterItemForm): Int {
        val saved = repository.save(createOneSemesterItem(form))
        return saved.id!!
    }

    fun createSemesterItemMultiple(form: SemesterItemMultipleForm): MutableList<SemesterItem> {
        val semesterItemList = mutableListOf<SemesterItem>()
        form.semesterItemList.forEach {
            semesterItemList.add(createOneSemesterItem(it))
        }
        return semesterItemList
    }

    fun saveSemesterItemMultiple(form: SemesterItemMultipleForm) {
        repository.saveAll(createSemesterItemMultiple(form))
    }

    fun saveSemesterItemMultipleBulkInsert(form: SemesterItemMultipleForm) {
        jdbcRepository.insertSemesterList(createSemesterItemMultiple(form))
    }
}
