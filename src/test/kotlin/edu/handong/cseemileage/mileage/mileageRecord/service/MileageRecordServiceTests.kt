package edu.handong.cseemileage.mileage.mileageRecord.service

import edu.handong.cseemileage.mileage.category.dto.CategoryForm
import edu.handong.cseemileage.mileage.category.service.CategoryService
import edu.handong.cseemileage.mileage.item.dto.ItemForm
import edu.handong.cseemileage.mileage.item.service.ItemService
import edu.handong.cseemileage.mileage.mileageRecord.dto.MileageRecordForm
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemForm
import edu.handong.cseemileage.mileage.semesterItem.service.SemesterItemService
import edu.handong.cseemileage.student.dto.StudentForm
import edu.handong.cseemileage.student.service.StudentService
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
    private val mileageRecordQueryService: MileageRecordQueryService
) {

    var semesterId: Int? = null

    @BeforeEach
    fun init() {
        val savedCategory = categoryService.saveCategory(
            CategoryForm(
                title = "전공 마일리지",
                description = "전공 마일리지",
                maxPoints = 0
            )
        )

        val savedItem = itemService.saveItem(
            ItemForm(
                categoryId = savedCategory,
                itemName = "캡스톤 수강",
                description1 = "",
                description2 = "",
                stuType = "",
                ItemForm.Flag(
                    isVisible = true,
                    isPortfolio = false,
                    isMultiple = false,
                    isStudentVisible = false,
                    isStudentEditable = false
                )
            )
        )

        semesterId = semesterItemService.saveSemesterItem(
            SemesterItemForm(
                itemId = savedItem,
                points = 1.0f,
                maxPoints = 0f
            ),
            "2023-02"
        )

        studentService.register(
            StudentForm(
                name = "홍길동",
                sid = "21800123",
                department = "",
                major1 = "",
                major2 = "",
                year = 1,
                semesterCount = 1
            )
        )

        studentService.register(
            StudentForm(
                name = "동길홍",
                sid = "21800456",
                department = "",
                major1 = "",
                major2 = "",
                year = 2,
                semesterCount = 3
            )
        )
    }

    @DisplayName("service: 마일리지 기록 저장")
    @Test
    fun mileageRecordServiceTests_11() {
        // Given
        val form = MileageRecordForm(
            semesterItemId = semesterId!!,
            studentId = "21800123",
            counts = 1,
            description1 = "",
            description2 = ""
        )

        // When
        val id = mileageRecordService.add(form)
        val found = mileageRecordQueryService.getRecordById(id)

        // Then
        assertThat(found.semester).isEqualTo("2023-02")
        assertThat(found.category).isEqualTo("전공 마일리지")
        assertThat(found.subcategory).isEqualTo("캡스톤 수강")
        assertThat(found.studentId).isEqualTo("21800123")
        assertThat(found.points).isEqualTo(1.0f)
        assertThat(found.counts).isEqualTo(1)
    }

    @DisplayName("service: 마일리지 기록 전체 조회")
    @Test
    fun mileageRecordServiceTests_104() {
        // Given
        val record1 = MileageRecordForm(
            semesterItemId = semesterId!!,
            studentId = "21800123",
            counts = 1
        )
        val record2 = MileageRecordForm(
            semesterItemId = semesterId!!,
            studentId = "21800456",
            counts = 2
        )
        mileageRecordService.add(record1)
        mileageRecordService.add(record2)

        // When
        val records = mileageRecordQueryService.getAll()

        // Then
        assertThat(records.size).isEqualTo(2)
    }
}
