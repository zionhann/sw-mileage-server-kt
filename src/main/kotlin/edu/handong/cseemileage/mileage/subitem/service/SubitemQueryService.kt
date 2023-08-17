package edu.handong.cseemileage.mileage.subitem.service

import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.subitem.dto.SubitemDto
import edu.handong.cseemileage.mileage.subitem.repository.SubitemRepository
import org.springframework.stereotype.Service

@Service
class SubitemQueryService(
    val repository: SubitemRepository,
    val categoryRepsoitory: CategoryRepository
) {
    fun getSubitems(): List<SubitemDto.Info> {
        val subitems = repository.findAll()
        return subitems.map {
            val category = categoryRepsoitory.findById(it.category?.id ?: 0)
                .orElseThrow { throw RuntimeException("Category not found") }
            SubitemDto.Info(it.id, CategoryDto.Info(category.id!!, category.cname!!, category.maxPoint!!), it.subitemName, it.weight, it.isPortfolio, it.maxPoint, it.description1, it.description2, it.semester, it.stuType)
        }
    }
}
