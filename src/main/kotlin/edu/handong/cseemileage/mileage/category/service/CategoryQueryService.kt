package edu.handong.cseemileage.mileage.category.service

import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.category.exception.CategoryNotFoundException
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.dto.ItemDto
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemDto
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepository
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
                    itemType = it.itemType,
                    isMulti = it.isMulti
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
                    itemType = it.itemType,
                    isMulti = it.isMulti
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
            val itemInfos = category.items.map { item ->
                val semesterInfos = item.semesterItems.filter { semesterItem ->
                    // 해당 학기에 사용된 항목만 필터링
                    semesterItem.semesterName == semesterName
                }.map { semesterItem ->
                    SemesterItemDto.Info(
                        id = semesterItem.id,
                        semesterName = semesterItem.semesterName,
                        points = semesterItem.pointValue,
                        itemMaxPoints = semesterItem.itemMaxPoints
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
                    isMulti = stringToBoolean(item.isMulti),
                    isStudentInput = stringToBoolean(item.isStudentInput),
                    isStudentVisible = stringToBoolean(item.isStudentVisible),
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
                itemType = category.itemType,
                isMulti = category.isMulti,
                items = itemInfos
            )
        }

        return categoryInfos
    }
}
