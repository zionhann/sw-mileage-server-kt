package edu.handong.cseemileage.mileage.category.service

import edu.handong.cseemileage.mileage.category.domain.Category
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class CategoryService(val repository: CategoryRepository) {
    fun saveCategory(category: Category) {
        repository.save(category)
    }
}
