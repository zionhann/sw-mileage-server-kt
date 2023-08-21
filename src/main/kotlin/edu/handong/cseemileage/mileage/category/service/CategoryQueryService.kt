package edu.handong.cseemileage.mileage.category.service

import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.category.exception.CategoryNotFoundException
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.dto.ItemDto
import edu.handong.cseemileage.mileage.semester.dto.SemesterDto
import edu.handong.cseemileage.mileage.semester.repository.SemesterRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class CategoryQueryService(
    val repository: CategoryRepository,
    val semesterRepository: SemesterRepository,
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
        val semesterList = semesterRepository.findAllByName(semesterName)

        // 해당 학기에 사용된 카테고리 조회(중복 제거)
        val categories = semesterList.map { it.category }
        val distinctCategories = categories.distinct()

        // 반환 데이터 조립
        val categoryInfos = distinctCategories.map { category ->
            val itemInfos = category.items.map { item ->
                val semesterInfos = item.semesterItems.filter { semesterItem ->
                    // 해당 학기에 사용된 항목만 필터링
                    semesterItem.name == semesterName
                }.map { semesterItem ->
                    SemesterDto.InfoV3(
                        semesterItem.name,
                        semesterItem.weight
                    )
                }
                ItemDto.InfoV3(
                    item.id,
                    item.name,
                    item.isPortfolio,
                    item.description1,
                    item.description2,
                    item.stuType,
                    semesterInfos
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