package edu.handong.cseemileage.service

import edu.handong.cseemileage.controller.StudentIntegrationTests.Companion.DEPARTMENT
import edu.handong.cseemileage.controller.StudentIntegrationTests.Companion.IS_CHECKED
import edu.handong.cseemileage.controller.StudentIntegrationTests.Companion.MAJOR1
import edu.handong.cseemileage.controller.StudentIntegrationTests.Companion.MAJOR2
import edu.handong.cseemileage.controller.StudentIntegrationTests.Companion.NAME
import edu.handong.cseemileage.controller.StudentIntegrationTests.Companion.SEMESTER_COUNT
import edu.handong.cseemileage.controller.StudentIntegrationTests.Companion.SID
import edu.handong.cseemileage.controller.StudentIntegrationTests.Companion.UPDATE_DEPARTMENT
import edu.handong.cseemileage.controller.StudentIntegrationTests.Companion.UPDATE_IS_CHECKED
import edu.handong.cseemileage.controller.StudentIntegrationTests.Companion.UPDATE_MAJOR1
import edu.handong.cseemileage.controller.StudentIntegrationTests.Companion.UPDATE_MAJOR2
import edu.handong.cseemileage.controller.StudentIntegrationTests.Companion.UPDATE_NAME
import edu.handong.cseemileage.controller.StudentIntegrationTests.Companion.UPDATE_SEMESTER_COUNT
import edu.handong.cseemileage.controller.StudentIntegrationTests.Companion.UPDATE_SID
import edu.handong.cseemileage.controller.StudentIntegrationTests.Companion.UPDATE_YEAR
import edu.handong.cseemileage.controller.StudentIntegrationTests.Companion.YEAR
import edu.handong.cseemileage.domain.acount.Student
import edu.handong.cseemileage.dto.account.student.StudentForm
import edu.handong.cseemileage.exception.account.student.DuplicateStudentException
import edu.handong.cseemileage.repository.account.StudentRepository
import edu.handong.cseemileage.service.student.StudentService
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import org.testng.Assert.assertThrows

@Transactional
@SpringBootTest
class StudentServiceTests @Autowired constructor(
    val studentService: StudentService,
    val studentRepository: StudentRepository
) {

    @DisplayName("service: 학생 수정")
    @Test
    fun studentServiceTests_43() {
        // Given
        val student = createDefaultStudent(SID)
        val updateForm = StudentForm(
            name = UPDATE_NAME,
            sid = UPDATE_SID,
            department = UPDATE_DEPARTMENT,
            major1 = UPDATE_MAJOR1,
            major2 = UPDATE_MAJOR2,
            year = UPDATE_YEAR,
            semesterCount = UPDATE_SEMESTER_COUNT,
            isChecked = UPDATE_IS_CHECKED
        )
        studentRepository.save(student)

        // When
        student.id?.let { studentService.modifyStudent(it, updateForm) }
        val updatedStudent = studentRepository.findById(student.id).get()

        // Then
        assertThat(updatedStudent.name).isEqualTo(UPDATE_NAME)
        assertThat(updatedStudent.sid).isEqualTo(UPDATE_SID)
        assertThat(updatedStudent.department).isEqualTo(UPDATE_DEPARTMENT)
        assertThat(updatedStudent.major1).isEqualTo(UPDATE_MAJOR1)
        assertThat(updatedStudent.major2).isEqualTo(UPDATE_MAJOR2)
        assertThat(updatedStudent.year).isEqualTo(UPDATE_YEAR)
        assertThat(updatedStudent.semesterCount).isEqualTo(UPDATE_SEMESTER_COUNT)
        assertThat(updatedStudent.isChecked).isEqualTo(UPDATE_IS_CHECKED)
    }

    @DisplayName("service: 학생 수정 - 존재하지 않는 값은 원래의 값")
    @Test
    fun studentServiceTests_69() {
        // Given
        val student = createDefaultStudent(SID)
        val updateForm = StudentForm(
            name = null,
            sid = SID,
            department = null,
            major1 = null,
            major2 = null,
            year = null,
            semesterCount = null,
            isChecked = null
        )
        studentRepository.save(student)

        // When
        student.id?.let { studentService.modifyStudent(it, updateForm) }
        val updatedStudent = studentRepository.findById(student.id).get()

        // Then
        assertThat(updatedStudent.name).isEqualTo(NAME)
        assertThat(updatedStudent.sid).isEqualTo(SID)
        assertThat(updatedStudent.department).isEqualTo(DEPARTMENT)
        assertThat(updatedStudent.major1).isEqualTo(MAJOR1)
        assertThat(updatedStudent.major2).isEqualTo(MAJOR2)
        assertThat(updatedStudent.year).isEqualTo(YEAR)
        assertThat(updatedStudent.semesterCount).isEqualTo(SEMESTER_COUNT)
        assertThat(updatedStudent.isChecked).isEqualTo(IS_CHECKED)
    }

    @DisplayName("service: 중복 sid 저장 불가")
    @Test
    fun StudentServiceTests_102() {
        // Given
        val form = StudentForm(
            name = NAME,
            sid = SID,
            department = DEPARTMENT,
            major1 = MAJOR1,
            major2 = MAJOR2,
            year = YEAR,
            semesterCount = SEMESTER_COUNT,
            isChecked = IS_CHECKED
        )

        // Then
        assertThrows(DuplicateStudentException::class.java) {
            // When
            studentService.register(form)
            studentService.register(form)
        }
    }

    @DisplayName("학생 수정 시 sid 중복 불가")
    @Test
    fun studentServiceTests_127() {
        // Given
        val student = createDefaultStudent(SID)
        val student2 = createDefaultStudent("00001111")
        val updateForm = StudentForm(
            name = UPDATE_NAME,
            sid = "00001111",
            department = UPDATE_DEPARTMENT,
            major1 = UPDATE_MAJOR1,
            major2 = UPDATE_MAJOR2,
            year = UPDATE_YEAR,
            semesterCount = UPDATE_SEMESTER_COUNT,
            isChecked = UPDATE_IS_CHECKED
        )
        studentRepository.save(student)
        studentRepository.save(student2)

        // When
        assertThrows(DuplicateStudentException::class.java) {
            studentService.modifyStudent(student.id!!, updateForm)
        }
    }

    @DisplayName("sid 수정 안할 시 중복 검사 안함")
    @Test
    fun studentServiceTests_152() {
        // Given
        val student = createDefaultStudent(SID)
        val updateForm = StudentForm(
            name = UPDATE_NAME,
            sid = SID,
            department = UPDATE_DEPARTMENT,
            major1 = UPDATE_MAJOR1,
            major2 = UPDATE_MAJOR2,
            year = UPDATE_YEAR,
            semesterCount = UPDATE_SEMESTER_COUNT,
            isChecked = UPDATE_IS_CHECKED
        )
        studentRepository.save(student)

        // When
        try {
            studentService.modifyStudent(student.id!!, updateForm)
        } catch (e: DuplicateStudentException) {
            e.printStackTrace()
            fail("An exception is " + e.message)
        }
    }

    companion object {
        fun createDefaultStudent(studentId: String): Student {
            return Student().apply {
                name = NAME
                sid = studentId
                department = DEPARTMENT
                major1 = MAJOR1
                major2 = MAJOR2
                year = YEAR
                semesterCount = SEMESTER_COUNT
                isChecked = IS_CHECKED
            }
        }
    }
}
