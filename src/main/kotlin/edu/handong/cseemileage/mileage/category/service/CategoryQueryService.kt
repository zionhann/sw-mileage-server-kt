package edu.handong.cseemileage.mileage.category.service

import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.category.exception.CategoryNotFoundException
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.dto.ItemDto
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemDto
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepository
import edu.handong.cseemileage.utils.Utils.Companion.stringToBoolean
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class CategoryQueryService(
    val repository: CategoryRepository,
    val semesterItemRepository: SemesterItemRepository,
    val modelMapper: ModelMapper
) {
    fun getCategories(): List<CategoryDto.InfoV1> {
        return repository
            .findAll()
            .map {
                modelMapper.map(it, CategoryDto.InfoV1::class.java)
            }
    }

    fun getCategoriesWithItems(): List<CategoryDto.InfoV2> {
        val categories = repository.findAll()
        return categories.map {
            CategoryDto.InfoV2(
                modelMapper.map(it, CategoryDto.InfoV1::class.java),
                it.items.map { item ->
                    modelMapper.map(item, ItemDto.InfoV2::class.java)
                }
            )
        }
    }

    fun getCategoryById(id: Int): CategoryDto.InfoV1 {
        repository
            .findById(id)
            .orElseThrow { throw CategoryNotFoundException() }
            .let {
                return modelMapper.map(it, CategoryDto.InfoV1::class.java)
            }
    }

    /**
     * 학기별 카테고리, 항목, 학기 정보를 가져온다
     * */
    fun getCategoryWithItemAndSemester(semesterName: String): List<CategoryDto.InfoV3> {
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
                    SemesterItemDto.InfoV3(
                        id = semesterItem.id,
                        semesterName = semesterItem.semesterName,
                        points = semesterItem.pointValue,
                        itemMaxPoints = semesterItem.itemMaxPoints,
                        categoryMaxPoints = semesterItem.categoryMaxPoints
                    )
                }
                ItemDto.InfoV3(
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
            CategoryDto.InfoV3(
                modelMapper.map(category, CategoryDto.InfoV1::class.java),
                itemInfos
            )
        }.toList()

        return categoryInfos
    }
}
