package edu.handong.cseemileage.mileage.semesterItem.controller

import com.fasterxml.jackson.databind.ObjectMapper
import edu.handong.cseemileage.mileage.category.domain.Category
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.domain.Item
import edu.handong.cseemileage.mileage.item.repository.ItemRepository
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemForm
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemMultipleForm
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepository
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders
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
    val semesterController: MileageSemesterItemController
) {

    companion object {
        private const val SEMESTER_NAME = "2023-02"
        private const val POINT_VALUE = 3f
        private const val ITEM_MAX_POINTS = 15f
        private const val API_URI = "/api/mileage/semesters"
    }

    @Autowired
    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
    }

    @PostConstruct
    @Profile("dev") // CI 환경에서만 실행
    fun init() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(semesterController)
            .build()

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

    /**
     * Lazy Loading이 걸린 item은 service 단에서 검사
     * */
    @DisplayName("integration: 학기별 마일리지 항목 생성")
    @Test
    fun createSemester() {
        // Given
        val itemId = itemRepository.findTopByOrderByIdDesc()?.id ?: 0
        val form = SemesterItemForm(itemId, POINT_VALUE, ITEM_MAX_POINTS)
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
        semesterItemRepository.findById(id).ifPresent {
            Assertions.assertThat(it.pointValue).isEqualTo(POINT_VALUE)
            Assertions.assertThat(it.itemMaxPoints).isEqualTo(ITEM_MAX_POINTS)
            Assertions.assertThat(it.semesterName).isEqualTo(SEMESTER_NAME)
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
        val semesterList = mutableListOf<SemesterItemForm>()
        item1.id?.let {
            semesterList.add(SemesterItemForm(it, POINT_VALUE, ITEM_MAX_POINTS))
        }
        item2.id?.let {
            semesterList.add(SemesterItemForm(it, POINT_VALUE * 2, ITEM_MAX_POINTS))
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
}
