package edu.handong.cseemileage.mileage.item.service

import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.category.exception.CategoryNotFoundException
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.dto.ItemDto
import edu.handong.cseemileage.mileage.item.repository.ItemRepository
import edu.handong.cseemileage.utils.Utils.Companion.stringToBoolean
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
                id = it.id,
                category = modelMapper.map(category, CategoryDto.InfoV1::class.java),
                name = it.name,
                isPortfolio = it.isPortfolio,
                description1 = it.description1,
                description2 = it.description2,
                stuType = it.stuType,
                isMulti = stringToBoolean(it.isMulti),
                isStudentInput = stringToBoolean(it.isStudentInput),
                isStudentVisible = stringToBoolean(it.isStudentVisible),
                isVisible = stringToBoolean(it.isVisible)
            )
        }
    }

    fun getItemsByCategoryId(categoryId: Int): List<ItemDto.deleteFailureInfo> {
        val items = repository.findAllByCategoryId(categoryId)
        return items.map {
            ItemDto.deleteFailureInfo(
                id = it.id,
                name = it.name
            )
        }
    }
}
