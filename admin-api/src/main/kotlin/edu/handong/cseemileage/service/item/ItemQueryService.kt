package edu.handong.cseemileage.service.item

import edu.handong.cseemileage.dto.mileage.category.CategoryDto
import edu.handong.cseemileage.dto.mileage.item.ItemDto
import edu.handong.cseemileage.repository.mileage.CategoryRepository
import edu.handong.cseemileage.repository.mileage.ItemRepository
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
                    type = item.category.type,
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
                modDate = item.modDate,
                semesterItemCount = item.semesterItems.size
            )
        }
    }

    fun getItemsByCategoryId(categoryId: Int): List<ItemDto.Info> {
        val items = repository.findAllByCategoryId(categoryId)
        return items.map {
            ItemDto.Info(
                id = it.id,
                name = it.name,
                modDate = it.modDate,
                semesterItemCount = it.semesterItems.size
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
                    modDate = item.modDate,
                    semesterItemCount = item.semesterItems.size
                )
            }
            CategoryDto.Info(
                id = category.id,
                name = category.name,
                description1 = category.description1,
                description2 = category.description2,
                categoryMaxPoints = category.categoryMaxPoints,
                orderIdx = category.orderIdx,
                type = category.type,
                modDate = category.modDate,
                items = itemInfos
            )
        }
    }
}
