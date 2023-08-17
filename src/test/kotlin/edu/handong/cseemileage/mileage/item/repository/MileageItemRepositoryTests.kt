package edu.handong.cseemileage.mileage.item.repository

import edu.handong.cseemileage.mileage.category.domain.Category
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.domain.Item
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MileageItemRepositoryTests @Autowired constructor(
    private val itemRepository: ItemRepository,
    private val categoryRepository: CategoryRepository
) {
    @DisplayName("repository: 마일리지 항목 persist")
    @Test
    fun saveSubitem() {
        // given
        val category = Category("전공 상담", "교수님과 전공 상담 진행", 20)
        val savedCategory = categoryRepository.save(category)
        val item = Item(savedCategory, "전공 상담 세부 항목", 0, "description1", "description2", "2020-01", "R")

        // when
        itemRepository.save(item)
        val savedSubitem = itemRepository.findById(item.id!!)

        // then
        Assertions.assertThat(savedSubitem).isNotNull
        Assertions.assertThat(savedSubitem.get()).isEqualTo(item)
    }

    @DisplayName("repository: 마일리지 항목 전체 조회")
    @Test
    fun getSubitems() {
        // given
        val category = Category("전공 상담", "교수님과 전공 상담 진행", 20)
        val savedCategory = categoryRepository.save(category)
        val item1 =
            Item(savedCategory, "전공 상담 세부 항목1", 0, "description1", "description2", "2020-01", "R")
        val item2 =
            Item(savedCategory, "전공 상담 세부 항목2", 0, "description1", "description2", "2020-01", "R")

        // when
        itemRepository.save(item1)
        itemRepository.save(item2)
        val subitems = itemRepository.findAll()

        // then
        Assertions.assertThat(subitems).isNotNull
    }

    @DisplayName("repository: 마일리지 항목 삭제")
    @Test
    fun deleteSubitem() {
        // given
        val category = Category("전공 상담", "교수님과 전공 상담 진행", 20)
        val savedCategory = categoryRepository.save(category)
        val item = Item(savedCategory, "전공 상담 세부 항목", 0, "description1", "description2", "2020-01", "R")

        // when
        itemRepository.save(item)
        itemRepository.deleteById(item.id!!)
        val subitemOptional = itemRepository.findById(item.id!!)

        // then
        Assertions.assertThat(subitemOptional.isEmpty).isTrue
    }
}
