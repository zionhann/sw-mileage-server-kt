package edu.handong.cseemileage.mileage.item.service

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.category.exception.CategoryNotFoundException
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.dto.ItemDto
import edu.handong.cseemileage.mileage.item.repository.ItemRepository
import org.springframework.stereotype.Service

@Service
class ItemQueryService(
    val repository: ItemRepository,
    val categoryRepository: CategoryRepository
) {
    fun getItems(): List<ItemDto.Info> {
        val items = repository.findAll()
        return items.map {
            val category = categoryRepository.findById(it.category?.id ?: 0)
                .orElseThrow { throw CategoryNotFoundException(ExceptionMessage.CATEGORY_NOT_FOUND) }
            ItemDto.Info(
                it.id,
                CategoryDto.Info(category.id!!, category.name!!, category.maxPoints!!),
                it.name,
                it.isPortfolio,
                it.description1,
                it.description2,
                it.semester,
                it.stuType
            )
        }
    }
}
