package edu.handong.cseemileage.mileage.subitem.service

import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.subitem.domain.Subitem
import edu.handong.cseemileage.mileage.subitem.dto.SubitemForm
import edu.handong.cseemileage.mileage.subitem.repository.SubitemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SubitemService(
    val repository: SubitemRepository,
    val categoryRepsoitory: CategoryRepository
) {
    fun saveSubitem(form: SubitemForm) {
        val category = categoryRepsoitory.findById(form.categoryId)
            .orElseThrow { throw RuntimeException("Category not found") }
        val subitem = Subitem(category, form.subitemName, form.weight, form.isPortfolio, form.maxPoint, form.description1, form.description2, form.semester, form.stuType)
        repository.save(subitem)
    }

    fun modifySubitem(subitemId: Int, form: SubitemForm) {
        repository.findById(subitemId)
            .orElseThrow { throw RuntimeException("Subitem not found") }
            .apply {
                category = categoryRepsoitory.findById(form.categoryId)
                    .orElseThrow { throw RuntimeException("Category not found") }
                subitemName = form.subitemName
                weight = form.weight
                isPortfolio = form.isPortfolio
                maxPoint = form.maxPoint
                description1 = form.description1
                description2 = form.description2
                semester = form.semester
                stuType = form.stuType
            }
    }

    fun deleteSubitem(subitemId: Int) {
        repository.deleteById(subitemId)
    }
}
