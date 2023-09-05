package edu.handong.cseemileage.mileage.semesterItem.service

import edu.handong.cseemileage.mileage.category.domain.Category
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests
import edu.handong.cseemileage.mileage.item.domain.Item
import edu.handong.cseemileage.mileage.item.repository.ItemRepository
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.NAME
import edu.handong.cseemileage.mileage.item.service.MileageItemServiceTests
import edu.handong.cseemileage.mileage.semesterItem.domain.SemesterItem
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemForm
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemMultipleForm
import edu.handong.cseemileage.mileage.semesterItem.exception.DuplicateSemesterItemException
import edu.handong.cseemileage.mileage.semesterItem.exception.SemesterNameNotFoundException
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepository
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepositoryTests.Companion.CATEGORY_MAX_POINTS
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepositoryTests.Companion.ITEM_MAX_POINTS
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepositoryTests.Companion.POINT_VALUE
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepositoryTests.Companion.SEMESTER_NAME
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepositoryTests.Companion.UPDATE_CATEGORY_MAX_POINTS
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepositoryTests.Companion.UPDATE_ITEM_MAX_POINTS
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepositoryTests.Companion.UPDATE_POINT_VALUE
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepositoryTests.Companion.UPDATE_SEMESTER_NAME
import edu.handong.cseemileage.student.domain.Student
import edu.handong.cseemileage.student.repository.StudentRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.test.annotation.DirtiesContext
import org.springframework.transaction.annotation.Transactional
import org.testng.Assert.assertThrows
import org.testng.Assert.fail
import javax.annotation.PostConstruct
import kotlin.system.measureTimeMillis

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@Transactional
class MileageSemesterItemServiceTestsItem @Autowired constructor(
    val categoryRepository: CategoryRepository,
    val itemRepository: ItemRepository,
    val semesterItemRepository: SemesterItemRepository,
    val semesterItemService: SemesterItemService,
    val studentRepository: StudentRepository
) {

    // TODO: 학기 수정 추가하기
    @DisplayName("service: 학기별 항목 수정")
    @Test
    fun mileageSemesterItemServiceTests_53() {
        // Given
        val map = prepareData()
        val item2 = map["item2"] as Item
        val category2 = map["category2"] as Category
        val semesterItem = map["semesterItem"] as SemesterItem

        val updateForm = SemesterItemForm(
            itemId = item2.id,
            points = UPDATE_POINT_VALUE,
            itemMaxPoints = UPDATE_ITEM_MAX_POINTS,
            categoryMaxPoints = UPDATE_CATEGORY_MAX_POINTS,
            semesterName = UPDATE_SEMESTER_NAME
        )

        // When
        semesterItemService.modifySemesterItem(semesterItem.id, updateForm)
        val updatedSemesterItem = semesterItemRepository.findById(semesterItem.id).get()

        // Then
        Assertions.assertThat(updatedSemesterItem).isNotNull
        Assertions.assertThat(updatedSemesterItem.category).isEqualTo(category2)
        Assertions.assertThat(updatedSemesterItem.item).isEqualTo(item2)
        Assertions.assertThat(updatedSemesterItem.pointValue).isEqualTo(UPDATE_POINT_VALUE)
        Assertions.assertThat(updatedSemesterItem.itemMaxPoints).isEqualTo(UPDATE_ITEM_MAX_POINTS)
        Assertions.assertThat(updatedSemesterItem.categoryMaxPoints).isEqualTo(UPDATE_CATEGORY_MAX_POINTS)
        Assertions.assertThat(updatedSemesterItem.semesterName).isEqualTo(UPDATE_SEMESTER_NAME)
    }

    @DisplayName("service: 학기별 항목 수정 - 존재하지 않는 값은 원래의 값")
    @Test
    fun mileageSemetserItemServiceTests_82() {
        // Given
        val map = prepareData()
        val semesterItem = map["semesterItem"] as SemesterItem

        val updateForm = SemesterItemForm(
            itemId = semesterItem.item.id,
            points = null,
            itemMaxPoints = null,
            categoryMaxPoints = null,
            semesterName = SEMESTER_NAME
        )

        // When
        semesterItemService.modifySemesterItem(semesterItem.id, updateForm)
        val updatedSemesterItem = semesterItemRepository.findById(semesterItem.id).get()

        // Then
        Assertions.assertThat(updatedSemesterItem).isNotNull
        Assertions.assertThat(updatedSemesterItem.category).isEqualTo(semesterItem.category)
        Assertions.assertThat(updatedSemesterItem.item).isEqualTo(semesterItem.item)
        Assertions.assertThat(updatedSemesterItem.pointValue).isEqualTo(POINT_VALUE)
        Assertions.assertThat(updatedSemesterItem.itemMaxPoints).isEqualTo(ITEM_MAX_POINTS)
        Assertions.assertThat(updatedSemesterItem.categoryMaxPoints).isEqualTo(CATEGORY_MAX_POINTS)
        Assertions.assertThat(updatedSemesterItem.semesterName).isEqualTo(SEMESTER_NAME)
    }

    @DisplayName("학기별 항목 수정 시 semesterName을 보내지 않으면 exception")
    @Test
    fun mileageSemetserItemServiceTests_111() {
        // Given
        val map = prepareData()
        val item2 = map["item2"] as Item
        val semesterItem = map["semesterItem"] as SemesterItem

        val updateForm = SemesterItemForm(
            itemId = item2.id,
            points = null,
            itemMaxPoints = null,
            categoryMaxPoints = null,
            semesterName = null
        )

        // When
        assertThrows(SemesterNameNotFoundException::class.java) {
            semesterItemService.modifySemesterItem(semesterItem.id, updateForm)
        }
    }

    @DisplayName("학기별 항목 수정 시 itemId-semesterName 조합 중복 불가")
    @Test
    fun mileageSemetserItemServiceTests_136() {
        // Given
        val map = prepareData()
        val item2 = map["item2"] as Item
        val semesterItem = map["semesterItem"] as SemesterItem

        // semesterItem2와 중복
        val updateForm = SemesterItemForm(
            itemId = item2.id,
            points = null,
            itemMaxPoints = null,
            categoryMaxPoints = null,
            semesterName = semesterItem.semesterName
        )

        // When
        assertThrows(DuplicateSemesterItemException::class.java) {
            semesterItemService.modifySemesterItem(semesterItem.id, updateForm)
        }
    }

    @DisplayName("학기별 항목 수정 시 itemId-semesterName 변경 안 할 경우 중복 검사 안함")
    @Test
    fun mileageSemetserItemServiceTests_160() {
        // Given
        val map = prepareData()
        val semesterItem = map["semesterItem"] as SemesterItem

        val updateForm = SemesterItemForm(
            itemId = semesterItem.item.id,
            points = null,
            itemMaxPoints = null,
            categoryMaxPoints = null,
            semesterName = semesterItem.semesterName
        )

        // When
        try {
            semesterItemService.modifySemesterItem(semesterItem.id, updateForm)
        } catch (e: DuplicateSemesterItemException) {
            e.printStackTrace()
            fail("An exception is " + e.message)
        }
    }

    @PostConstruct
    @Profile("dev")
    fun init() {
        var category = Category("전공 마일리지")
        categoryRepository.save(category)

        var subitem = Item(category, "전공 항목1")
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
        val form = SemesterItemForm(
            itemId = itemId,
            points = POINT_VALUE,
            itemMaxPoints = ITEM_MAX_POINTS,
            categoryMaxPoints = CATEGORY_MAX_POINTS,
            semesterName = null
        )

        // When
        semesterItemService.saveSemesterItem(form, SEMESTER_NAME)
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
                        itemId = it,
                        points = POINT_VALUE,
                        itemMaxPoints = ITEM_MAX_POINTS,
                        categoryMaxPoints = CATEGORY_MAX_POINTS,
                        semesterName = null
                    )
                )
        }
        val form = SemesterItemMultipleForm(semesterList)

        // When
        val elapsed1: Long = measureTimeMillis {
            semesterItemService.saveSemesterItemMultipleBulkInsert(form, SEMESTER_NAME)
        }
        val elapsed2: Long = measureTimeMillis {
            semesterItemService.saveSemesterItemMultiple(form, SEMESTER_NAME)
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

    fun createDefaultSemesterItem(item: Item): SemesterItem {
        return SemesterItem(
            category = item.category,
            item = item
        ).apply {
            pointValue = POINT_VALUE
            itemMaxPoints = ITEM_MAX_POINTS
            categoryMaxPoints = CATEGORY_MAX_POINTS
            semesterName = SEMESTER_NAME
        }
    }

    fun prepareData(): Map<String, Any> {
        val category1 = Category(MileageCategoryRepositoryTests.NAME)
        val category2 = Category("category2")
        categoryRepository.save(category1)
        categoryRepository.save(category2)
        val item1 = MileageItemServiceTests.createDefaultItem(category1, NAME)
        itemRepository.save(item1)
        val item2 = MileageItemServiceTests.createDefaultItem(category2, NAME)
        itemRepository.save(item2)
        val semesterItem = createDefaultSemesterItem(item1)
        semesterItemRepository.save(semesterItem)
        val semesterItem2 = createDefaultSemesterItem(item2)
        semesterItemRepository.save(semesterItem2)
        val student = Student()
        studentRepository.save(student)

        return mapOf(
            "category1" to category1,
            "category2" to category2,
            "item1" to item1,
            "item2" to item2,
            "semesterItem" to semesterItem,
            "student" to student
        )
    }
}
