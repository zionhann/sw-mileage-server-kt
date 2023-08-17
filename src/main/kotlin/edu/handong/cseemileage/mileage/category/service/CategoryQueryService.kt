package edu.handong.cseemileage.mileage.category.service

import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.category.exception.CategoryNotFoundException
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryQueryService(val repository: CategoryRepository) {
    fun getCategories(): List<CategoryDto.Info> {
        return repository
            .findAll()
            .map { CategoryDto.Info(it.id!!, it.name!!, it.maxPoints!!) }
    }

    fun getCategoryById(id: Int): CategoryDto.Info {
        repository
            .findById(id)
            .orElseThrow { throw CategoryNotFoundException() }
            .let {
                return CategoryDto.Info(it.id!!, it.name!!, it.maxPoints!!)
            }
    }
}
