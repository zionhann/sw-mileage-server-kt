package edu.handong.cseemileage.student.service

import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.DEPARTMENT
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.IS_CHECKED
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.MAJOR1
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.MAJOR2
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.NAME
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.SEMESTER_COUNT
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.SID
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.UPDATE_DEPARTMENT
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.UPDATE_IS_CHECKED
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.UPDATE_MAJOR1
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.UPDATE_MAJOR2
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.UPDATE_NAME
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.UPDATE_SEMESTER_COUNT
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.UPDATE_SID
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.UPDATE_YEAR
import edu.handong.cseemileage.mileage.item.repository.StudentRepositoryTests.Companion.YEAR
import edu.handong.cseemileage.student.domain.Student
import edu.handong.cseemileage.student.dto.StudentForm
import edu.handong.cseemileage.student.repository.StudentRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

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
        val student = createDefaultStudent()
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
        val student = createDefaultStudent()
        val updateForm = StudentForm(
            name = null,
            sid = null,
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

    companion object {
        fun createDefaultStudent(): Student {
            return Student().apply {
                name = NAME
                sid = SID
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
