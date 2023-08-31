package edu.handong.cseemileage.mileage.item.service

import edu.handong.cseemileage.mileage.category.exception.CategoryNotFoundException
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.domain.Item
import edu.handong.cseemileage.mileage.item.dto.ItemForm
import edu.handong.cseemileage.mileage.item.exception.DuplicateItemException
import edu.handong.cseemileage.mileage.item.exception.ItemNotFoundException
import edu.handong.cseemileage.mileage.item.repository.ItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ItemService(
    val repository: ItemRepository,
    val categoryRepository: CategoryRepository
) {
    fun saveItem(form: ItemForm): Int {
        validateDuplicateItem(form.itemName!!)
        val category = form.categoryId?.let {
            categoryRepository
                .findById(it)
                .orElseThrow { throw CategoryNotFoundException() }
        }
        val item = Item.createItem(form, category!!)
        val saved = repository.save(item)
        return saved.id!!
    }

    private fun validateDuplicateItem(name: String) {
        val foundItem = repository.findByName(name)
        if (foundItem != null) {
            throw DuplicateItemException()
        }
    }

    fun modifyItem(itemId: Int, form: ItemForm): Int {
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
        repository.deleteById(itemId)
        return itemId
    }
}
