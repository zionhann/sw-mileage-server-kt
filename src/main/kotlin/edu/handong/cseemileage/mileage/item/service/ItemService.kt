package edu.handong.cseemileage.mileage.item.service

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.mileage.category.exception.CategoryNotFoundException
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.domain.Item
import edu.handong.cseemileage.mileage.item.dto.ItemForm
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
        val category = categoryRepository.findById(form.categoryId)
            .orElseThrow { throw RuntimeException("Category not found") }
        val item = Item.createItem(form, category)
        val saved = repository.save(item)

        return saved.id!!
    }

    fun modifyItem(itemId: Int, form: ItemForm) {
        repository.findById(itemId)
            .orElseThrow { throw RuntimeException("Item not found") }
            .apply {
                category = categoryRepository.findById(form.categoryId)
                    .orElseThrow { throw CategoryNotFoundException(ExceptionMessage.CATEGORY_NOT_FOUND) }
                name = form.itemName
                isPortfolio = form.isPortfolio
                description1 = form.description1
                description2 = form.description2
                stuType = form.stuType
            }
    }

    fun deleteItem(itemId: Int) {
        repository.deleteById(itemId)
    }
}
