package edu.handong.cseemileage.controller

import ExceptionMessage.Companion.CATEGORY_INVALID_POINTS
import ExceptionMessage.Companion.CATEGORY_POINTS_IS_EMPTY
import ExceptionMessage.Companion.CATEGORY_TITLE_IS_EMPTY
import com.fasterxml.jackson.databind.ObjectMapper
import edu.handong.cseemileage.dto.mileage.category.CategoryDto
import edu.handong.cseemileage.dto.mileage.category.CategoryForm
import edu.handong.cseemileage.exception.ExceptionResponse
import edu.handong.cseemileage.service.category.CategoryQueryService
import edu.handong.cseemileage.service.category.CategoryService
import edu.handong.cseemileage.util.ExtractExceptionMessage.Companion.getExceptionMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class MileageCategoryIntegrationTests @Autowired constructor(
    val restTemplate: TestRestTemplate,
    val mockMvc: MockMvc,
    val mapper: ObjectMapper,
    val categoryService: CategoryService,
    val categoryQueryService: CategoryQueryService
) {

    companion object {
        const val NAME = "전공 상담"
        const val ORDER_IDX = 5
        const val DESCRIPTION1 = "description1"
        const val DESCRIPTION2 = "description2"
        const val ITEM_TYPE = "R"
        const val IS_MULTI = true

        const val UPDATE_NAME = "전공 상담 수정"
        const val UPDATE_ORDER_IDX = 2
        const val UPDATE_DESCRIPTION1 = "description1 수정"
        const val UPDATE_DESCRIPTION2 = "description2 수정"
        const val UPDATE_ITEM_TYPE = "R"
        const val UPDATE_IS_MULTI = false

        const val DEFAULT_ORDER_IDX = 0
        const val DEFAULT_ITEM_TYPE = "R"
        const val DEFAULT_IS_MULTI = false
    }

    @DisplayName("integration: 마일리지 카테고리 생성")
    @Test
    fun mileageCategoryIntegrationTests_31() {
        // Given
        val form = CategoryForm(
            title = NAME,
            orderIdx = ORDER_IDX,
            type = ITEM_TYPE,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2,
            categoryMaxPoints = 20f
        )
        val req = mapper.writeValueAsString(form)

        // When
        mockMvc
            .post("/api/mileage/categories") {
                contentType = MediaType.APPLICATION_JSON
                content = req
            }
            .andExpect { status { isCreated() } }
            .andDo { print() }
    }

    @Deprecated("maxPoints 필드 삭제 - 글로벌 카테고리 변경")
    @DisplayName("service: 마일리지 값이 음수인 경우")
    fun mileageCategoryIntegrationTests_54() {
        // Given
        val form = CategoryForm("전공 마일리지", null, null, null, null, null)

        // When
        val mvcResults = mockMvc
            .post("/api/mileage/categories") {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(form)
            }
            .andExpect { status { isBadRequest() } }
            .andDo { print() }
            .andReturn()

        val res = mapper.readValue(
            mvcResults.response.contentAsString,
            ExceptionResponse::class.java
        )
        // Then
        assertThat(res.error).isEqualTo(HttpStatus.BAD_REQUEST.reasonPhrase)
        assertThat(getExceptionMessage(mvcResults)).isEqualTo(CATEGORY_INVALID_POINTS)
    }

    @Deprecated("maxPoints 필드 삭제 - 글로벌 카테고리 변경")
    @DisplayName("service: 마일리지 값이 없는 경우")
    fun mileageCategoryIntegrationTests_82() {
        // Given
        val form = CategoryForm("전공 마일리지", null, null, null, null, null)

        // When
        val mvcResults = mockMvc
            .post("/api/mileage/categories") {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(form)
            }
            .andExpect { status { isBadRequest() } }
            .andDo { print() }
            .andReturn()

        val res = mapper.readValue(
            mvcResults.response.contentAsString,
            ExceptionResponse::class.java
        )
        // Then
        assertThat(res.error).isEqualTo(HttpStatus.BAD_REQUEST.reasonPhrase)
        assertThat(getExceptionMessage(mvcResults)).isEqualTo(CATEGORY_POINTS_IS_EMPTY)
    }

    @DisplayName("service: 제목이 없는 경우")
    @Test
    fun mileageCategoryIntegrationTests_107() {
        // Given
        val form = CategoryForm(
            title = null,
            orderIdx = ORDER_IDX,
            type = ITEM_TYPE,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2,
            categoryMaxPoints = 20f
        )

        // When
        val mvcResults = mockMvc
            .post("/api/mileage/categories") {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(form)
            }
            .andExpect { status { isBadRequest() } }
            .andDo { print() }
            .andReturn()

        val res = mapper.readValue(
            mvcResults.response.contentAsString,
            ExceptionResponse::class.java
        )
        // Then
        assertThat(res.error).isEqualTo(HttpStatus.BAD_REQUEST.reasonPhrase)
        assertThat(getExceptionMessage(mvcResults)).isEqualTo(CATEGORY_TITLE_IS_EMPTY)
    }

    @DisplayName("service: 마일리지 카테고리 목록 조회")
    @Test
    fun mileageCategoryIntegrationTests_132() {
        // Given
        val form1 = CategoryForm(
            title = NAME,
            orderIdx = ORDER_IDX,
            type = ITEM_TYPE,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2,
            categoryMaxPoints = 20f
        )
        val form2 = CategoryForm(
            title = "category2",
            orderIdx = ORDER_IDX,
            type = ITEM_TYPE,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2,
            categoryMaxPoints = 20f
        )
        categoryService.saveCategory(form1)
        categoryService.saveCategory(form2)

        // When
        val mvcResult = mockMvc
            .get("/api/mileage/categories")
            .andExpect { status { isOk() } }
            .andDo { print() }
            .andReturn()
        val res = mapper.readValue(
            mvcResult.response.contentAsString,
            CategoryDto::class.java
        )

        // Then
        assertThat(res.count).isEqualTo(2)
    }

    @DisplayName("integration: 마일리지 카테고리 수정")
    @Test
    fun mileageCategoryIntegrationTests_157() {
        // Given
        val form = CategoryForm(
            title = NAME,
            orderIdx = ORDER_IDX,
            type = ITEM_TYPE,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2,
            categoryMaxPoints = 20f
        )
        val initialId = categoryService.saveCategory(form)

        val updateForm = CategoryForm(
            title = UPDATE_NAME,
            orderIdx = UPDATE_ORDER_IDX,
            type = UPDATE_ITEM_TYPE,
            description1 = UPDATE_DESCRIPTION1,
            description2 = UPDATE_DESCRIPTION2,
            categoryMaxPoints = 10f
        )
        val req = mapper.writeValueAsString(updateForm)

        // When
        val mvcResult = mockMvc
            .patch("/api/mileage/categories/$initialId") {
                contentType = MediaType.APPLICATION_JSON
                content = req
            }
            .andExpect { status { isOk() } }
            .andDo { print() }
            .andReturn()

        val res = mapper.readValue(
            mvcResult.response.contentAsString,
            Map::class.java
        )
        val id = res["id"] as Int
        val found = categoryQueryService.getCategoryById(id)

        // Then
        assertThat(id).isEqualTo(initialId)
        assertThat(found.name).isEqualTo(UPDATE_NAME)
        assertThat(found.orderIdx).isEqualTo(UPDATE_ORDER_IDX)
        assertThat(found.type).isEqualTo(UPDATE_ITEM_TYPE)
        assertThat(found.description1).isEqualTo(UPDATE_DESCRIPTION1)
        assertThat(found.description2).isEqualTo(UPDATE_DESCRIPTION2)
    }

    @DisplayName("integration: 마일리지 카테고리 삭제")
    @Test
    fun mileageCategoryIntegrationTests_192() {
        // Given
        val form = CategoryForm(
            title = NAME,
            orderIdx = ORDER_IDX,
            type = ITEM_TYPE,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2,
            categoryMaxPoints = 20f
        )
        val initialId = categoryService.saveCategory(form)

        // When
        val mvcResult = mockMvc
            .delete("/api/mileage/categories/$initialId")
            .andExpect { status { isOk() } }
            .andDo { print() }
            .andReturn()

        val res = mapper.readValue(
            mvcResult.response.contentAsString,
            Map::class.java
        )

        // Then
        assertThat(res["id"]).isEqualTo(initialId)
    }
}
