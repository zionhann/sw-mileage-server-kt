package edu.handong.cseemileage.mileage.semesterItem.repository

import edu.handong.cseemileage.mileage.category.domain.Category
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests
import edu.handong.cseemileage.mileage.item.domain.Item
import edu.handong.cseemileage.mileage.item.repository.ItemRepository
import edu.handong.cseemileage.mileage.item.repository.MileageItemRepositoryTests
import edu.handong.cseemileage.mileage.semesterItem.domain.SemesterItem
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
class SemesterItemRepositoryTests @Autowired constructor(
    private val semesterItemRepository: SemesterItemRepository,
    private val categoryRepository: CategoryRepository,
    private val itemRepository: ItemRepository
) {

    companion object {
        const val SEMESTER_NAME = "2023-01"
        const val POINT_VALUE = 5f
        const val ITEM_MAX_POINTS = 10f
        const val CATEGORY_MAX_POINTS = 15f

        const val UPDATE_SEMESTER_NAME = "2024-02"
        const val UPDATE_POINT_VALUE = 2f
        const val UPDATE_ITEM_MAX_POINTS = 6f
        const val UPDATE_CATEGORY_MAX_POINTS = 12f

        const val DEFAULT_SEMESTER_NAME = "2023-02"
        const val DEFAULT_POINT_VALUE = 0f
        const val DEFAULT_ITEM_MAX_POINTS = 0f
        const val DEFAULT_CATEGORY_MAX_POINTS = 0f
    }

    @DisplayName("repository: 학기별 항목 persist")
    @Test
    fun saveSemesterItem1() {
        // given
        val map = createCategoryAndItem()
        val savedCategory = map["category"] as Category
        val savedItem = map["item"] as Item

        val semesterItem = createSemesterItem(savedItem, savedCategory, SEMESTER_NAME)

        // when
        semesterItemRepository.save(semesterItem)
        val savedSemesterItem = semesterItemRepository.findById(semesterItem.id!!)

        // then
        Assertions.assertThat(savedSemesterItem).isNotNull
        Assertions.assertThat(savedSemesterItem.get()).isEqualTo(semesterItem)
        Assertions.assertThat(savedSemesterItem.get().item).isEqualTo(savedItem)
        Assertions.assertThat(savedSemesterItem.get().category).isEqualTo(savedCategory)
        Assertions.assertThat(savedSemesterItem.get().semesterName).isEqualTo(SEMESTER_NAME)
        Assertions.assertThat(savedSemesterItem.get().pointValue).isEqualTo(POINT_VALUE)
        Assertions.assertThat(savedSemesterItem.get().itemMaxPoints).isEqualTo(ITEM_MAX_POINTS)
    }

    @DisplayName("모든 값을 지정하지 않아도 @ColumnDefault 값이 들어가야 한다")
    @Test
    fun saveSemesterItem2() {
        // given
        val map = createCategoryAndItem()
        val savedCategory = map["category"] as Category
        val savedItem = map["item"] as Item

        val semesterItem = SemesterItem(
            item = savedItem,
            category = savedCategory
        )

        // when
        semesterItemRepository.save(semesterItem)
        val savedSemesterItem = semesterItemRepository.findById(semesterItem.id!!)

        // then
        Assertions.assertThat(savedSemesterItem).isNotNull
        Assertions.assertThat(savedSemesterItem.get()).isEqualTo(semesterItem)
        Assertions.assertThat(savedSemesterItem.get().item).isEqualTo(savedItem)
        Assertions.assertThat(savedSemesterItem.get().category).isEqualTo(savedCategory)
        Assertions.assertThat(savedSemesterItem.get().semesterName).isEqualTo(DEFAULT_SEMESTER_NAME)
        Assertions.assertThat(savedSemesterItem.get().pointValue).isEqualTo(DEFAULT_POINT_VALUE)
        Assertions.assertThat(savedSemesterItem.get().itemMaxPoints).isEqualTo(DEFAULT_ITEM_MAX_POINTS)
    }

    @DisplayName("repository: 마일리지 항목 전체 조회")
    @Test
    fun getSubitems() {
        // given
        val map = createCategoryAndItem()
        val savedCategory = map["category"] as Category
        val savedItem = map["item"] as Item

        val semesterItem1 = createSemesterItem(savedItem, savedCategory, SEMESTER_NAME)
        val semesterItem2 = createSemesterItem(savedItem, savedCategory, "2019-01")

        // when
        semesterItemRepository.save(semesterItem1)
        semesterItemRepository.save(semesterItem2)
        val semesterItems = semesterItemRepository.findAll()

        // then
        Assertions.assertThat(semesterItems).isNotNull
        Assertions.assertThat(semesterItems).hasSize(2)
    }

    @DisplayName("repository: 학기별 항목 삭제")
    @Test
    fun deleteSemesterItem() {
        // given
        val map = createCategoryAndItem()
        val savedCategory = map["category"] as Category
        val savedItem = map["item"] as Item

        val semesterItem = createSemesterItem(savedItem, savedCategory, SEMESTER_NAME)

        // when
        semesterItemRepository.save(semesterItem)
        semesterItemRepository.deleteById(semesterItem.id!!)
        val semesterItemOptional = semesterItemRepository.findById(semesterItem.id!!)

        // then
        Assertions.assertThat(semesterItemOptional).isEmpty
    }

    fun createSemesterItem(
        savedItem: Item,
        savedCategory: Category,
        semester: String
    ): SemesterItem {
        return SemesterItem(
            item = savedItem,
            category = savedCategory
        ).apply {
            semesterName = semester
            pointValue = POINT_VALUE
            itemMaxPoints = ITEM_MAX_POINTS
        }
    }

    fun createCategoryAndItem(): Map<String, Any> {
        val category = Category(MileageCategoryRepositoryTests.NAME)
        val savedCategory = categoryRepository.save(category)
        val item = Item(
            category = savedCategory,
            name = MileageItemRepositoryTests.NAME
        ).apply {
            description1 = MileageItemRepositoryTests.DESCRIPTION1
            description2 = MileageItemRepositoryTests.DESCRIPTION2
            stuType = MileageItemRepositoryTests.STU_TYPE
            isVisible = MileageItemRepositoryTests.IS_VISIBLE
            isPortfolio = MileageItemRepositoryTests.IS_PORTFOLIO
            isStudentVisible = MileageItemRepositoryTests.IS_STUDENT_VISIBLE
            isStudentInput = MileageItemRepositoryTests.IS_STUDENT_INPUT
            isMulti = MileageItemRepositoryTests.IS_MULTI
            itemMaxPoints = 20f
        }
        val savedItem = itemRepository.save(item)
        return mapOf(
            "category" to savedCategory,
            "item" to savedItem
        )
    }
}
