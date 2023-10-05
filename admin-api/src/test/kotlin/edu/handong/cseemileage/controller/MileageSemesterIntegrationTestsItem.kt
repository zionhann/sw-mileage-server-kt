package edu.handong.cseemileage.controller

import com.fasterxml.jackson.databind.ObjectMapper
import edu.handong.cseemileage.domain.mileage.Category
import edu.handong.cseemileage.domain.mileage.Item
import edu.handong.cseemileage.dto.mileage.semesterItem.SemesterItemForm
import edu.handong.cseemileage.dto.mileage.semesterItem.SemesterItemMultipleForm
import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.ExceptionResponse
import edu.handong.cseemileage.repository.mileage.CategoryRepository
import edu.handong.cseemileage.repository.mileage.ItemRepository
import edu.handong.cseemileage.repository.mileage.SemesterItemRepository
import edu.handong.cseemileage.util.ExtractExceptionMessage.Companion.getExceptionMessage
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class MileageSemesterIntegrationTestsItem @Autowired constructor(
    val mapper: ObjectMapper,
    val semesterItemRepository: SemesterItemRepository,
    val itemRepository: ItemRepository,
    val categoryRepository: CategoryRepository,
    val semesterController: MileageSemesterItemController,
    val mockMvc: MockMvc
) {
    companion object {
        private const val API_URI = "/api/mileage/semesters"

        const val SEMESTER_NAME = "2023-01"
        const val POINT_VALUE = 5f
        const val ITEM_MAX_POINTS = 10f
        const val CATEGORY_MAX_POINTS = 15f

        const val UPDATE_SEMESTER_NAME = "2024-02"
        const val UPDATE_POINT_VALUE = 2f
        const val UPDATE_ITEM_MAX_POINTS = 6f
        const val UPDATE_CATEGORY_MAX_POINTS = 12f

        const val DEFAULT_SEMESTER_NAME = "2023-02"
        const val DEFAULT_POINT_VALUE = 0f
        const val DEFAULT_ITEM_MAX_POINTS = 0f
        const val DEFAULT_CATEGORY_MAX_POINTS = 0f
    }

    @BeforeEach
    fun setUp() {
    }

    @PostConstruct
    @Profile("dev") // CI 환경에서만 실행
    fun init() {
        var category1 = Category("전공 마일리지")
        categoryRepository.save(category1)

        var category2 = Category("캠프 마일리지")
        categoryRepository.save(category2)

        var subitem1 = Item(category1, "전공 항목1")
        itemRepository.save(subitem1)

        // subitem1과 category가 다름
        var subitem2 = Item(category2, "캠프 항목1")
        itemRepository.save(subitem2)

        // subitem2와 semester가 다름
        var subitem3 = Item(category2, "캠프 항목2")
        itemRepository.save(subitem3)
    }

    @DisplayName("integration: 학기별 마일리지 항목 생성")
    @Test
    fun createSemester() {
        // Given
        val itemId = itemRepository.findTopByOrderByIdDesc()?.id ?: 0
        val form = SemesterItemForm(
            itemId = itemId,
            points = POINT_VALUE,
            itemMaxPoints = ITEM_MAX_POINTS,
            semesterName = null,
            isMulti = true
        )
        val req = mapper.writeValueAsString(form)

        // When
        val mvcResult = mockMvc
            .post("$API_URI/{SEMESTER_NAME}/items", SEMESTER_NAME) {
                contentType = MediaType.APPLICATION_JSON
                content = req
            }
            .andExpect { status { isCreated() } }
            .andDo { print() }
            .andReturn()

        val id = semesterItemRepository.findTopByOrderByIdDesc()?.id ?: 0

        // Then
        Assertions.assertThat(mvcResult.response.status).isEqualTo(HttpStatus.CREATED.value())
    }

    /**
     * category1 / subitem1
     * category2 / subitem2
     * 2023-02학기에 위의 두 항목을 추가한다.
     * */
    //    @Test
    @DisplayName("integration: 학기별 마일리지 항목 생성(multiple)")
    fun createSemesterMultiple() {
        // Given
        val item1 = itemRepository.findByName("전공 항목1")
        val item2 = itemRepository.findByName("캠프 항목1")
        val semesterList = mutableListOf<SemesterItemForm>()
        if (item1 != null) {
            item1.id?.let {
                semesterList.add(
                    SemesterItemForm(
                        itemId = it,
                        points = POINT_VALUE,
                        itemMaxPoints = ITEM_MAX_POINTS,
                        semesterName = null,
                        isMulti = true
                    )
                )
            }
        }
        if (item2 != null) {
            item2.id?.let {
                semesterList.add(
                    SemesterItemForm(
                        itemId = it,
                        points = POINT_VALUE * 2,
                        itemMaxPoints = ITEM_MAX_POINTS,
                        semesterName = null,
                        isMulti = true
                    )
                )
            }
        }
        val form = SemesterItemMultipleForm(semesterList)
        val req = mapper.writeValueAsString(form)

        // When
        val mvcResult = mockMvc
            .post("$API_URI/{SEMESTER_NAME}/multiple", SEMESTER_NAME) {
                contentType = MediaType.APPLICATION_JSON
                content = req
            }
            .andExpect { status { isCreated() } }
            .andDo { print() }
            .andReturn()

        // Then
        Assertions.assertThat(mvcResult.response.status).isEqualTo(HttpStatus.CREATED.value())
        Assertions.assertThat(semesterItemRepository.findAllBySemesterName(SEMESTER_NAME).size).isEqualTo(2)
    }

    @DisplayName("integration: 글로벌 항목 ID가 음수인 경우")
    @Test
    fun MileageSemesterItemServiceTestsItem_163() {
        // Given
        val form = SemesterItemForm(
            itemId = -1,
            points = POINT_VALUE,
            itemMaxPoints = ITEM_MAX_POINTS,
            semesterName = SEMESTER_NAME,
            isMulti = true
        )
        val req = mapper.writeValueAsString(form)

        // When
        val mvcResult = mockMvc
            .post("$API_URI/{SEMESTER_NAME}/items", SEMESTER_NAME) {
                contentType = MediaType.APPLICATION_JSON
                content = req
            }
            .andExpect { status { isBadRequest() } }
            .andDo { print() }
            .andReturn()

        val response = mapper.readValue(
            mvcResult.response.contentAsString,
            ExceptionResponse::class.java
        )

        // Then
        Assertions.assertThat(response.status).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(getExceptionMessage(mvcResult)).isEqualTo(ExceptionMessage.SEMESTER_ITEM_ID_IS_NOT_POSITIVE)
    }
}
