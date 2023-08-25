package edu.handong.cseemileage.mileage.item.service

import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.category.exception.CategoryNotFoundException
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.dto.ItemDto
import edu.handong.cseemileage.mileage.item.repository.ItemRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class ItemQueryService(
    val repository: ItemRepository,
    val categoryRepository: CategoryRepository,
    val modelMapper: ModelMapper
) {
    fun getItems(): List<ItemDto.InfoV1> {
        val items = repository.findAll()
        return items.map {
            val category = categoryRepository
                .findById(it.category.id!!)
                .orElseThrow { throw CategoryNotFoundException() }
            ItemDto.InfoV1(
                it.id,
                modelMapper.map(category, CategoryDto.InfoV1::class.java),
                it.name,
                it.isPortfolio,
                it.description1,
                it.description2,
                it.stuType
            )
        }
    }
}
