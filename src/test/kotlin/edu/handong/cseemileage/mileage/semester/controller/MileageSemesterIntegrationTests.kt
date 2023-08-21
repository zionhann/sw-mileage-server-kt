package edu.handong.cseemileage.mileage.semester.controller

import com.fasterxml.jackson.databind.ObjectMapper
import edu.handong.cseemileage.mileage.category.domain.Category
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.domain.Item
import edu.handong.cseemileage.mileage.item.repository.ItemRepository
import edu.handong.cseemileage.mileage.semester.dto.SemesterDto
import edu.handong.cseemileage.mileage.semester.dto.SemesterForm
import edu.handong.cseemileage.mileage.semester.dto.SemesterMultipleForm
import edu.handong.cseemileage.mileage.semester.repository.SemesterRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import javax.annotation.PostConstruct

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class MileageSemesterIntegrationTests @Autowired constructor(
    val mapper: ObjectMapper,
    val semesterRepository: SemesterRepository,
    val itemRepository: ItemRepository,
    val categoryRepository: CategoryRepository,
    val semesterController: MileageSemesterController
) {

    companion object {
        private const val SEMESTER_NAME = "2023-02"
        private const val WEIGHT = 3f
        private const val MAX_POINTS = 15f
        private const val API_URI = "/api/mileage/semesters"
    }

    @Autowired
    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
    }

    @PostConstruct
    fun init() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(semesterController)
            .build()

        var category1 = Category("전공 마일리지", "-", 20)
        categoryRepository.save(category1)

        var category2 = Category("캠프 마일리지", "-", 20)
        categoryRepository.save(category2)

        var subitem1 = Item(category1, "전공 항목1", 0, "설명1", "설명2", "R")
        itemRepository.save(subitem1)

        // subitem1과 category가 다름
        var subitem2 = Item(category2, "캠프 항목1", 0, "설명1", "설명2", "R")
        itemRepository.save(subitem2)

        // subitem2와 semester가 다름
        var subitem3 = Item(category2, "캠프 항목2", 0, "설명1", "설명2", "R")
        itemRepository.save(subitem3)
    }

    /**
     * Lazy Loading이 걸린 item은 service 단에서 검사
     * */
    @DisplayName("integration: 학기별 마일리지 항목 생성")
    @Test
    fun createSemester() {
        // Given
        val itemId = itemRepository.findTopByOrderByIdDesc()?.id ?: 0
        val form = SemesterForm(itemId, WEIGHT, MAX_POINTS, SEMESTER_NAME)
        val req = mapper.writeValueAsString(form)

        // When
        val mvcResult = mockMvc
            .post(API_URI) {
                contentType = MediaType.APPLICATION_JSON
                content = req
            }
            .andExpect { status { isCreated() } }
            .andDo { print() }
            .andReturn()

        val id = semesterRepository.findTopByOrderByIdDesc()?.id ?: 0

        // Then
        Assertions.assertThat(mvcResult.response.status).isEqualTo(HttpStatus.CREATED.value())
        semesterRepository.findById(id).ifPresent {
            Assertions.assertThat(it.weight).isEqualTo(WEIGHT)
            Assertions.assertThat(it.maxPoints).isEqualTo(MAX_POINTS)
            Assertions.assertThat(it.name).isEqualTo(SEMESTER_NAME)
        }
    }

    /**
     * category1 / subitem1
     * category2 / subitem2
     * 2023-02학기에 위의 두 항목을 추가한다.
     * */
    @DisplayName("integration: 학기별 마일리지 항목 생성(multiple)")
    @Test
    fun createSemesterMultiple() {
        // Given
        val item1 = itemRepository.findByName("전공 항목1")
        val item2 = itemRepository.findByName("캠프 항목1")
        val semesterList = mutableListOf<SemesterForm>()
        item1.id?.let {
            semesterList.add(SemesterForm(it, WEIGHT, MAX_POINTS, SEMESTER_NAME))
        }
        item2.id?.let {
            semesterList.add(SemesterForm(it, WEIGHT * 2, MAX_POINTS, SEMESTER_NAME))
        }
        val form = SemesterMultipleForm(semesterList)
        val req = mapper.writeValueAsString(form)

        // When
        val mvcResult = mockMvc
            .post("$API_URI/multiple") {
                contentType = MediaType.APPLICATION_JSON
                content = req
            }
            .andExpect { status { isCreated() } }
            .andDo { print() }
            .andReturn()

        // Then
        Assertions.assertThat(mvcResult.response.status).isEqualTo(HttpStatus.CREATED.value())
        Assertions.assertThat(semesterRepository.findAllByName(SEMESTER_NAME).size).isEqualTo(2)
    }

    @DisplayName("integration: 학기별 마일리지 항목 조회")
    @Test
    fun readSemestersByName() {
        // Given
        val item1 = itemRepository.findByName("전공 항목1")
        val item2 = itemRepository.findByName("캠프 항목1")
        val item3 = itemRepository.findByName("캠프 항목2")
        val semesterList = mutableListOf<SemesterForm>()
        item1.id?.let {
            semesterList.add(SemesterForm(it, WEIGHT, MAX_POINTS, SEMESTER_NAME))
        }
        item2.id?.let {
            semesterList.add(SemesterForm(it, WEIGHT * 2, MAX_POINTS, SEMESTER_NAME))
        }
        item3.id?.let {
            semesterList.add(SemesterForm(it, WEIGHT * 3, MAX_POINTS, "2019-02"))
        }
        val form = SemesterMultipleForm(semesterList)
        val req = mapper.writeValueAsString(form)

        mockMvc
            .post("$API_URI/multiple") {
                contentType = MediaType.APPLICATION_JSON
                content = req
            }
            .andExpect { status { isCreated() } }
            .andDo { print() }
            .andReturn()

        // When
        val mvcResult = mockMvc
            .get("$API_URI?semester=$SEMESTER_NAME")
            .andExpect { status { isOk() } }
            .andDo { print() }
            .andReturn()

        val res = mapper.readValue(
            mvcResult.response.contentAsString,
            SemesterDto::class.java
        )

        // Then
        Assertions.assertThat(mvcResult.response.status).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(res.semesters).hasSize(2)
    }
}
