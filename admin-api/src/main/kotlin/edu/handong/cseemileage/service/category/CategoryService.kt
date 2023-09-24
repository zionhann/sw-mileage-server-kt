package edu.handong.cseemileage.service.category

import edu.handong.cseemileage.domain.mileage.Category
import edu.handong.cseemileage.dto.mileage.category.CategoryForm
import edu.handong.cseemileage.exception.mileage.category.CategoryNotFoundException
import edu.handong.cseemileage.exception.mileage.category.DuplicateCategoryException
import edu.handong.cseemileage.repository.mileage.CategoryRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class CategoryService(val repository: CategoryRepository) {
    fun saveCategory(form: CategoryForm): Int {
        validateDuplicateCategory(form.title!!, 0)
        val category = Category(
            form.title!!
        ).apply {
            categoryMaxPoints = form.categoryMaxPoints ?: 0f
            description1 = form.description1
            description2 = form.description2
            orderIdx = form.orderIdx ?: 0
            itemType = form.itemType ?: "R"
            isMulti = form.isMulti ?: false
        }

        val result = repository.save(category)

        return result.id!!
    }

    private fun validateDuplicateCategory(title: String, id: Int) {
        val findCategory = repository.findByName(title)
        if (findCategory != null && findCategory.id != id) {
            throw DuplicateCategoryException()
        }
    }

    fun update(id: Int, form: CategoryForm): Int {
        validateDuplicateCategory(form.title!!, id)
        return repository
            .findById(id)
            .orElseThrow { CategoryNotFoundException() }
            .update(form)
    }

    fun remove(savedId: Int): Int {
        try {
            repository.deleteById(savedId)
        } catch (e: Exception) {
            throw CategoryNotFoundException()
        }
        return savedId
    }
}
