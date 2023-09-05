package edu.handong.cseemileage.mileage.category.integration

import com.fasterxml.jackson.databind.ObjectMapper
import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.ExceptionResponse
import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.category.dto.CategoryForm
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.DESCRIPTION1
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.DESCRIPTION2
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.IS_MULTI
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.ITEM_TYPE
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.NAME
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.ORDER_IDX
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.UPDATE_DESCRIPTION1
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.UPDATE_DESCRIPTION2
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.UPDATE_IS_MULTI
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.UPDATE_ITEM_TYPE
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.UPDATE_NAME
import edu.handong.cseemileage.mileage.category.repository.MileageCategoryRepositoryTests.Companion.UPDATE_ORDER_IDX
import edu.handong.cseemileage.mileage.category.service.CategoryQueryService
import edu.handong.cseemileage.mileage.category.service.CategoryService
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

    @DisplayName("integration: 마일리지 카테고리 생성")
    @Test
    fun mileageCategoryIntegrationTests_31() {
        // Given
        val form = CategoryForm(
            title = NAME,
            orderIdx = ORDER_IDX,
            itemType = ITEM_TYPE,
            isMulti = IS_MULTI,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2
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
        assertThat(res.message).isEqualTo(ExceptionMessage.CATEGORY_INVALID_POINTS)
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
        assertThat(res.message).isEqualTo(ExceptionMessage.CATEGORY_POINTS_IS_EMPTY)
    }

    @DisplayName("service: 제목이 없는 경우")
    @Test
    fun mileageCategoryIntegrationTests_107() {
        // Given
        val form = CategoryForm(
            title = null,
            orderIdx = ORDER_IDX,
            itemType = ITEM_TYPE,
            isMulti = IS_MULTI,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2
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
        assertThat(res.message).isEqualTo(ExceptionMessage.CATEGORY_TITLE_IS_EMPTY)
    }

    @DisplayName("service: 마일리지 카테고리 목록 조회")
    @Test
    fun mileageCategoryIntegrationTests_132() {
        // Given
        val form1 = CategoryForm(
            title = NAME,
            orderIdx = ORDER_IDX,
            itemType = ITEM_TYPE,
            isMulti = IS_MULTI,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2
        )
        val form2 = CategoryForm(
            title = "category2",
            orderIdx = ORDER_IDX,
            itemType = ITEM_TYPE,
            isMulti = IS_MULTI,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2
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
            itemType = ITEM_TYPE,
            isMulti = IS_MULTI,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2
        )
        val initialId = categoryService.saveCategory(form)

        val updateForm = CategoryForm(
            title = UPDATE_NAME,
            orderIdx = UPDATE_ORDER_IDX,
            itemType = UPDATE_ITEM_TYPE,
            isMulti = UPDATE_IS_MULTI,
            description1 = UPDATE_DESCRIPTION1,
            description2 = UPDATE_DESCRIPTION2
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
        assertThat(found.itemType).isEqualTo(UPDATE_ITEM_TYPE)
        assertThat(found.isMulti).isEqualTo(UPDATE_IS_MULTI)
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
            itemType = ITEM_TYPE,
            isMulti = IS_MULTI,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2
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
