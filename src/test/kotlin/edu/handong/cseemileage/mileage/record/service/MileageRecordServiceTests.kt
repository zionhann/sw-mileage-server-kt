package edu.handong.cseemileage.mileage.record.service

import edu.handong.cseemileage.mileage.category.dto.CategoryForm
import edu.handong.cseemileage.mileage.category.service.CategoryService
import edu.handong.cseemileage.mileage.item.dto.ItemForm
import edu.handong.cseemileage.mileage.item.service.ItemService
import edu.handong.cseemileage.mileage.record.dto.RecordForm
import edu.handong.cseemileage.mileage.semester.dto.SemesterForm
import edu.handong.cseemileage.mileage.semester.service.SemesterService
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
    private val semesterService: SemesterService,
    private val studentService: StudentService,
    private val categoryService: CategoryService,
    private val itemService: ItemService,
    private val recordService: RecordService,
    private val recordQueryService: RecordQueryService
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
                isPortfolio = 0,
                description1 = "",
                description2 = "",
                stuType = ""
            )
        )

        semesterId = semesterService.saveSemester(
            SemesterForm(
                itemId = savedItem,
                name = "2023-2",
                weight = 1.0f,
                maxPoints = 0f
            )
        )

        studentService.register(
            StudentForm(
                name = "홍길동",
                sid = "21800123",
                email = "",
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
                email = "",
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
        val form = RecordForm(
            semesterId = semesterId!!,
            studentId = "21800123",
            counts = 1,
            description1 = "",
            description2 = ""
        )

        // When
        val id = recordService.add(form)
        val found = recordQueryService.getRecordById(id)

        // Then
        assertThat(found.semester).isEqualTo("2023-2")
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
        val record1 = RecordForm(
            semesterId = semesterId!!,
            studentId = "21800123",
            counts = 1
        )
        val record2 = RecordForm(
            semesterId = semesterId!!,
            studentId = "21800456",
            counts = 2
        )
        recordService.add(record1)
        recordService.add(record2)

        // When
        val records = recordQueryService.getAll()

        // Then
        assertThat(records.size).isEqualTo(2)
    }
}
