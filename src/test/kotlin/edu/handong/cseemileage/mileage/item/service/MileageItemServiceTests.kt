package edu.handong.cseemileage.mileage.item.service

import edu.handong.cseemileage.mileage.category.domain.Category
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests
import edu.handong.cseemileage.mileage.item.domain.Item
import edu.handong.cseemileage.mileage.item.dto.ItemForm
import edu.handong.cseemileage.mileage.item.repository.ItemRepository
import edu.handong.cseemileage.mileage.item.repository.MileageItemRepositoryTests.Companion.DESCRIPTION1
import edu.handong.cseemileage.mileage.item.repository.MileageItemRepositoryTests.Companion.DESCRIPTION2
import edu.handong.cseemileage.mileage.item.repository.MileageItemRepositoryTests.Companion.IS_MULTI
import edu.handong.cseemileage.mileage.item.repository.MileageItemRepositoryTests.Companion.IS_PORTFOLIO
import edu.handong.cseemileage.mileage.item.repository.MileageItemRepositoryTests.Companion.IS_STUDENT_INPUT
import edu.handong.cseemileage.mileage.item.repository.MileageItemRepositoryTests.Companion.IS_STUDENT_VISIBLE
import edu.handong.cseemileage.mileage.item.repository.MileageItemRepositoryTests.Companion.IS_VISIBLE
import edu.handong.cseemileage.mileage.item.repository.MileageItemRepositoryTests.Companion.NAME
import edu.handong.cseemileage.mileage.item.repository.MileageItemRepositoryTests.Companion.STU_TYPE
import edu.handong.cseemileage.mileage.item.repository.MileageItemRepositoryTests.Companion.UPDATE_DESCRIPTION1
import edu.handong.cseemileage.mileage.item.repository.MileageItemRepositoryTests.Companion.UPDATE_DESCRIPTION2
import edu.handong.cseemileage.mileage.item.repository.MileageItemRepositoryTests.Companion.UPDATE_IS_MULTI
import edu.handong.cseemileage.mileage.item.repository.MileageItemRepositoryTests.Companion.UPDATE_IS_PORTFOLIO
import edu.handong.cseemileage.mileage.item.repository.MileageItemRepositoryTests.Companion.UPDATE_IS_STUDENT_INPUT
import edu.handong.cseemileage.mileage.item.repository.MileageItemRepositoryTests.Companion.UPDATE_IS_STUDENT_VISIBLE
import edu.handong.cseemileage.mileage.item.repository.MileageItemRepositoryTests.Companion.UPDATE_IS_VISIBLE
import edu.handong.cseemileage.mileage.item.repository.MileageItemRepositoryTests.Companion.UPDATE_NAME
import edu.handong.cseemileage.mileage.item.repository.MileageItemRepositoryTests.Companion.UPDATE_STU_TYPE
import edu.handong.cseemileage.utils.Utils.Companion.stringToBoolean
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class MileageItemServiceTests @Autowired constructor(
    val itemService: ItemService,
    val itemRepository: ItemRepository,
    val categoryRepository: CategoryRepository
) {

    @DisplayName("service: 마일리지 항목 수정")
    @Test
    fun mileageItemServiceTests_21() {
        // Given
        val category1 = Category(MileageCategoryRepositoryTests.NAME)
        val category2 = Category("update")
        categoryRepository.save(category1)
        categoryRepository.save(category2)
        val item = createDefaultItem(category1)
        val updateForm = ItemForm(
            categoryId = category2.id,
            itemName = UPDATE_NAME,
            description1 = UPDATE_DESCRIPTION1,
            description2 = UPDATE_DESCRIPTION2,
            stuType = UPDATE_STU_TYPE,
            flags = ItemForm.Flag(
                isPortfolio = UPDATE_IS_PORTFOLIO,
                isVisible = stringToBoolean(UPDATE_IS_VISIBLE),
                isStudentVisible = stringToBoolean(UPDATE_IS_STUDENT_VISIBLE),
                isStudentEditable = stringToBoolean(UPDATE_IS_STUDENT_INPUT),
                isMultiple = stringToBoolean(UPDATE_IS_MULTI)
            )
        )
        itemRepository.save(item)

        // When
        itemService.modifyItem(item.id, updateForm)
        val updatedItem = itemRepository.findById(item.id).get()

        // Then
        assertThat(updatedItem.category).isEqualTo(category2)
        assertThat(updatedItem.name).isEqualTo(UPDATE_NAME)
        assertThat(updatedItem.description1).isEqualTo(UPDATE_DESCRIPTION1)
        assertThat(updatedItem.description2).isEqualTo(UPDATE_DESCRIPTION2)
        assertThat(updatedItem.stuType).isEqualTo(UPDATE_STU_TYPE)
        assertThat(updatedItem.isPortfolio).isEqualTo(UPDATE_IS_PORTFOLIO)
        assertThat(updatedItem.isVisible).isEqualTo(UPDATE_IS_VISIBLE)
        assertThat(updatedItem.isStudentVisible).isEqualTo(UPDATE_IS_STUDENT_VISIBLE)
        assertThat(updatedItem.isStudentInput).isEqualTo(UPDATE_IS_STUDENT_INPUT)
        assertThat(updatedItem.isMulti).isEqualTo(UPDATE_IS_MULTI)
    }

    @DisplayName("service: 마일리지 항목 수정 - 존재하지 않는 값은 원래의 값")
    @Test
    fun mileageItemServiceTests_56() {
        // Given
        val category1 = Category(MileageCategoryRepositoryTests.NAME)
        val category2 = Category("update")
        categoryRepository.save(category1)
        categoryRepository.save(category2)
        val item = createDefaultItem(category1)
        val updateForm = ItemForm(
            categoryId = category2.id,
            itemName = UPDATE_NAME,
            description1 = null,
            description2 = null,
            stuType = null,
            flags = null
        )
        itemRepository.save(item)

        // When
        itemService.modifyItem(item.id, updateForm)
        val updatedItem = itemRepository.findById(item.id).get()

        // Then
        assertThat(updatedItem.category).isEqualTo(category2)
        assertThat(updatedItem.name).isEqualTo(UPDATE_NAME)
        assertThat(updatedItem.description1).isEqualTo(DESCRIPTION1)
        assertThat(updatedItem.description2).isEqualTo(DESCRIPTION2)
        assertThat(updatedItem.stuType).isEqualTo(STU_TYPE)
        assertThat(updatedItem.isPortfolio).isEqualTo(IS_PORTFOLIO)
        assertThat(updatedItem.isVisible).isEqualTo(IS_VISIBLE)
        assertThat(updatedItem.isStudentVisible).isEqualTo(IS_STUDENT_VISIBLE)
        assertThat(updatedItem.isStudentInput).isEqualTo(IS_STUDENT_INPUT)
        assertThat(updatedItem.isMulti).isEqualTo(IS_MULTI)
    }

    @DisplayName("service: repository 의존성 주입")
    @Test
    fun getRepository() {
        assertThat(itemService.repository).isNotNull
    }

    @DisplayName("service: category repository 의존성 주입")
    @Test
    fun getCategoryRepsoitory() {
        assertThat(itemService.categoryRepository).isNotNull
    }

    companion object {
        fun createDefaultItem(category: Category): Item {
            return Item(
                category = category,
                name = NAME
            ).apply {
                description1 = DESCRIPTION1
                description2 = DESCRIPTION2
                stuType = STU_TYPE
                isPortfolio = IS_PORTFOLIO
                isVisible = IS_VISIBLE
                isStudentVisible = IS_STUDENT_VISIBLE
                isStudentInput = IS_STUDENT_INPUT
                isMulti = IS_MULTI
            }
        }
    }
}
