package edu.handong.cseemileage.mileage.record.integration

import com.fasterxml.jackson.databind.ObjectMapper
import edu.handong.cseemileage.mileage.category.dto.CategoryForm
import edu.handong.cseemileage.mileage.category.service.CategoryService
import edu.handong.cseemileage.mileage.item.dto.ItemForm
import edu.handong.cseemileage.mileage.item.service.ItemService
import edu.handong.cseemileage.mileage.record.dto.RecordDto
import edu.handong.cseemileage.mileage.record.dto.RecordForm
import edu.handong.cseemileage.mileage.record.service.RecordQueryService
import edu.handong.cseemileage.mileage.record.service.RecordService
import edu.handong.cseemileage.mileage.semester.dto.SemesterForm
import edu.handong.cseemileage.mileage.semester.service.SemesterService
import edu.handong.cseemileage.student.dto.StudentForm
import edu.handong.cseemileage.student.service.StudentService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import javax.transaction.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MileageRecordIntegrationTests @Autowired constructor(
    private val mockMvc: MockMvc,
    private val recordService: RecordService,
    private val recordQueryService: RecordQueryService,
    private val objectMapper: ObjectMapper,
    private val categoryService: CategoryService,
    private val itemService: ItemService,
    private val semesterService: SemesterService,
    private val studentService: StudentService
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

    @DisplayName("마일리지 기록을 생성한다.")
    @Test
    fun mileageRecordIntegrationTests_23() {
        // Given
        val form = RecordForm(
            semesterId = semesterId!!,
            studentId = "21800123",
            counts = 1
        )
        val req = objectMapper.writeValueAsString(form)

        // When
        val mvcResult = mockMvc
            .post("/api/mileage/records") {
                contentType = MediaType.APPLICATION_JSON
                content = req
            }.andExpect { status { isCreated() } }
            .andDo { print() }
            .andReturn()

        val res = objectMapper.readValue(
            mvcResult.response.contentAsString,
            Map::class.java
        )
        val id = res["id"] as Int
        val found = recordQueryService.getRecordById(id)

        // Then
        assertThat(found.semester).isEqualTo("2023-2")
        assertThat(found.category).isEqualTo("전공 마일리지")
        assertThat(found.subcategory).isEqualTo("캡스톤 수강")
        assertThat(found.studentId).isEqualTo("21800123")
        assertThat(found.points).isEqualTo(1.0f)
        assertThat(found.counts).isEqualTo(1)
    }

    @DisplayName("마일리지 기록 전체조회")
    @Test
    fun mileageRecordIntegrationTests_125() {
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
        val mvcResult = mockMvc
            .get("/api/mileage/records") {
                contentType = MediaType.APPLICATION_JSON
            }.andExpect { status { isOk() } }
            .andDo { print() }
            .andReturn()

        val res = objectMapper.readValue(
            mvcResult.response.contentAsString,
            RecordDto::class.java
        )

        // Then
        assertThat(res.records).hasSize(2)
    }
}
