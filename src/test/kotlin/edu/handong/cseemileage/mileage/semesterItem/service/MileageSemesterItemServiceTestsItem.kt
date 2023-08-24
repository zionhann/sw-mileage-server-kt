package edu.handong.cseemileage.mileage.semesterItem.service

import edu.handong.cseemileage.mileage.category.domain.Category
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.domain.Item
import edu.handong.cseemileage.mileage.item.repository.ItemRepository
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemForm
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemMultipleForm
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.test.annotation.DirtiesContext
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct
import kotlin.system.measureTimeMillis

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@Transactional
class MileageSemesterItemServiceTestsItem @Autowired constructor(
    val categoryRepository: CategoryRepository,
    val itemRepository: ItemRepository,
    val semesterItemRepository: SemesterItemRepository,
    val semesterItemService: SemesterItemService
) {

    companion object {
        private const val SEMESTER_NAME = "2023-02"
        private const val WEIGHT = 3f
        private const val MAX_POINTS = 15f
        private const val API_URI = "/api/mileage/semesters"
    }

    @PostConstruct
    @Profile("dev")
    fun init() {
        var category = Category("전공 마일리지", "-", 20)
        categoryRepository.save(category)

        var subitem = Item(category, "전공 항목1", 0, "설명1", "설명2", "R")
        itemRepository.save(subitem)
    }

    /**
     * controller 단에서 save 테스트 완료
     * item lazy loading 후 연관관계 테스트
     * */
    @DisplayName("service: 학기별 마일리지 항목 생성")
    @Test
    fun saveSemester() {
        // Given
        val itemId = itemRepository.findTopByOrderByIdDesc()?.id ?: 0
        val form = SemesterItemForm(itemId, WEIGHT, MAX_POINTS, SEMESTER_NAME)

        // When
        semesterItemService.saveSemesterItem(form)
        val id = semesterItemRepository.findTopByOrderByIdDesc()?.id ?: 0

        // Then
        semesterItemRepository.findById(id).ifPresent {
            Assertions.assertThat(it.item.id).isEqualTo(itemId)
        }
    }

    // 검증 완료. 시간이 오래 걸리는 관계로 @Test 주석 처리
    // @Test
    @DisplayName("integration: bulk insert 성능 테스트 - 학기별 마일리지 항목 생성(multiple)")
    fun createSemesterMultipleBulkInsert() {
        // Given
        val item1 = itemRepository.findAllByName("전공 항목1").get(0)
        val semesterList = mutableListOf<SemesterItemForm>()
        item1.id?.let {
            for (i: Int in 1..1000)
                semesterList.add(
                    SemesterItemForm(
                        it,
                        WEIGHT,
                        MAX_POINTS,
                        SEMESTER_NAME
                    )
                )
        }
        val form = SemesterItemMultipleForm(semesterList)

        // When
        val elapsed1: Long = measureTimeMillis {
            semesterItemService.saveSemesterItemMultipleBulkInsert(form)
        }
        val elapsed2: Long = measureTimeMillis {
            semesterItemService.saveSemesterItemMultiple(form)
        }
        println("Bulk insert 경과 시간: $elapsed1 ms")
        println("None Bulk insert 경과 시간: $elapsed2 ms")

        /*
        * 출력 예시
        * Bulk insert 경과 시간: 121 ms
        * None Bulk insert 경과 시간: 3746 ms
        * */

        // Then
        Assertions.assertThat(elapsed1).isLessThan(elapsed2)
    }
}
