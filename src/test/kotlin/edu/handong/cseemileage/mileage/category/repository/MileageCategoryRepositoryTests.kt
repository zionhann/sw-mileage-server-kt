package edu.handong.cseemileage.mileage.category.repository

import edu.handong.cseemileage.mileage.category.domain.Category
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MileageCategoryRepositoryTests @Autowired constructor(
    private val categoryRepository: CategoryRepository
) {
    @DisplayName("repository: 마일리지 카테고리 persist")
    @Test
    fun saveCategory() {
        // given
        val category = Category("전공 상담", "교수님과 전공 상담 진행", 20)

        // when
        val foundCategory = categoryRepository.save(category)

        // then
        assertThat(foundCategory).isNotNull
    }

    @DisplayName("repository: 마일리지 카테고리 find")
    @Test
    fun mileageCategoryRepositoryTests_31() {
        // Given
        val category = Category("전공 상담", "교수님과 전공 상담 진행", 20)
        val savedId = categoryRepository.save(category).id!!

        // When
        val found = categoryRepository.findById(savedId)

        // Then
        assertThat(found.isPresent).isTrue
    }
}
