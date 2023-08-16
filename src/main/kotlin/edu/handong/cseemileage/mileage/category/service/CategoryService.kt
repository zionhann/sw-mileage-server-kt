package edu.handong.cseemileage.mileage.category.service

import edu.handong.cseemileage.mileage.category.domain.Category
import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.category.dto.CategoryForm
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class CategoryService(val repository: CategoryRepository) {
    fun saveCategory(form: CategoryForm): CategoryDto.Info {
        val category = Category(form.title, form.description, form.maxPoints)
        val result = repository.save(category)

        return CategoryDto.Info(
            id = result.id,
            title = result.cname,
            maxPoints = result.maxPoint
        )
    }

    fun getCategories(): List<CategoryDto.Info> {
        val categories = repository.findAll()
        return categories.map { CategoryDto.Info(it.id, it.cname, it.maxPoint) }
    }
}
