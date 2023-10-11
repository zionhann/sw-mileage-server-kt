package edu.handong.cseemileage.service

import edu.handong.cseemileage.controller.MileageRecordIntegrationTests.Companion.COUNTS
import edu.handong.cseemileage.controller.MileageRecordIntegrationTests.Companion.DESCRIPTION1
import edu.handong.cseemileage.controller.MileageRecordIntegrationTests.Companion.DESCRIPTION2
import edu.handong.cseemileage.controller.MileageRecordIntegrationTests.Companion.EXTRA_POINTS
import edu.handong.cseemileage.controller.StudentIntegrationTests.Companion.NAME
import edu.handong.cseemileage.controller.StudentIntegrationTests.Companion.SID
import edu.handong.cseemileage.dto.account.student.StudentForm
import edu.handong.cseemileage.dto.mileage.category.CategoryForm
import edu.handong.cseemileage.dto.mileage.item.ItemForm
import edu.handong.cseemileage.dto.mileage.record.MileageRecordForm
import edu.handong.cseemileage.dto.mileage.semesterItem.SemesterItemForm
import edu.handong.cseemileage.repository.mileage.MileageRecordRepository
import edu.handong.cseemileage.service.category.CategoryService
import edu.handong.cseemileage.service.item.ItemService
import edu.handong.cseemileage.service.record.MileageRecordQueryService
import edu.handong.cseemileage.service.record.MileageRecordService
import edu.handong.cseemileage.service.semesterItem.SemesterItemService
import edu.handong.cseemileage.service.student.StudentService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.transaction.Transactional

@SpringBootTest
@Transactional
class MileageRecordServiceTests @Autowired constructor(
    private val semesterItemService: SemesterItemService,
    private val studentService: StudentService,
    private val categoryService: CategoryService,
    private val itemService: ItemService,
    private val mileageRecordService: MileageRecordService,
    private val mileageRecordQueryService: MileageRecordQueryService,
    private val mileageRecordRepository: MileageRecordRepository
) {

    var semesterId: Int? = null
    var studentId1: Int? = null
    var studentId2: Int? = null

    @BeforeEach
    fun init() {
        val savedCategory = categoryService.saveCategory(
            CategoryForm(
                title = "전공 마일리지",
                description1 = null,
                description2 = null,
                orderIdx = null,
                type = null,
                categoryMaxPoints = 20f
            )
        )

        val savedItem = itemService.saveItem(
            ItemForm(
                categoryId = savedCategory,
                itemName = "캡스톤 수강",
                description1 = "",
                description2 = "",
                stuType = "",
                itemMaxPoints = 10f,
                ItemForm.Flag(
                    isVisible = true,
                    isPortfolio = false,
                    isStudentVisible = false,
                    isStudentEditable = false
                )
            )
        )

        semesterId = semesterItemService.saveSemesterItem(
            SemesterItemForm(
                itemId = savedItem,
                points = 1.0f,
                itemMaxPoints = 1.0f,
                semesterName = null,
                isMulti = true
            ),
            "2023-02"
        )

        studentId1 = studentService.register(
            StudentForm(
                name = "홍길동",
                sid = "21800123",
                department = "",
                major1 = "",
                major2 = "",
                year = 1,
                semesterCount = 1,
                isChecked = true
            )
        )

        studentId2 = studentService.register(
            StudentForm(
                name = "동길홍",
                sid = "21800456",
                department = "",
                major1 = "",
                major2 = "",
                year = 2,
                semesterCount = 3,
                isChecked = true
            )
        )
    }

    @DisplayName("service: 마일리지 기록 저장")
    @Test
    fun mileageRecordServiceTests_11() {
        // Given
        val form = MileageRecordForm(
            semesterItemId = semesterId!!,
            sid = SID,
            studentName = NAME,
            counts = COUNTS,
            extraPoints = EXTRA_POINTS,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2
        )

        // When
        val id = mileageRecordService.add(form)
        val found = mileageRecordRepository.findById(id)

        // Then
        assertThat(found).isNotNull
    }

    @DisplayName("service: 마일리지 기록 전체 조회")
    @Test
    fun mileageRecordServiceTests_104() {
        // Given
        val record1 = MileageRecordForm(
            semesterItemId = semesterId!!,
            sid = SID,
            studentName = NAME,
            counts = COUNTS,
            extraPoints = EXTRA_POINTS,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2
        )
        val record2 = MileageRecordForm(
            semesterItemId = semesterId!!,
            sid = SID,
            studentName = NAME,
            counts = COUNTS,
            extraPoints = EXTRA_POINTS,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2
        )
        mileageRecordService.add(record1)
        mileageRecordService.add(record2)

        // When
        val records = mileageRecordQueryService.getRecords()

        // Then
        assertThat(records.size).isEqualTo(2)
    }
}
