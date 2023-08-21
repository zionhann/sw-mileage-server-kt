package edu.handong.cseemileage.mileage.item.controller

import com.fasterxml.jackson.databind.ObjectMapper
import edu.handong.cseemileage.mileage.category.domain.Category
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.dto.ItemDto
import edu.handong.cseemileage.mileage.item.dto.ItemForm
import edu.handong.cseemileage.mileage.item.repository.ItemRepository
import org.assertj.core.api.Assertions.assertThat
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
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import javax.annotation.PostConstruct

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class MileageItemIntegrationTests @Autowired constructor(
    val mapper: ObjectMapper,
    val itemRepository: ItemRepository,
    val categoryRepository: CategoryRepository,
    val subitemController: MileageItemController
) {

    @Autowired
    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
    }

    @PostConstruct
    fun init() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(subitemController)
            .build()

        var category = Category("전공 마일리지", "-", 20)
        categoryRepository.save(category)
    }

    @DisplayName("integration: 마일리지 항목 생성")
    @Test
    fun createSubitem() {
        // Given
        val categoryId = categoryRepository.findTopByOrderByIdDesc()?.id ?: 0
        val form = ItemForm(categoryId, "전공 항목1", 0, "설명1", "설명2", "R")
        val req = mapper.writeValueAsString(form)

        // When
        val mvcResult = mockMvc
            .post("/api/mileage/items") {
                contentType = MediaType.APPLICATION_JSON
                content = req
            }
            .andExpect { status { isOk() } }
            .andDo { print() }
            .andReturn()

        val id = itemRepository.findTopByOrderByIdDesc()?.id ?: 0

        // Then
        assertThat(mvcResult.response.status).isEqualTo(HttpStatus.OK.value())
        itemRepository.findById(id).ifPresent {
            assertThat(it.name).isEqualTo("전공 항목1")
            assertThat(it.isPortfolio).isEqualTo(0)
            assertThat(it.description1).isEqualTo("설명1")
            assertThat(it.description2).isEqualTo("설명2")
            assertThat(it.stuType).isEqualTo("R")
        }
    }

    @DisplayName("integration: 마일리지 항목 전체 조회")
    @Test
    fun getSubitems() {
        // Given
        val categoryId = categoryRepository.findTopByOrderByIdDesc()?.id ?: 0
        val form1 = ItemForm(categoryId, "전공 항목1", 0, "설명1", "설명2", "R")
        val form2 = ItemForm(categoryId, "전공 항목2", 0, "설명1", "설명2", "R")

        val req1 = mapper.writeValueAsString(form1)
        val req2 = mapper.writeValueAsString(form2)

        mockMvc
            .post("/api/mileage/items") {
                contentType = MediaType.APPLICATION_JSON
                content = req1
            }
            .andExpect { status { isOk() } }
            .andReturn()
        mockMvc
            .post("/api/mileage/items") {
                contentType = MediaType.APPLICATION_JSON
                content = req2
            }
            .andExpect { status { isOk() } }
            .andReturn()

        // When
        val mvcResult = mockMvc
            .get("/api/mileage/items")
            .andExpect { status { isOk() } }
            .andDo { print() }
            .andReturn()

        val res = mapper.readValue(
            mvcResult.response.contentAsString,
            ItemDto::class.java
        )

        // Then
        assertThat(res.items).hasSize(2)
    }

    @DisplayName("integration: 마일리지 항목 수정")
    @Test
    fun modifySubitem() {
        // Given
        val categoryId = categoryRepository.findTopByOrderByIdDesc()?.id ?: 0
        val form = ItemForm(categoryId, "전공 항목1", 0, "설명1", "설명2", "R")
        val req = mapper.writeValueAsString(form)
        val modifyForm = form.copy(itemName = "수정된 전공 항목 이름")
        val req2 = mapper.writeValueAsString(modifyForm)

        mockMvc
            .post("/api/mileage/items") {
                contentType = MediaType.APPLICATION_JSON
                content = req
            }
            .andExpect { status { isOk() } }
            .andReturn()

        val topId = itemRepository.findTopByOrderByIdDesc()?.id ?: 0

        // When
        mockMvc
            .patch("/api/mileage/items/$topId") {
                contentType = MediaType.APPLICATION_JSON
                content = req2
            }

        // Then
        itemRepository.findById(topId).ifPresent {
            assertThat(it.name).isEqualTo("수정된 전공 항목 이름")
        }
    }

    @DisplayName("integration: 마일리지 항목 삭제")
    @Test
    fun deleteSubitem() {
        // Given
        val categoryId = categoryRepository.findTopByOrderByIdDesc()?.id ?: 0
        val form = ItemForm(categoryId, "전공 항목1", 0, "설명1", "설명2", "R")
        val req = mapper.writeValueAsString(form)

        mockMvc
            .post("/api/mileage/items") {
                contentType = MediaType.APPLICATION_JSON
                content = req
            }
            .andExpect { status { isOk() } }
            .andDo { print() }
            .andReturn()

        val id = itemRepository.findTopByOrderByIdDesc()?.id ?: 0

        // When
        mockMvc
            .delete("/api/mileage/items/$id")
            .andExpect { status { isOk() } }
            .andDo { print() }
            .andReturn()

        // Then
        assertThat(itemRepository.findById(id).isEmpty).isTrue
    }

    @DisplayName("integration: service 의존성 주입")
    @Test
    fun getSubitemService() {
        assertThat(subitemController.itemService).isNotNull
    }

    @DisplayName("integration: query service 의존성 주입")
    @Test
    fun getSubitemQueryService() {
        assertThat(subitemController.itemQueryService).isNotNull
    }
}
