package edu.handong.cseemileage.mileage.mileageRecord.repository

import edu.handong.cseemileage.mileage.category.domain.Category
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests
import edu.handong.cseemileage.mileage.item.domain.Item
import edu.handong.cseemileage.mileage.item.repository.ItemRepository
import edu.handong.cseemileage.mileage.item.repository.MileageItemRepositoryTests
import edu.handong.cseemileage.mileage.mileageRecord.domain.MileageRecord
import edu.handong.cseemileage.mileage.semesterItem.domain.SemesterItem
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepository
import edu.handong.cseemileage.student.domain.Student
import edu.handong.cseemileage.student.repository.StudentRepository
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
class MileageRecordRepositoryTests @Autowired constructor(
    private val mileageRecordRepository: MileageRecordRepository,
    private val categoryRepository: CategoryRepository,
    private val itemRepository: ItemRepository,
    private val semesterItemRepository: SemesterItemRepository,
    private val studentRepository: StudentRepository
) {

    companion object {
        const val COUNTS = 4f
        const val EXTRA_POINTS = 8
        const val DESCRIPTION1 = "설명1"
        const val DESCRIPTION2 = "설명2"

        const val UPDATE_COUNTS = 2f
        const val UPDATE_POINTS = 8
        const val UPDATE_EXTRA_POINTS = 4
        const val UPDATE_DESCRIPTION1 = "설명1 수정"
        const val UPDATE_DESCRIPTION2 = "설명2 수정"

        const val DEFAULT_COUNTS = 1f
    }

    @DisplayName("repository: 마일리지 등록 persist")
    @Test
    fun saveMileageRecord() {
        // given
        val map = prepareDate()
        val mileageRecord = MileageRecord(
            semesterItem = map["semesterItem"] as SemesterItem,
            student = map["student"] as Student
        ).apply {
            counts = COUNTS
            extraPoints = EXTRA_POINTS
            description1 = DESCRIPTION1
            description2 = DESCRIPTION2
        }

        // when
        mileageRecordRepository.save(mileageRecord)
        val saved = mileageRecordRepository.findById(mileageRecord.id!!)

        // then
        Assertions.assertThat(saved).isNotNull
        Assertions.assertThat(saved.get()).isEqualTo(mileageRecord)
        Assertions.assertThat(saved.get().semesterItem).isEqualTo(map["semesterItem"] as SemesterItem)
        Assertions.assertThat(saved.get().student).isEqualTo(map["student"] as Student)
        Assertions.assertThat(saved.get().semesterItem.item).isEqualTo(map["item"] as Item)
        Assertions.assertThat(saved.get().semesterItem.item.category).isEqualTo(map["category"] as Category)
        Assertions.assertThat(saved.get().counts).isEqualTo(COUNTS)
        Assertions.assertThat(saved.get().points).isNull()
        Assertions.assertThat(saved.get().extraPoints).isEqualTo(EXTRA_POINTS)
        Assertions.assertThat(saved.get().description1).isEqualTo(DESCRIPTION1)
        Assertions.assertThat(saved.get().description2).isEqualTo(DESCRIPTION2)
    }

    @DisplayName("모든 값을 지정하지 않아도 @ColumnDefault 값이 들어가야 한다")
    @Test
    fun saveMileageRecord2() {
        // given
        val map = prepareDate()
        val mileageRecord = MileageRecord(
            semesterItem = map["semesterItem"] as SemesterItem,
            student = map["student"] as Student
        )

        // when
        mileageRecordRepository.save(mileageRecord)
        val saved = mileageRecordRepository.findById(mileageRecord.id!!)

        // then
        Assertions.assertThat(saved).isNotNull
        Assertions.assertThat(saved.get()).isEqualTo(mileageRecord)
        Assertions.assertThat(saved.get().semesterItem).isEqualTo(map["semesterItem"] as SemesterItem)
        Assertions.assertThat(saved.get().student).isEqualTo(map["student"] as Student)
        Assertions.assertThat(saved.get().semesterItem.item).isEqualTo(map["item"] as Item)
        Assertions.assertThat(saved.get().semesterItem.item.category).isEqualTo(map["category"] as Category)
        Assertions.assertThat(saved.get().counts).isEqualTo(DEFAULT_COUNTS)
        Assertions.assertThat(saved.get().points).isNull()
        Assertions.assertThat(saved.get().extraPoints).isNull()
        Assertions.assertThat(saved.get().description1).isNull()
        Assertions.assertThat(saved.get().description2).isNull()
    }

    @DisplayName("repository: 마일리지 등록 전체 조회")
    @Test
    fun getMileageRecords() {
        // given
        val map = prepareDate()
        val mileageRecord1 = MileageRecord(
            semesterItem = map["semesterItem"] as SemesterItem,
            student = map["student"] as Student
        )
        val mileageRecord2 = MileageRecord(
            semesterItem = map["semesterItem"] as SemesterItem,
            student = map["student"] as Student
        )

        // when
        mileageRecordRepository.save(mileageRecord1)
        mileageRecordRepository.save(mileageRecord2)
        val records = mileageRecordRepository.findAll()

        // then
        Assertions.assertThat(records).isNotNull
        Assertions.assertThat(records).hasSize(2)
    }

    @DisplayName("repository: 마일리지 등록 삭제")
    @Test
    fun deleteMileageRecord() {
        // given
        val map = prepareDate()
        val mileageRecord = MileageRecord(
            semesterItem = map["semesterItem"] as SemesterItem,
            student = map["student"] as Student
        )
        mileageRecordRepository.save(mileageRecord)

        // when
        mileageRecordRepository.deleteById(mileageRecord.id!!)
        val recordOptional = mileageRecordRepository.findById(mileageRecord.id!!)

        // then
        Assertions.assertThat(recordOptional).isEmpty
    }

    fun prepareDate(): Map<String, Any> {
        val category = Category(MileageCategoryRepositoryTests.NAME)
        val savedCategory = categoryRepository.save(category)
        val item = Item(category, MileageItemRepositoryTests.NAME)
        val savedItem = itemRepository.save(item)
        val semesterItem = SemesterItem(item, category)
        val savedSemestserItem = semesterItemRepository.save(semesterItem)
        val student = Student()
        val savedStudent = studentRepository.save(student)
        return mapOf(
            "category" to savedCategory,
            "item" to savedItem,
            "semesterItem" to savedSemestserItem,
            "student" to savedStudent
        )
    }
}
