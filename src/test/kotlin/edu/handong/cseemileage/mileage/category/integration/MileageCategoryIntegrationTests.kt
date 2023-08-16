package edu.handong.cseemileage.mileage.category.integration

import com.fasterxml.jackson.databind.ObjectMapper
import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.ExceptionResponse
import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.category.dto.CategoryForm
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
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class MileageCategoryIntegrationTests @Autowired constructor(
    val restTemplate: TestRestTemplate,
    val mockMvc: MockMvc,
    val mapper: ObjectMapper,
    val categoryService: CategoryService
) {

    @DisplayName("integration: 마일리지 카테고리 생성")
    @Test
    fun mileageCategoryIntegrationTests_31() {
        // Given
        val form = CategoryForm("전공 마일리지", "-", 0)
        val req = mapper.writeValueAsString(form)

        // When
        val mvcResult = mockMvc
            .post("/api/mileage/categories") {
                contentType = MediaType.APPLICATION_JSON
                content = req
            }
            .andExpect { status { isOk() } }
            .andDo { print() }
            .andReturn()

        val res = mapper.readValue(
            mvcResult.response.contentAsString,
            CategoryDto.Info::class.java
        )

        // Then
        assertThat(res.id).isNotNull
        assertThat(res.title).isEqualTo(form.title)
    }

    @DisplayName("service: 마일리지 값이 음수인 경우")
    @Test
    fun mileageCategoryIntegrationTests_54() {
        // Given
        val form = CategoryForm("전공 마일리지", "-", -1)

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
        assertThat(res.status).isEqualTo(HttpStatus.BAD_REQUEST.reasonPhrase)
        assertThat(res.message).isEqualTo(ExceptionMessage.CATEGORY_INVALID_POINTS)
    }

    @DisplayName("service: 마일리지 값이 없는 경우")
    @Test
    fun mileageCategoryIntegrationTests_82() {
        // Given
        val form = CategoryForm("전공 마일리지", "-", null)

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
        assertThat(res.status).isEqualTo(HttpStatus.BAD_REQUEST.reasonPhrase)
        assertThat(res.message).isEqualTo(ExceptionMessage.CATEGORY_POINTS_IS_EMPTY)
    }

    @DisplayName("service: 제목이 없는 경우")
    @Test
    fun mileageCategoryIntegrationTests_107() {
        // Given
        val form = CategoryForm(null, "-", 0)

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
        assertThat(res.status).isEqualTo(HttpStatus.BAD_REQUEST.reasonPhrase)
        assertThat(res.message).isEqualTo(ExceptionMessage.CATEGORY_TITLE_IS_EMPTY)
    }

    @DisplayName("service: 마일리지 카테고리 목록 조회")
    @Test
    fun mileageCategoryIntegrationTests_132() {
        // Given
        val form1 = CategoryForm(
            title = "전공 마일리지",
            description = "-",
            maxPoints = 0
        )
        val form2 = CategoryForm(
            title = "동아리 마일리지",
            description = "-",
            maxPoints = 0
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
        assertThat(res.categories).hasSize(2)
    }
}
