package edu.handong.cseemileage.mileage.semester.service

import edu.handong.cseemileage.mileage.category.domain.Category
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.domain.Item
import edu.handong.cseemileage.mileage.item.repository.ItemRepository
import edu.handong.cseemileage.mileage.semester.dto.SemesterForm
import edu.handong.cseemileage.mileage.semester.dto.SemesterMultipleForm
import edu.handong.cseemileage.mileage.semester.repository.SemesterRepository
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
class MileageSemesterServiceTests @Autowired constructor(
    val categoryRepository: CategoryRepository,
    val itemRepository: ItemRepository,
    val semesterRepository: SemesterRepository,
    val semesterService: SemesterService
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
        val form = SemesterForm(itemId, WEIGHT, MAX_POINTS, SEMESTER_NAME)

        // When
        semesterService.saveSemester(form)
        val id = semesterRepository.findTopByOrderByIdDesc()?.id ?: 0

        // Then
        semesterRepository.findById(id).ifPresent {
            Assertions.assertThat(it.item.id).isEqualTo(itemId)
        }
    }

    @DisplayName("integration: bulk insert 성능 테스트 - 학기별 마일리지 항목 생성(multiple)")
    @Test
    @Profile("dev")
    fun createSemesterMultipleBulkInsert() {
        // Given
        val item1 = itemRepository.findAllByName("전공 항목1").get(0)
        val semesterList = mutableListOf<SemesterForm>()
        item1.id?.let {
            for (i: Int in 1..1000)
                semesterList.add(
                    SemesterForm(
                        it,
                        WEIGHT,
                        MAX_POINTS,
                        SEMESTER_NAME
                    )
                )
        }
        val form = SemesterMultipleForm(semesterList)

        // When
        val elapsed1: Long = measureTimeMillis {
            semesterService.saveSemesterMultipleBulkInsert(form)
        }
        val elapsed2: Long = measureTimeMillis {
            semesterService.saveSemesterMultiple(form)
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
