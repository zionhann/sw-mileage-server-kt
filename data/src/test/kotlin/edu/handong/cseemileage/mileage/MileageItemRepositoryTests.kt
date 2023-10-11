package edu.handong.cseemileage.mileage

import edu.handong.cseemileage.domain.mileage.Category
import edu.handong.cseemileage.domain.mileage.Item
import edu.handong.cseemileage.repository.mileage.CategoryRepository
import edu.handong.cseemileage.repository.mileage.ItemRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.transaction.annotation.Transactional

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class MileageItemRepositoryTests @Autowired constructor(
    private val itemRepository: ItemRepository,
    private val categoryRepository: CategoryRepository
) {

    companion object {
        const val NAME = "전공 상담"
        const val DESCRIPTION1 = "description1"
        const val DESCRIPTION2 = "description2"
        const val STU_TYPE = "R"
        const val IS_VISIBLE = "N"
        const val IS_PORTFOLIO = true
        const val IS_STUDENT_VISIBLE = "Y"
        const val IS_STUDENT_INPUT = "Y"
        const val IS_MULTI = "Y"

        const val UPDATE_NAME = "전공 상담 수정"
        const val UPDATE_DESCRIPTION1 = "description1 수정"
        const val UPDATE_DESCRIPTION2 = "description2 수정"
        const val UPDATE_STU_TYPE = "T"
        const val UPDATE_IS_VISIBLE = "Y"
        const val UPDATE_IS_PORTFOLIO = false
        const val UPDATE_IS_STUDENT_VISIBLE = "N"
        const val UPDATE_IS_STUDENT_INPUT = "N"
        const val UPDATE_IS_MULTI = "N"

        const val DEFAULT_IS_VISIBLE = "Y"
        const val DEFAULT_IS_PORTFOLIO = false
        const val DEFAULT_IS_STUDENT_VISIBLE = "N"
        const val DEFAULT_IS_STUDENT_INPUT = "N"
        const val DEFAULT_IS_MULTI = "N"
    }

    @DisplayName("repository: 마일리지 항목 persist")
    @Test
    fun saveSubitem1() {
        // given
        val category = Category(MileageCategoryRepositoryTests.NAME)
        val savedCategory = categoryRepository.save(category)
        val item = Item(
            category = savedCategory,
            name = NAME
        ).apply {
            description1 = DESCRIPTION1
            description2 = DESCRIPTION2
            stuType = STU_TYPE
            isVisible = IS_VISIBLE
            isPortfolio = IS_PORTFOLIO
            isStudentVisible = IS_STUDENT_VISIBLE
            isStudentInput = IS_STUDENT_INPUT
        }

        // when
        itemRepository.save(item)
        val savedItem = itemRepository.findById(item.id!!)

        // then
        Assertions.assertThat(savedItem).isNotNull
        Assertions.assertThat(savedItem.get()).isEqualTo(item)
        Assertions.assertThat(savedItem.get().category).isEqualTo(savedCategory)
        Assertions.assertThat(savedItem.get().name).isEqualTo(NAME)
        Assertions.assertThat(savedItem.get().description1).isEqualTo(DESCRIPTION1)
        Assertions.assertThat(savedItem.get().description2).isEqualTo(DESCRIPTION2)
        Assertions.assertThat(savedItem.get().stuType).isEqualTo(STU_TYPE)
        Assertions.assertThat(savedItem.get().isVisible).isEqualTo(IS_VISIBLE)
        Assertions.assertThat(savedItem.get().isPortfolio).isEqualTo(IS_PORTFOLIO)
        Assertions.assertThat(savedItem.get().isStudentVisible).isEqualTo(IS_STUDENT_VISIBLE)
        Assertions.assertThat(savedItem.get().isStudentInput).isEqualTo(IS_STUDENT_INPUT)
    }

    @DisplayName("모든 값을 지정하지 않아도 @ColumnDefault 값이 들어가야 한다")
    @Test
    fun saveSubitem2() {
        // given
        val category = Category(MileageCategoryRepositoryTests.NAME)
        val savedCategory = categoryRepository.save(category)
        val item = Item(
            category = savedCategory,
            name = NAME
        )

        // when
        itemRepository.save(item)
        val savedItem = itemRepository.findById(item.id!!)

        // then
        Assertions.assertThat(savedItem).isNotNull
        Assertions.assertThat(savedItem.get()).isEqualTo(item)
        Assertions.assertThat(savedItem.get().category).isEqualTo(savedCategory)
        Assertions.assertThat(savedItem.get().name).isEqualTo(NAME)
        Assertions.assertThat(savedItem.get().description1).isNull()
        Assertions.assertThat(savedItem.get().description2).isNull()
        Assertions.assertThat(savedItem.get().stuType).isNull()
        Assertions.assertThat(savedItem.get().isVisible).isEqualTo(DEFAULT_IS_VISIBLE)
        Assertions.assertThat(savedItem.get().isPortfolio).isEqualTo(DEFAULT_IS_PORTFOLIO)
        Assertions.assertThat(savedItem.get().isStudentVisible).isEqualTo(DEFAULT_IS_STUDENT_VISIBLE)
        Assertions.assertThat(savedItem.get().isStudentInput).isEqualTo(DEFAULT_IS_STUDENT_INPUT)
    }

    @DisplayName("repository: 마일리지 항목 전체 조회")
    @Test
    fun getSubitems() {
        // given
        val category = Category(MileageCategoryRepositoryTests.NAME)
        val savedCategory = categoryRepository.save(category)
        val item1 = Item(
            category = savedCategory,
            name = NAME
        ).apply {
            description1 = DESCRIPTION1
            description2 = DESCRIPTION2
            stuType = STU_TYPE
            isVisible = IS_VISIBLE
            isPortfolio = IS_PORTFOLIO
            isStudentVisible = IS_STUDENT_VISIBLE
            isStudentInput = IS_STUDENT_INPUT
        }
        val item2 = Item(
            category = savedCategory,
            name = "전공 상담2"
        ).apply {
            description1 = DESCRIPTION1
            description2 = DESCRIPTION2
            stuType = STU_TYPE
            isVisible = IS_VISIBLE
            isPortfolio = IS_PORTFOLIO
            isStudentVisible = IS_STUDENT_VISIBLE
            isStudentInput = IS_STUDENT_INPUT
        }

        // when
        itemRepository.save(item1)
        itemRepository.save(item2)
        val items = itemRepository.findAll()

        // then
        Assertions.assertThat(items).isNotNull
        Assertions.assertThat(items).hasSize(2)
    }

    @DisplayName("repository: 마일리지 항목 삭제")
    @Test
    fun deleteSubitem() {
        // given
        val category = Category(MileageCategoryRepositoryTests.NAME)
        val savedCategory = categoryRepository.save(category)
        val item = Item(
            category = savedCategory,
            name = NAME
        ).apply {
            description1 = DESCRIPTION1
            description2 = DESCRIPTION2
            stuType = STU_TYPE
            isVisible = IS_VISIBLE
            isPortfolio = IS_PORTFOLIO
            isStudentVisible = IS_STUDENT_VISIBLE
            isStudentInput = IS_STUDENT_INPUT
        }

        // when
        itemRepository.save(item)
        itemRepository.deleteById(item.id!!)
        val itemOptional = itemRepository.findById(item.id!!)

        // then
        Assertions.assertThat(itemOptional).isEmpty
    }
}
