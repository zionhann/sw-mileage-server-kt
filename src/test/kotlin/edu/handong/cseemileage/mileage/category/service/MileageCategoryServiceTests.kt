package edu.handong.cseemileage.mileage.category.service

import edu.handong.cseemileage.mileage.category.domain.Category
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MileageCategoryServiceTests @Autowired constructor(
    val categoryService: CategoryService,
    val categoryRepository: CategoryRepository
) {

    @DisplayName("service: 마일리지 카테고리 save")
    @Test
    fun saveCategory() {
        // given
        val category = Category(null, "전공 상담", "전공 교수님과 진로 상담", 20)

        // when
        categoryService.saveCategory(category)
        val foundCategory = categoryRepository.findByIdOrNull(category.id!!)

        // then
        assertThat(foundCategory).isNotNull
        if (foundCategory != null) {
            assertThat(foundCategory.cname).isEqualTo("전공 상담")
        }
    }

    @DisplayName("service: CategoryRepository 의존성 주입")
    @Test
    fun getRepository() {
        assertThat(categoryService.repository).isNotNull
    }
}
