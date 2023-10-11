package edu.handong.cseemileage.service.category

import edu.handong.cseemileage.dto.mileage.category.CategoryDto
import edu.handong.cseemileage.dto.mileage.item.ItemDto
import edu.handong.cseemileage.dto.mileage.semesterItem.SemesterItemDto
import edu.handong.cseemileage.exception.mileage.category.CategoryNotFoundException
import edu.handong.cseemileage.repository.mileage.CategoryRepository
import edu.handong.cseemileage.repository.mileage.SemesterItemRepository
import edu.handong.cseemileage.utils.Utils.Companion.stringToBoolean
import org.springframework.stereotype.Service

@Service
class CategoryQueryService(
    val repository: CategoryRepository,
    val semesterItemRepository: SemesterItemRepository
) {
    fun getCategories(): List<CategoryDto.Info> {
        return repository
            .findAll()
            .map {
                CategoryDto.Info(
                    id = it.id,
                    name = it.name,
                    description1 = it.description1,
                    description2 = it.description2,
                    categoryMaxPoints = it.categoryMaxPoints,
                    orderIdx = it.orderIdx,
                    type = it.type,
                    modDate = it.modDate
                )
            }
    }

    fun getCategoryById(id: Int): CategoryDto.Info {
        repository
            .findById(id)
            .orElseThrow { throw CategoryNotFoundException() }
            .let {
                return CategoryDto.Info(
                    id = it.id,
                    name = it.name,
                    description1 = it.description1,
                    description2 = it.description2,
                    categoryMaxPoints = it.categoryMaxPoints,
                    orderIdx = it.orderIdx,
                    type = it.type,
                    modDate = it.modDate
                )
            }
    }

    /**
     * 학기별 카테고리, 항목, 학기 정보를 가져온다
     * */
    fun getCategoryWithItemAndSemester(semesterName: String): List<CategoryDto.Info> {
        // 해당 학기 정보 전체 조회
        val semesterList = semesterItemRepository.findAllBySemesterName(semesterName)

        // 해당 학기에 사용된 카테고리 조회(중복 제거)
        val categories = semesterList.map { it.category }
        val distinctCategories = categories.distinct()

        // 반환 데이터 조립
        val categoryInfos = distinctCategories.map { category ->
            val itemInfos = category.items.filter {
                    // 해당 학기에 사용된 항목만 필터링
                    item ->
                semesterList.any { it.item == item }
            }.map { item ->
                val semesterInfos = item.semesterItems.filter { semesterItem ->
                    // 해당 학기에 사용된 항목만 필터링
                    semesterItem.semesterName == semesterName
                }.map { semesterItem ->
                    SemesterItemDto.Info(
                        id = semesterItem.id,
                        semesterName = semesterItem.semesterName,
                        points = semesterItem.pointValue,
                        itemMaxPoints = semesterItem.itemMaxPoints,
                        isMulti = stringToBoolean(semesterItem.isMulti)
                    )
                }
                ItemDto.Info(
                    id = item.id,
                    name = item.name,
                    isPortfolio = item.isPortfolio,
                    description1 = item.description1,
                    description2 = item.description2,
                    stuType = item.stuType,
                    isVisible = stringToBoolean(item.isVisible),
                    isStudentVisible = stringToBoolean(item.isStudentVisible),
                    isStudentInput = stringToBoolean(item.isStudentInput),
                    semesterItems = semesterInfos
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

        return categoryInfos
    }
}
