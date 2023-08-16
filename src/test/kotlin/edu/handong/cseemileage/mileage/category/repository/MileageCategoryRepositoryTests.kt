package edu.handong.cseemileage.mileage.category.repository

import edu.handong.cseemileage.mileage.category.domain.Category
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MileageCategoryRepositoryTests @Autowired constructor(
    val entityManager: TestEntityManager,
    val categoryRepository: CategoryRepository
) {
    @DisplayName("repository: 마일리지 카테고리 persist")
    @Test
    fun saveCategory() {
        // given
        val category = Category(null, "전공 상담", "교수님과 전공 상담 진행", 20)

        // when
        entityManager.persist(category)
        entityManager.flush()
        val foundCategory = categoryRepository.findByIdOrNull(category.id!!)

        // then
        assertThat(foundCategory).isEqualTo(category)
    }
}
