package edu.handong.cseemileage.student.integration

import com.fasterxml.jackson.databind.ObjectMapper
import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.ExceptionResponse
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.DEPARTMENT
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.IS_CHECKED
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.MAJOR1
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.MAJOR2
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.NAME
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.SEMESTER_COUNT
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.YEAR
import edu.handong.cseemileage.student.dto.StudentForm
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
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
class StudentIntegrationTests @Autowired constructor(
    val mapper: ObjectMapper,
    val mockMvc: MockMvc,
    val categoryRepository: CategoryRepository
) {
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
        assertThat(res.message).isEqualTo(ExceptionMessage.STUDENT_SID_IS_EMPTY)
    }
}
