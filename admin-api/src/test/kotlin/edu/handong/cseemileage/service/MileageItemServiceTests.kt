package edu.handong.cseemileage.service

import edu.handong.cseemileage.controller.MileageCategoryIntegrationTests.Companion.NAME
import edu.handong.cseemileage.controller.MileageItemIntegrationTests.Companion.DESCRIPTION1
import edu.handong.cseemileage.controller.MileageItemIntegrationTests.Companion.DESCRIPTION2
import edu.handong.cseemileage.controller.MileageItemIntegrationTests.Companion.IS_PORTFOLIO
import edu.handong.cseemileage.controller.MileageItemIntegrationTests.Companion.IS_STUDENT_INPUT
import edu.handong.cseemileage.controller.MileageItemIntegrationTests.Companion.IS_STUDENT_VISIBLE
import edu.handong.cseemileage.controller.MileageItemIntegrationTests.Companion.IS_VISIBLE
import edu.handong.cseemileage.controller.MileageItemIntegrationTests.Companion.STU_TYPE
import edu.handong.cseemileage.controller.MileageItemIntegrationTests.Companion.UPDATE_DESCRIPTION1
import edu.handong.cseemileage.controller.MileageItemIntegrationTests.Companion.UPDATE_DESCRIPTION2
import edu.handong.cseemileage.controller.MileageItemIntegrationTests.Companion.UPDATE_IS_PORTFOLIO
import edu.handong.cseemileage.controller.MileageItemIntegrationTests.Companion.UPDATE_IS_STUDENT_INPUT
import edu.handong.cseemileage.controller.MileageItemIntegrationTests.Companion.UPDATE_IS_STUDENT_VISIBLE
import edu.handong.cseemileage.controller.MileageItemIntegrationTests.Companion.UPDATE_IS_VISIBLE
import edu.handong.cseemileage.controller.MileageItemIntegrationTests.Companion.UPDATE_NAME
import edu.handong.cseemileage.controller.MileageItemIntegrationTests.Companion.UPDATE_STU_TYPE
import edu.handong.cseemileage.domain.mileage.Category
import edu.handong.cseemileage.domain.mileage.Item
import edu.handong.cseemileage.dto.mileage.item.ItemForm
import edu.handong.cseemileage.exception.mileage.item.DuplicateItemException
import edu.handong.cseemileage.repository.mileage.CategoryRepository
import edu.handong.cseemileage.repository.mileage.ItemRepository
import edu.handong.cseemileage.service.item.ItemService
import edu.handong.cseemileage.utils.Utils.Companion.stringToBoolean
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import org.testng.Assert.assertThrows

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
        val category1 = Category(NAME)
        val category2 = Category("update")
        categoryRepository.save(category1)
        categoryRepository.save(category2)
        val item = createDefaultItem(category1, NAME)
        val updateForm = ItemForm(
            categoryId = category2.id,
            itemName = UPDATE_NAME,
            description1 = UPDATE_DESCRIPTION1,
            description2 = UPDATE_DESCRIPTION2,
            stuType = UPDATE_STU_TYPE,
            itemMaxPoints = 5f,
            flags = ItemForm.Flag(
                isPortfolio = UPDATE_IS_PORTFOLIO,
                isVisible = stringToBoolean(UPDATE_IS_VISIBLE),
                isStudentVisible = stringToBoolean(UPDATE_IS_STUDENT_VISIBLE),
                isStudentEditable = stringToBoolean(UPDATE_IS_STUDENT_INPUT)
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
    }

    @DisplayName("service: 마일리지 항목 수정 - 존재하지 않는 값은 원래의 값")
    @Test
    fun mileageItemServiceTests_56() {
        // Given
        val category1 = Category(NAME)
        val category2 = Category("update")
        categoryRepository.save(category1)
        categoryRepository.save(category2)
        val item = createDefaultItem(category1, NAME)
        val updateForm = ItemForm(
            categoryId = category2.id,
            itemName = UPDATE_NAME,
            description1 = null,
            description2 = null,
            stuType = null,
            itemMaxPoints = null,
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
    }

    @DisplayName("수정 시 중복된 이름이 있으면 예외 발생")
    @Test
    fun mileageItemServiceTests_123() {
        // Given
        val category = Category(NAME)
        categoryRepository.save(category)
        val item = createDefaultItem(category, NAME)
        val item2 = createDefaultItem(category, "test")
        itemRepository.save(item)
        itemRepository.save(item2)

        val updateForm = ItemForm(
            categoryId = category.id,
            itemName = "test",
            description1 = null,
            description2 = null,
            stuType = null,
            itemMaxPoints = null,
            flags = null
        )

        // Then
        assertThrows(DuplicateItemException::class.java) {
            // When
            itemService.modifyItem(item.id, updateForm)
        }
    }

    @DisplayName("수정 시 이름을 수정하지 않으면 예외 발생 하지 않음")
    @Test
    fun mileageItemServiceTests_152() {
        // Given
        val category = Category(NAME)
        categoryRepository.save(category)
        val item = createDefaultItem(category, NAME)
        itemRepository.save(item)

        val updateForm = ItemForm(
            categoryId = category.id,
            itemName = NAME,
            description1 = null,
            description2 = null,
            stuType = null,
            itemMaxPoints = null,
            flags = null
        )

        // Then
        try {
            itemService.modifyItem(item.id, updateForm)
        } catch (e: DuplicateItemException) {
            e.printStackTrace()
            fail("An exception is" + e.message)
        }
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
        fun createDefaultItem(category: Category, name: String): Item {
            return Item(
                category = category,
                name = name
            ).apply {
                description1 = DESCRIPTION1
                description2 = DESCRIPTION2
                stuType = STU_TYPE
                isPortfolio = IS_PORTFOLIO
                isVisible = IS_VISIBLE
                isStudentVisible = IS_STUDENT_VISIBLE
                isStudentInput = IS_STUDENT_INPUT
            }
        }
    }
}
