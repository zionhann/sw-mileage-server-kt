package edu.handong.cseemileage.mileage.item.service

import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.dto.ItemDto
import edu.handong.cseemileage.mileage.item.repository.ItemRepository
import edu.handong.cseemileage.utils.Utils.Companion.stringToBoolean
import org.springframework.stereotype.Service

@Service
class ItemQueryService(
    val repository: ItemRepository,
    val categoryRepository: CategoryRepository
) {
    fun getItems(): List<ItemDto.Info> {
        val items = repository.findAllWithCategory()
        return items.map { item ->
            ItemDto.Info(
                id = item.id,
                category = CategoryDto.Info(
                    id = item.category.id,
                    name = item.category.name,
                    categoryMaxPoints = item.category.categoryMaxPoints,
                    itemType = item.category.itemType,
                    isMulti = item.category.isMulti,
                    modDate = item.category.modDate
                ),
                name = item.name,
                itemMaxPoints = item.itemMaxPoints,
                isPortfolio = item.isPortfolio,
                description1 = item.description1,
                description2 = item.description2,
                stuType = item.stuType,
                isVisible = stringToBoolean(item.isVisible),
                isStudentVisible = stringToBoolean(item.isStudentVisible),
                isStudentInput = stringToBoolean(item.isStudentInput),
                isMulti = stringToBoolean(item.isMulti),
                modDate = item.modDate
            )
        }
    }

    fun getItemsByCategoryId(categoryId: Int): List<ItemDto.Info> {
        val items = repository.findAllByCategoryId(categoryId)
        return items.map {
            ItemDto.Info(
                id = it.id,
                name = it.name,
                modDate = it.modDate
            )
        }
    }

    fun getItemsWithCategory(): List<CategoryDto.Info> {
        val list = repository.findAllWithCategory()
        val categories = list.map { it.category }
        val categoriesSet = categories.distinct()

        return categoriesSet.map { category ->
            val itemInfos = category.items.map { item ->
                ItemDto.Info(
                    id = item.id,
                    name = item.name,
                    itemMaxPoints = item.itemMaxPoints,
                    isPortfolio = item.isPortfolio,
                    description1 = item.description1,
                    description2 = item.description2,
                    stuType = item.stuType,
                    isVisible = stringToBoolean(item.isVisible),
                    isStudentVisible = stringToBoolean(item.isStudentVisible),
                    isStudentInput = stringToBoolean(item.isStudentInput),
                    isMulti = stringToBoolean(item.isMulti),
                    modDate = item.modDate
                )
            }
            CategoryDto.Info(
                id = category.id,
                name = category.name,
                description1 = category.description1,
                description2 = category.description2,
                categoryMaxPoints = category.categoryMaxPoints,
                orderIdx = category.orderIdx,
                itemType = category.itemType,
                isMulti = category.isMulti,
                modDate = category.modDate,
                items = itemInfos
            )
        }
    }
}
