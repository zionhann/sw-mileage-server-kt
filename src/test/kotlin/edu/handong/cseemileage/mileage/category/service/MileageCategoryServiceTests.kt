package edu.handong.cseemileage.mileage.category.service

import edu.handong.cseemileage.mileage.category.dto.CategoryForm
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class MileageCategoryServiceTests @Autowired constructor(
    val categoryService: CategoryService
) {

    @DisplayName("service: 마일리지 카테고리 save")
    @Test
    fun saveCategory() {
        // given
        val category = CategoryForm("전공 상담", "전공 교수님과 진로 상담", 20)

        // when
        val result = categoryService.saveCategory(category)

        // then
        assertThat(result.id).isNotNull
        assertThat(result.title).isEqualTo("전공 상담")
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
        val saved = categoryService.saveCategory(form)

        // When
        val list = categoryService.getCategories()

        // Then
        assertThat(list).isNotEmpty
        list.find { it.id == saved.id }.let {
            assertThat(it).isNotNull
            assertThat(it?.title).isEqualTo("전공 상담")
        }
    }
}
