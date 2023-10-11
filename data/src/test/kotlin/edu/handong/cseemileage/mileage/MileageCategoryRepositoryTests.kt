package edu.handong.cseemileage.mileage

import edu.handong.cseemileage.domain.mileage.Category
import edu.handong.cseemileage.repository.mileage.CategoryRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class MileageCategoryRepositoryTests @Autowired constructor(
    private val categoryRepository: CategoryRepository
) {
    companion object {
        const val NAME = "전공 상담"
        const val ORDER_IDX = 5
        const val DESCRIPTION1 = "description1"
        const val DESCRIPTION2 = "description2"
        const val ITEM_TYPE = "T"
        const val IS_MULTI = true

        const val UPDATE_NAME = "전공 상담 수정"
        const val UPDATE_ORDER_IDX = 2
        const val UPDATE_DESCRIPTION1 = "description1 수정"
        const val UPDATE_DESCRIPTION2 = "description2 수정"
        const val UPDATE_ITEM_TYPE = "D"
        const val UPDATE_IS_MULTI = false

        const val DEFAULT_ORDER_IDX = 0
        const val DEFAULT_ITEM_TYPE = "A"
        const val DEFAULT_IS_MULTI = false
    }

    @DisplayName("repository: 마일리지 카테고리 persist")
    @Test
    fun saveCategory1() {
        // given
        val category = Category(NAME).apply {
            description1 = DESCRIPTION1
            description2 = DESCRIPTION2
            orderIdx = ORDER_IDX
            type = ITEM_TYPE
        }

        // when
        val foundCategory = categoryRepository.save(category)

        // then
        assertThat(foundCategory).isNotNull
        assertThat(foundCategory).isEqualTo(category)
        assertThat(foundCategory.name).isEqualTo(NAME)
        assertThat(foundCategory.orderIdx).isEqualTo(ORDER_IDX)
        assertThat(foundCategory.type).isEqualTo(ITEM_TYPE)
        assertThat(foundCategory.description1).isEqualTo(DESCRIPTION1)
        assertThat(foundCategory.description2).isEqualTo(DESCRIPTION2)
    }

    @DisplayName("모든 값을 지정하지 않아도 @ColumnDefault 값이 들어가야 한다")
    @Test
    fun saveCategory2() {
        // given
        val category = Category(NAME)
        // when
        val foundCategory = categoryRepository.save(category)

        // then
        assertThat(foundCategory).isNotNull
        assertThat(foundCategory).isEqualTo(category)
        assertThat(foundCategory.name).isEqualTo(NAME)
        assertThat(foundCategory.description1).isNull()
        assertThat(foundCategory.description2).isNull()
        assertThat(foundCategory.orderIdx).isEqualTo(DEFAULT_ORDER_IDX)
        assertThat(foundCategory.type).isEqualTo(DEFAULT_ITEM_TYPE)
    }

    @DisplayName("repository: 마일리지 카테고리 find")
    @Test
    fun mileageCategoryRepositoryTests_31() {
        // Given
        val category = Category(NAME).apply {
            description1 = DESCRIPTION1
            description2 = DESCRIPTION2
            orderIdx = ORDER_IDX
            type = ITEM_TYPE
        }
        val savedId = categoryRepository.save(category).id!!

        // When
        val found = categoryRepository.findById(savedId)

        // Then
        assertThat(found.isPresent).isTrue
    }

    @DisplayName("repository: 마일리지 카테고리 삭제")
    @Test
    fun mileageCategoryRepositoryTests_45() {
        // Given
        val category = Category(NAME).apply {
            description1 = DESCRIPTION1
            description2 = DESCRIPTION2
            orderIdx = ORDER_IDX
            type = ITEM_TYPE
        }
        val saved = categoryRepository.save(category)

        // When
        categoryRepository.delete(saved)
        val found = categoryRepository.findById(saved.id!!)

        // Then
        assertThat(found).isEmpty
    }
}
