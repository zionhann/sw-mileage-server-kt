package edu.handong.cseemileage.mileage.semester.service

import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.dto.ItemDto
import edu.handong.cseemileage.mileage.semester.dto.SemesterDto
import edu.handong.cseemileage.mileage.semester.repository.SemesterRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class SemesterQueryService(
    val repository: SemesterRepository,
    val categoryRepository: CategoryRepository,
    val modelMapper: ModelMapper
) {
    fun getSemestersV1(semester: String): List<SemesterDto.InfoV1> {
        val semesterItems = repository.findAllByName(semester)
        return semesterItems.map {
            SemesterDto.InfoV1(
                modelMapper.map(it.item, ItemDto.InfoV2::class.java),
                modelMapper.map(it.category, CategoryDto.InfoV1::class.java),
                it.name,
                it.weight
            )
        }
    }
}