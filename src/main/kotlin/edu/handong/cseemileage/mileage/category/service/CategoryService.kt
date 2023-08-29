package edu.handong.cseemileage.mileage.category.service

import edu.handong.cseemileage.mileage.category.domain.Category
import edu.handong.cseemileage.mileage.category.dto.CategoryForm
import edu.handong.cseemileage.mileage.category.exception.CategoryNotFoundException
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class CategoryService(val repository: CategoryRepository) {
    fun saveCategory(form: CategoryForm): Int {
        val category = Category(
            form.title!!
        ).apply {
            description1 = form.description1
            description2 = form.description2
            orderIdx = form.orderIdx ?: 0
            itemType = form.itemType ?: "R"
            isMulti = form.isMulti ?: false
        }

        val result = repository.save(category)

        return result.id!!
    }

    fun update(id: Int, form: CategoryForm): Int {
        return repository
            .findById(id)
            .orElseThrow { CategoryNotFoundException() }
            .update(form)
    }

    fun remove(savedId: Int): Int {
        repository.deleteById(savedId)
        return savedId
    }
}
