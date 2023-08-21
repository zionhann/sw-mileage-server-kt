package edu.handong.cseemileage.mileage.category.service

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.mileage.category.dto.CategoryForm
import edu.handong.cseemileage.mileage.category.exception.CategoryNotFoundException
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
    val categoryQueryService: CategoryQueryService
) {

    @DisplayName("service: 마일리지 카테고리 save")
    @Test
    fun saveCategory() {
        // given
        val category = CategoryForm("전공 상담", "전공 교수님과 진로 상담", 20)

        // when
        val id = categoryService.saveCategory(category)

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
        val form = CategoryForm("전공 상담", "전공 교수님과 진로 상담", 20)
        val savedId = categoryService.saveCategory(form)

        // When
        val list = categoryQueryService.getCategories()

        // Then
        assertThat(list).isNotEmpty
        list.find { it.id == savedId }.let {
            assertThat(it).isNotNull
            assertThat(it?.name).isEqualTo("전공 상담")
        }
    }

    @DisplayName("service: 마일리지 카테고리 수정")
    @Test
    fun mileageCategoryServiceTests_58() {
        // Given
        val form = CategoryForm("전공 상담", "전공 교수님과 진로 상담", 20)
        val updateForm = CategoryForm("봉사 마일리지", "-", 10)
        val id = categoryService.saveCategory(form)

        // When
        categoryService.update(id, updateForm)
        val res = categoryQueryService.getCategoryById(id)

        // Then
        assertThat(res.id).isEqualTo(id)
        assertThat(res.name).isEqualTo("봉사 마일리지")
        assertThat(res.maxPoints).isEqualTo(10)
    }

    @DisplayName("service: 마일리지 카테고리 삭제")
    @Test
    fun mileageCategoryServiceTests_75() {
        // Given
        val form = CategoryForm("전공 상담", "전공 교수님과 진로 상담", 20)
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
