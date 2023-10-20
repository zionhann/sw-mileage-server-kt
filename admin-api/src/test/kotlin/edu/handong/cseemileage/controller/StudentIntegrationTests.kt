package edu.handong.cseemileage.controller

import TestSecurityConfig
import com.fasterxml.jackson.databind.ObjectMapper
import edu.handong.cseemileage.dto.account.student.StudentForm
import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.ExceptionResponse
import edu.handong.cseemileage.repository.mileage.CategoryRepository
import edu.handong.cseemileage.util.ExtractExceptionMessage.Companion.getExceptionMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@Import(TestSecurityConfig::class)
class StudentIntegrationTests @Autowired constructor(
    val mapper: ObjectMapper,
    val mockMvc: MockMvc,
    val categoryRepository: CategoryRepository
) {

    companion object {
        const val NAME = "홍길동"
        const val SID = "22000680"
        const val DEPARTMENT = "전산전자공학부"
        const val MAJOR1 = "컴퓨터공학"
        const val MAJOR2 = "경영학"
        const val YEAR = 3
        const val SEMESTER_COUNT = 6
        const val IS_CHECKED = true

        const val UPDATE_NAME = "장길동"
        const val UPDATE_SID = "22000314"
        const val UPDATE_DEPARTMENT = "경영경제학부"
        const val UPDATE_MAJOR1 = "경영학"
        const val UPDATE_MAJOR2 = "경제학"
        const val UPDATE_YEAR = 4
        const val UPDATE_SEMESTER_COUNT = 7
        const val UPDATE_IS_CHECKED = false

        const val DEFAULT_YEAR = 0
        const val DEFAULT_SEMESTER_COUNT = 0
        const val DEFAULT_IS_CHECKED = false
    }

    @DisplayName("integration: 학생 생성 sid 필수 요청")
    @Test
    fun studentControllerTests_45() {
        // given
        val form = StudentForm(
            name = NAME,
            sid = null,
            department = DEPARTMENT,
            major1 = MAJOR1,
            major2 = MAJOR2,
            year = YEAR,
            semesterCount = SEMESTER_COUNT,
            isChecked = IS_CHECKED
        )
        val request = mapper.writeValueAsString(form)

        // when
        val mvcResult = mockMvc
            .post("/api/mileage/students") {
                contentType = MediaType.APPLICATION_JSON
                content = request
            }
            .andExpect { status { isBadRequest() } }
            .andDo { print() }
            .andReturn()

        val res = mapper.readValue(
            mvcResult.response.contentAsString,
            ExceptionResponse::class.java
        )

        assertThat(res.status).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(getExceptionMessage(mvcResult)).isEqualTo(ExceptionMessage.STUDENT_SID_IS_EMPTY)
    }
}
