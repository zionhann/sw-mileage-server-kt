package edu.handong.cseemileage.service.semesterItem

import edu.handong.cseemileage.domain.mileage.SemesterItem
import edu.handong.cseemileage.dto.mileage.semesterItem.SemesterItemForm
import edu.handong.cseemileage.dto.mileage.semesterItem.SemesterItemMultipleForm
import edu.handong.cseemileage.exception.mileage.item.ItemNotFoundException
import edu.handong.cseemileage.exception.mileage.semesterItem.DuplicateSemesterItemException
import edu.handong.cseemileage.exception.mileage.semesterItem.SemesterItemNotFoundException
import edu.handong.cseemileage.exception.mileage.semesterItem.SemesterNameNotFoundException
import edu.handong.cseemileage.repository.mileage.ItemRepository
import edu.handong.cseemileage.repository.mileage.SemesterItemJdbcRepository
import edu.handong.cseemileage.repository.mileage.SemesterItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SemesterItemService(
    val repository: SemesterItemRepository,
    val itemRepository: ItemRepository,
    val jdbcRepository: SemesterItemJdbcRepository
) {

    fun createOneSemesterItem(form: SemesterItemForm, semesterName: String): SemesterItem {
        validateDuplicateSemesterItem(semesterName!!, form.itemId!!, 0)
        val item = itemRepository
            .findById(form.itemId)
            .orElseThrow { ItemNotFoundException() }
        return SemesterItem.createSemesterItem(form, item, item.category, semesterName)
    }

    fun saveSemesterItem(form: SemesterItemForm, semesterName: String): Int {
        val saved = repository.save(createOneSemesterItem(form, semesterName))
        return saved.id!!
    }

    fun createSemesterItemMultiple(form: SemesterItemMultipleForm, semesterName: String): MutableList<SemesterItem> {
        val semesterItemList = mutableListOf<SemesterItem>()
        form.semesterItemList.forEach {
            try {
                // 중복되는 학기별 항목만 list에서 제외
                semesterItemList.add(createOneSemesterItem(it, semesterName))
            } catch (e: DuplicateSemesterItemException) {
                // ignore
            }
        }
        return semesterItemList
    }

    fun saveSemesterItemMultiple(form: SemesterItemMultipleForm, semesterName: String) {
        repository.saveAll(createSemesterItemMultiple(form, semesterName))
    }

    fun saveSemesterItemMultipleBulkInsert(form: SemesterItemMultipleForm, semesterName: String) {
        jdbcRepository.insertSemesterList(createSemesterItemMultiple(form, semesterName))
    }

    fun modifySemesterItem(semesterItemId: Int, form: SemesterItemForm): Int {
        if (form.semesterName == null) {
            throw SemesterNameNotFoundException()
        }
        validateDuplicateSemesterItem(form.semesterName!!, form.itemId!!, semesterItemId)
        val item = form.itemId?.let {
            itemRepository
                .findById(it)
                .orElseThrow { throw ItemNotFoundException() }
        }
        return repository
            .findById(semesterItemId)
            .orElseThrow { SemesterItemNotFoundException() }
            .update(form, item!!)
    }

    fun deleteSemesterItem(semesterItemId: Int): Int {
        try {
            repository.deleteById(semesterItemId)
        } catch (e: Exception) {
            throw SemesterItemNotFoundException()
        }
        return semesterItemId
    }

    private fun validateDuplicateSemesterItem(semesterName: String, itemId: Int, semesterItemId: Int) {
        val semesterItem = repository.findBySemesterNameAndItemId(semesterName, itemId)
        if (semesterItem != null && semesterItem.id != semesterItemId) {
            throw DuplicateSemesterItemException()
        }
    }
}
