package edu.handong.cseemileage.mileage.semesterItem.service

import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.dto.ItemDto
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemDto
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class SemesterItemQueryService(
    val repository: SemesterItemRepository,
    val categoryRepository: CategoryRepository,
    val modelMapper: ModelMapper
) {
    fun getSemesterItemsV1(semesterItems: String): List<SemesterItemDto.InfoV1> {
        val semesterItems = repository.findAllBySemesterName(semesterItems)
        return semesterItems.map {
            SemesterItemDto.InfoV1(
                modelMapper.map(it.item, ItemDto.InfoV2::class.java),
                modelMapper.map(it.category, CategoryDto.InfoV1::class.java),
                it.semesterName,
                it.pointValue
            )
        }
    }
}
