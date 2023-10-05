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

    fun createSemesterItemMultiple(form: SemesterItemMultipleForm): MutableMap<String, MutableList<Any>> {
//        val semesterItemList = mutableListOf<SemesterItem>()
        // 정상 처리된 항목, 중복 발생으로 실패한 항목, 학기별 항목을 찾지 못해 실패한 항목을 map으로 반환할 예정이다.
        val map = mutableMapOf<String, MutableList<Any>>()
        map["semesterItemList"] = mutableListOf<SemesterItem>().toMutableList()
        map["복사 성공"] = mutableListOf<String>().toMutableList()
        map["복사 실패 - 중복 항목"] = mutableListOf<String>().toMutableList()
        map["복사 실패 - 학기별 항목을 찾지 못함"] = mutableListOf<Int>().toMutableList()
        form.copyFrom.forEach {
            try {
                // 중복되는 학기별 항목만 list에서 제외
                map["semesterItemList"]?.add(findOneSemesterItem(it, form.copyTo!!))
                map["복사 성공"]?.add(repository.findById(it).get().item.name)
            } catch (e: DuplicateSemesterItemException) {
                // ignore. 예외가 발생한 항목을 제외하고 정상적으로 진행한다
                map["복사 실패 - 중복 항목"]?.add(repository.findById(it).get().item.name)
            } catch (e: SemesterItemNotFoundException) {
                // ignore. 예외가 발생한 항목을 제외하고 정상적으로 진행한다
                map["복사 실패 - 학기별 항목을 찾지 못함"]?.add(it)
            }
        }
        return map
    }

    private fun findOneSemesterItem(copyFrom: Int, copyTo: String): SemesterItem {
        val semesterItemFrom = repository.findById(copyFrom)
            .orElseThrow { throw SemesterItemNotFoundException() }
        validateDuplicateSemesterItem(copyTo, semesterItemFrom.item.id, 0)

        return SemesterItem(
            item = semesterItemFrom.item,
            category = semesterItemFrom.category
        ).apply {
            this.semesterName = copyTo
            this.pointValue = semesterItemFrom.pointValue
            this.itemMaxPoints = semesterItemFrom.itemMaxPoints
            this.isMulti = semesterItemFrom.isMulti
        }
    }

    fun saveSemesterItemMultiple(form: SemesterItemMultipleForm, semesterName: String): MutableMap<String, MutableList<Any>> {
        val map = createSemesterItemMultiple(form)
        repository.saveAll(map["semesterItemList"] as MutableList<SemesterItem>)
        return map
    }

    fun saveSemesterItemMultipleBulkInsert(form: SemesterItemMultipleForm): MutableMap<String, MutableList<Any>> {
        val map = createSemesterItemMultiple(form)
        jdbcRepository.insertSemesterList(map["semesterItemList"] as MutableList<SemesterItem>)
        return map
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
