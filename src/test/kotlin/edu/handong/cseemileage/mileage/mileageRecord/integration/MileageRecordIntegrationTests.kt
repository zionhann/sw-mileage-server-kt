package edu.handong.cseemileage.mileage.mileageRecord.integration

import com.fasterxml.jackson.databind.ObjectMapper
import edu.handong.cseemileage.mileage.category.dto.CategoryForm
import edu.handong.cseemileage.mileage.category.service.CategoryService
import edu.handong.cseemileage.mileage.item.dto.ItemForm
import edu.handong.cseemileage.mileage.item.service.ItemService
import edu.handong.cseemileage.mileage.mileageRecord.dto.MileageRecordDto
import edu.handong.cseemileage.mileage.mileageRecord.dto.MileageRecordForm
import edu.handong.cseemileage.mileage.mileageRecord.repository.MileageRecordRepository
import edu.handong.cseemileage.mileage.mileageRecord.repository.MileageRecordRepositoryTests.Companion.COUNTS
import edu.handong.cseemileage.mileage.mileageRecord.repository.MileageRecordRepositoryTests.Companion.DESCRIPTION1
import edu.handong.cseemileage.mileage.mileageRecord.repository.MileageRecordRepositoryTests.Companion.DESCRIPTION2
import edu.handong.cseemileage.mileage.mileageRecord.repository.MileageRecordRepositoryTests.Companion.EXTRA_POINTS
import edu.handong.cseemileage.mileage.mileageRecord.repository.MileageRecordRepositoryTests.Companion.POINTS
import edu.handong.cseemileage.mileage.mileageRecord.service.MileageRecordQueryService
import edu.handong.cseemileage.mileage.mileageRecord.service.MileageRecordService
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemForm
import edu.handong.cseemileage.mileage.semesterItem.service.SemesterItemService
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
    private val mileageRecordService: MileageRecordService,
    private val mileageRecordQueryService: MileageRecordQueryService,
    private val objectMapper: ObjectMapper,
    private val categoryService: CategoryService,
    private val itemService: ItemService,
    private val semesterItemService: SemesterItemService,
    private val studentService: StudentService,
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
                itemType = null,
                isMulti = null
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
                itemMaxPoints = 1.0f,
                categoryMaxPoints = 1.0f,
                semesterName = null
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

    @DisplayName("마일리지 기록을 생성한다.")
    @Test
    fun mileageRecordIntegrationTests_23() {
        // Given
        val form = MileageRecordForm(
            semesterItemId = semesterId!!,
            studentId = studentId1!!,
            counts = COUNTS,
            points = POINTS,
            extraPoints = EXTRA_POINTS,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2
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
        val found = mileageRecordRepository.findById(id)

        // Then
        assertThat(found).isNotNull
    }

    @DisplayName("마일리지 기록 전체조회")
    @Test
    fun mileageRecordIntegrationTests_125() {
        // Given
        val record1 = MileageRecordForm(
            semesterItemId = semesterId!!,
            studentId = studentId1!!,
            counts = COUNTS,
            points = POINTS,
            extraPoints = EXTRA_POINTS,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2
        )
        val record2 = MileageRecordForm(
            semesterItemId = semesterId!!,
            studentId = studentId2!!,
            counts = COUNTS,
            points = POINTS,
            extraPoints = EXTRA_POINTS,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2
        )
        mileageRecordService.add(record1)
        mileageRecordService.add(record2)

        // When
        val mvcResult = mockMvc
            .get("/api/mileage/records") {
                contentType = MediaType.APPLICATION_JSON
            }.andExpect { status { isOk() } }
            .andDo { print() }
            .andReturn()

        val res = objectMapper.readValue(
            mvcResult.response.contentAsString,
            MileageRecordDto::class.java
        )

        // Then
        assertThat(res.records).hasSize(2)
    }
}
