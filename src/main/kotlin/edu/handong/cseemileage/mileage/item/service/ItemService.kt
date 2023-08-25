package edu.handong.cseemileage.mileage.item.service

import edu.handong.cseemileage.mileage.category.exception.CategoryNotFoundException
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.domain.Item
import edu.handong.cseemileage.mileage.item.dto.ItemForm
import edu.handong.cseemileage.mileage.item.exception.ItemNotFoundException
import edu.handong.cseemileage.mileage.item.repository.ItemRepository
import edu.handong.cseemileage.utils.Utils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ItemService(
    val repository: ItemRepository,
    val categoryRepository: CategoryRepository
) {
    fun saveItem(form: ItemForm): Int {
        val category = form.categoryId?.let {
            categoryRepository
                .findById(it)
                .orElseThrow { throw CategoryNotFoundException() }
        }
        val item = Item.createItem(form, category!!)
        val saved = repository.save(item)
        return saved.id!!
    }

    fun modifyItem(itemId: Int, form: ItemForm): Int {
        repository.findById(itemId)
            .orElseThrow { throw ItemNotFoundException() }
            .run {
                category = categoryRepository
                    .findById(form.categoryId!!)
                    .orElseThrow { throw CategoryNotFoundException() }
                name = form.itemName!!
                isPortfolio = form.flags.isPortfolio!!
                description1 = form.description1
                description2 = form.description2
                stuType = form.stuType!!
                isVisible = Utils.booleanToString(form.flags.isVisible!!)
                isStudentVisible = Utils.booleanToString(form.flags.isStudentVisible!!)
                isStudentInput = Utils.booleanToString(form.flags.isStudentEditable!!)
                isMulti = Utils.booleanToString(form.flags.isMultiple!!)

                return id!!
            }
    }

    fun deleteItem(itemId: Int): Int {
        repository.deleteById(itemId)
        return itemId
    }
}
