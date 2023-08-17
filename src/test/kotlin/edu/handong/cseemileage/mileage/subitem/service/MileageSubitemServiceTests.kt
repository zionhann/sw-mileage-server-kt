package edu.handong.cseemileage.mileage.subitem.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class MileageSubitemServiceTests @Autowired constructor(
    val subitemService: SubitemService
) {
    @DisplayName("service: 마일리지 서브아이템 save")
    @Test
    fun saveSubitem() {
    }

    @Test
    fun modifySubitem() {
    }

    @Test
    fun deleteSubitem() {
    }

    @DisplayName("service: repository 의존성 주입")
    @Test
    fun getRepository() {
        assertThat(subitemService.repository).isNotNull
    }

    @DisplayName("service: category repository 의존성 주입")
    @Test
    fun getCategoryRepsoitory() {
        assertThat(subitemService.categoryRepsoitory).isNotNull
    }
}
