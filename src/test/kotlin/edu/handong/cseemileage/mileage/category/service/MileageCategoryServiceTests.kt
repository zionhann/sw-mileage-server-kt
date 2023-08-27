package edu.handong.cseemileage.mileage.category.service

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.mileage.category.domain.Category
import edu.handong.cseemileage.mileage.category.dto.CategoryForm
import edu.handong.cseemileage.mileage.category.exception.CategoryNotFoundException
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.DESCRIPTION1
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.DESCRIPTION2
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.IS_MULTI
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.ITEM_TYPE
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.NAME
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.ORDER_IDX
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.UPDATE_DESCRIPTION1
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.UPDATE_DESCRIPTION2
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.UPDATE_IS_MULTI
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.UPDATE_ITEM_TYPE
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.UPDATE_NAME
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.UPDATE_ORDER_IDX
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class MileageCategoryServiceTests @Autowired constructor(
    val categoryService: CategoryService,
    val categoryQueryService: CategoryQueryService,
    val categoryRepository: CategoryRepository
) {

    @DisplayName("service: 마일리지 카테고리 저장")
    @Test
    fun saveCategory1() {
        // given
        val form = CategoryForm(
            title = NAME,
            orderIdx = ORDER_IDX,
            itemType = ITEM_TYPE,
            isMulti = IS_MULTI,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2
        )

        // when
        val id = categoryService.saveCategory(form)

        // then
        assertThat(id).isNotNull
    }

    @DisplayName("service: 필수값이 아닌 필드는 null이어도 저장 가능")
    @Test
    fun saveCategory2() {
        // given
        val form = CategoryForm(
            title = NAME,
            orderIdx = null,
            itemType = null,
            isMulti = null,
            description1 = null,
            description2 = null
        )

        // when
        val id = categoryService.saveCategory(form)

        // then
        assertThat(id).isNotNull
    }

    @DisplayName("service: CategoryRepository 의존성 주입")
    @Test
    fun getRepository() {
        assertThat(categoryService.repository).isNotNull
    }

    @DisplayName("service: 마일리지 카테고리 조회")
    @Test
    fun mileageCategoryServiceTests_41() {
        // Given
        val form = CategoryForm(
            title = NAME,
            orderIdx = ORDER_IDX,
            itemType = ITEM_TYPE,
            isMulti = IS_MULTI,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2
        )
        val savedId = categoryService.saveCategory(form)

        // When
        val list = categoryQueryService.getCategories()

        // Then
        assertThat(list).isNotEmpty
        list.find { it.id == savedId }.let {
            assertThat(it).isNotNull
            assertThat(it?.name).isEqualTo(NAME)
        }
    }

    @DisplayName("service: 마일리지 카테고리 수정")
    @Test
    fun mileageCategoryServiceTests_58() {
        // Given
        val category = Category(NAME).apply {
            description1 = DESCRIPTION1
            description2 = DESCRIPTION2
            orderIdx = ORDER_IDX
            itemType = ITEM_TYPE
            isMulti = IS_MULTI
        }
        val updateForm = CategoryForm(
            title = UPDATE_NAME,
            orderIdx = UPDATE_ORDER_IDX,
            itemType = UPDATE_ITEM_TYPE,
            isMulti = UPDATE_IS_MULTI,
            description1 = UPDATE_DESCRIPTION1,
            description2 = UPDATE_DESCRIPTION2
        )
        categoryRepository.save(category)

        // When
        categoryService.update(category.id, updateForm)
        val updatedCategory = categoryRepository.findById(category.id).get()

        // Then
        assertThat(updatedCategory.id).isEqualTo(category.id)
        assertThat(updatedCategory.name).isEqualTo(UPDATE_NAME)
        assertThat(updatedCategory.orderIdx).isEqualTo(UPDATE_ORDER_IDX)
        assertThat(updatedCategory.itemType).isEqualTo(UPDATE_ITEM_TYPE)
        assertThat(updatedCategory.isMulti).isEqualTo(UPDATE_IS_MULTI)
        assertThat(updatedCategory.description1).isEqualTo(UPDATE_DESCRIPTION1)
        assertThat(updatedCategory.description2).isEqualTo(UPDATE_DESCRIPTION2)
    }

    @DisplayName("service: 마일리지 카테고리 수정 - 존재하지 않는 값은 원래의 값")
    @Test
    fun mileageCategoryServiceTests_143() {
        // Given
        val category = Category(NAME).apply {
            description1 = DESCRIPTION1
            description2 = DESCRIPTION2
            orderIdx = ORDER_IDX
            itemType = ITEM_TYPE
            isMulti = IS_MULTI
        }
        val updateForm = CategoryForm(
            title = UPDATE_NAME,
            orderIdx = null,
            itemType = null,
            isMulti = null,
            description1 = null,
            description2 = null
        )
        categoryRepository.save(category)

        // When
        categoryService.update(category.id, updateForm)
        val updatedCategory = categoryRepository.findById(category.id).get()

        // Then
        assertThat(updatedCategory.id).isEqualTo(category.id)
        assertThat(updatedCategory.name).isEqualTo(UPDATE_NAME)
        assertThat(updatedCategory.orderIdx).isEqualTo(ORDER_IDX)
        assertThat(updatedCategory.itemType).isEqualTo(ITEM_TYPE)
        assertThat(updatedCategory.isMulti).isEqualTo(IS_MULTI)
        assertThat(updatedCategory.description1).isEqualTo(DESCRIPTION1)
        assertThat(updatedCategory.description2).isEqualTo(DESCRIPTION2)
    }

    @DisplayName("service: 마일리지 카테고리 삭제")
    @Test
    fun mileageCategoryServiceTests_75() {
        // Given
        val form = CategoryForm(
            title = NAME,
            orderIdx = ORDER_IDX,
            itemType = ITEM_TYPE,
            isMulti = IS_MULTI,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2
        )
        val saved = categoryService.saveCategory(form)

        // When
        categoryService.remove(saved)

        // Then
        val e = assertThrows<CategoryNotFoundException> {
            categoryQueryService.getCategoryById(saved)
        }
        assertThat(e.info).isEqualTo(ExceptionMessage.CATEGORY_NOT_FOUND)
    }
}
