package edu.handong.cseemileage.controller

import com.fasterxml.jackson.databind.ObjectMapper
import edu.handong.cseemileage.controller.StudentIntegrationTests.Companion.NAME
import edu.handong.cseemileage.controller.StudentIntegrationTests.Companion.SID
import edu.handong.cseemileage.dto.mileage.category.CategoryForm
import edu.handong.cseemileage.dto.mileage.item.ItemForm
import edu.handong.cseemileage.dto.mileage.record.MileageRecordDto
import edu.handong.cseemileage.dto.mileage.record.MileageRecordForm
import edu.handong.cseemileage.dto.mileage.semesterItem.SemesterItemForm
import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.ExceptionResponse
import edu.handong.cseemileage.repository.mileage.MileageRecordRepository
import edu.handong.cseemileage.service.category.CategoryService
import edu.handong.cseemileage.service.item.ItemService
import edu.handong.cseemileage.service.record.MileageRecordQueryService
import edu.handong.cseemileage.service.record.MileageRecordService
import edu.handong.cseemileage.service.semesterItem.SemesterItemService
import edu.handong.cseemileage.service.student.StudentService
import edu.handong.cseemileage.util.ExtractExceptionMessage.Companion.getExceptionMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
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

    var semesterId: Int? = null

    @BeforeEach
    fun init() {
        val savedCategory = categoryService.saveCategory(
            CategoryForm(
                title = "전공 마일리지",
                description1 = null,
                description2 = null,
                orderIdx = null,
                itemType = null,
                isMulti = null,
                categoryMaxPoints = null
            )
        )

        val savedItem = itemService.saveItem(
            ItemForm(
                categoryId = savedCategory,
                itemName = "캡스톤 수강",
                description1 = "",
                description2 = "",
                stuType = "",
                itemMaxPoints = 20f,
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
                semesterName = null
            ),
            "2023-02"
        )
    }

    @DisplayName("마일리지 기록을 생성한다.")
    @Test
    fun mileageRecordIntegrationTests_23() {
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
        assertThat(res.count).isEqualTo(2)
    }

    @DisplayName("service: semesterItem_id가 음수인 경우")
    @Test
    fun mileageRecordIntegrationTests_204() {
        // Given
        val form = createFormSemesterItemIdNegative()
        val req = objectMapper.writeValueAsString(form)

        // When
        val mvcResult = mockMvc
            .post("/api/mileage/records") {
                contentType = MediaType.APPLICATION_JSON
                content = req
            }
            .andExpect { status { isBadRequest() } }
            .andDo { print() }
            .andReturn()

        val res = objectMapper.readValue(
            mvcResult.response.contentAsString,
            ExceptionResponse::class.java
        )
        // Then
        assertThat(res.error).isEqualTo(HttpStatus.BAD_REQUEST.reasonPhrase)
        assertThat(getExceptionMessage(mvcResult)).isEqualTo(ExceptionMessage.RECORD_SEMESTER_IS_NOT_POSITIVE)
    }

    @DisplayName("service: counts가 음수인 경우")
    @Test
    fun mileageRecordIntegrationTests_256() {
        // Given
        val form = createFormCountsNegative()
        val req = objectMapper.writeValueAsString(form)

        // When
        val mvcResult = mockMvc
            .post("/api/mileage/records") {
                contentType = MediaType.APPLICATION_JSON
                content = req
            }
            .andExpect { status { isBadRequest() } }
            .andDo { print() }
            .andReturn()

        val res = objectMapper.readValue(
            mvcResult.response.contentAsString,
            ExceptionResponse::class.java
        )
        // Then
        assertThat(res.error).isEqualTo(HttpStatus.BAD_REQUEST.reasonPhrase)
        assertThat(getExceptionMessage(mvcResult)).isEqualTo(ExceptionMessage.RECORD_INVALID_COUNTS)
    }

    fun createFormSemesterItemIdNegative(): MileageRecordForm {
        return MileageRecordForm(
            semesterItemId = -1,
            sid = SID,
            studentName = NAME,
            counts = COUNTS,
            extraPoints = EXTRA_POINTS,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2
        )
    }

    fun createFormCountsNegative(): MileageRecordForm {
        return MileageRecordForm(
            semesterItemId = semesterId!!,
            sid = SID,
            studentName = NAME,
            counts = -1f,
            extraPoints = EXTRA_POINTS,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2
        )
    }
}
