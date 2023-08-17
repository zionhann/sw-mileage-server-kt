package edu.handong.cseemileage.mileage.subitem.repository

import edu.handong.cseemileage.mileage.category.domain.Category
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.subitem.domain.Subitem
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MileageSubitemRepositoryTests @Autowired constructor(
    private val subitemRepository: SubitemRepository,
    private val categoryRepository: CategoryRepository
) {
    @DisplayName("repository: 마일리지 항목 persist")
    @Test
    fun saveSubitem() {
        // given
        val category = Category("전공 상담", "교수님과 전공 상담 진행", 20)
        val savedCategory = categoryRepository.save(category)
        val subitem = Subitem(savedCategory, "전공 상담 세부 항목", 20f, 0, 20f, "description1", "description2", "2020-01", "R")

        // when
        subitemRepository.save(subitem)
        val savedSubitem = subitemRepository.findById(subitem.id!!)

        // then
        Assertions.assertThat(savedSubitem).isNotNull
        Assertions.assertThat(savedSubitem.get()).isEqualTo(subitem)
    }

    @DisplayName("repository: 마일리지 항목 전체 조회")
    @Test
    fun getSubitems() {
        // given
        val category = Category("전공 상담", "교수님과 전공 상담 진행", 20)
        val savedCategory = categoryRepository.save(category)
        val subitem1 = Subitem(savedCategory, "전공 상담 세부 항목1", 20f, 0, 20f, "description1", "description2", "2020-01", "R")
        val subitem2 = Subitem(savedCategory, "전공 상담 세부 항목2", 20f, 0, 20f, "description1", "description2", "2020-01", "R")

        // when
        subitemRepository.save(subitem1)
        subitemRepository.save(subitem2)
        val subitems = subitemRepository.findAll()

        // then
        Assertions.assertThat(subitems).isNotNull
    }

    @DisplayName("repository: 마일리지 항목 삭제")
    @Test
    fun deleteSubitem() {
        // given
        val category = Category("전공 상담", "교수님과 전공 상담 진행", 20)
        val savedCategory = categoryRepository.save(category)
        val subitem = Subitem(savedCategory, "전공 상담 세부 항목", 20f, 0, 20f, "description1", "description2", "2020-01", "R")

        // when
        subitemRepository.save(subitem)
        subitemRepository.deleteById(subitem.id!!)
        val subitemOptional = subitemRepository.findById(subitem.id!!)

        // then
        Assertions.assertThat(subitemOptional.isEmpty).isTrue
    }
}
