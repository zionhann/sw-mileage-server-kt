package edu.handong.cseemileage.service.item

import edu.handong.cseemileage.domain.mileage.Item
import edu.handong.cseemileage.dto.mileage.item.ItemForm
import edu.handong.cseemileage.exception.mileage.category.CategoryNotFoundException
import edu.handong.cseemileage.exception.mileage.item.DuplicateItemException
import edu.handong.cseemileage.exception.mileage.item.ItemNotFoundException
import edu.handong.cseemileage.repository.mileage.CategoryRepository
import edu.handong.cseemileage.repository.mileage.ItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ItemService(
    val repository: ItemRepository,
    val categoryRepository: CategoryRepository
) {
    fun saveItem(form: ItemForm): Int {
        validateDuplicateItem(form.itemName!!, 0)
        val category = form.categoryId?.let {
            categoryRepository
                .findById(it)
                .orElseThrow { throw CategoryNotFoundException() }
        }
        val item = Item.createItem(form, category!!)
        val saved = repository.save(item)
        return saved.id!!
    }

    private fun validateDuplicateItem(name: String, itemId: Int) {
        val foundItem = repository.findByName(name)
        if (foundItem != null && foundItem.id != itemId) {
            throw DuplicateItemException()
        }
    }

    fun modifyItem(itemId: Int, form: ItemForm): Int {
        validateDuplicateItem(form.itemName!!, itemId)
        val category = form.categoryId?.let {
            categoryRepository
                .findById(it)
                .orElseThrow { throw CategoryNotFoundException() }
        }
        return repository
            .findById(itemId)
            .orElseThrow { ItemNotFoundException() }
            .update(form, category!!)
    }

    fun deleteItem(itemId: Int): Int {
        try {
            repository.deleteById(itemId)
        } catch (e: Exception) {
            throw ItemNotFoundException()
        }
        return itemId
    }
}
