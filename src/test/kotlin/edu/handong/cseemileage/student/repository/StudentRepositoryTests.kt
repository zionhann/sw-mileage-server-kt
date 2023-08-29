package edu.handong.cseemileage.mileage.item.repository

import edu.handong.cseemileage.student.domain.Student
import edu.handong.cseemileage.student.repository.StudentRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.transaction.annotation.Transactional

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class StudentRepositoryTests @Autowired constructor(
    private val studentRepository: StudentRepository
) {

    companion object {
        const val NAME = "홍길동"
        const val SID = "223000123"
        const val DEPARTMENT = "전산전자공학부"
        const val MAJOR1 = "컴퓨터공학"
        const val MAJOR2 = "경영학"
        const val YEAR = 3
        const val SEMESTER_COUNT = 6
        const val IS_CHECKED = true

        const val UPDATE_NAME = "장길동"
        const val UPDATE_SID = "223000321"
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

    @DisplayName("repository: 학생 persist")
    @Test
    fun StudentRepositoryTests_51() {
        // Given
        val student = createDefaultStudent()

        val savedStudent = studentRepository.save(student)

        Assertions.assertThat(savedStudent).isNotNull
        Assertions.assertThat(savedStudent).isEqualTo(student)
        Assertions.assertThat(savedStudent.name).isEqualTo(NAME)
        Assertions.assertThat(savedStudent.sid).isEqualTo(SID)
        Assertions.assertThat(savedStudent.department).isEqualTo(DEPARTMENT)
        Assertions.assertThat(savedStudent.major1).isEqualTo(MAJOR1)
        Assertions.assertThat(savedStudent.major2).isEqualTo(MAJOR2)
        Assertions.assertThat(savedStudent.year).isEqualTo(YEAR)
        Assertions.assertThat(savedStudent.semesterCount).isEqualTo(SEMESTER_COUNT)
        Assertions.assertThat(savedStudent.isChecked).isEqualTo(IS_CHECKED)
    }

    @DisplayName("모든 값을 지정하지 않아도 @ColumnDefault로 지정한 값으로 저장된다")
    @Test
    fun StudentRepositoryTests_82() {
        // Given
        val student = Student()

        val savedStudent = studentRepository.save(student)

        Assertions.assertThat(savedStudent).isNotNull
        Assertions.assertThat(savedStudent).isEqualTo(student)
        Assertions.assertThat(savedStudent.name).isNull()
        Assertions.assertThat(savedStudent.sid).isNull()
        Assertions.assertThat(savedStudent.department).isNull()
        Assertions.assertThat(savedStudent.major1).isNull()
        Assertions.assertThat(savedStudent.major2).isNull()
        Assertions.assertThat(savedStudent.year).isEqualTo(DEFAULT_YEAR)
        Assertions.assertThat(savedStudent.semesterCount).isEqualTo(DEFAULT_SEMESTER_COUNT)
        Assertions.assertThat(savedStudent.isChecked).isEqualTo(DEFAULT_IS_CHECKED)
    }

    @DisplayName("repository: 학생 전체 조회")
    @Test
    fun StudentRepositoryTests_103() {
        // Given
        val student1 = createDefaultStudent()
        val student2 = createDefaultStudent()

        // When
        studentRepository.save(student1)
        studentRepository.save(student2)
        val students = studentRepository.findAll()

        // Then
        Assertions.assertThat(students).isNotNull
        Assertions.assertThat(students).hasSize(2)
    }

    @DisplayName("repository: 학생 삭제")
    @Test
    fun StudentRepositoryTests_120() {
        // Given
        val student = createDefaultStudent()

        // When
        studentRepository.save(student)
        studentRepository.deleteById(student.id!!)
        val studentOptional = studentRepository.findById(student.id!!)

        // Then
        Assertions.assertThat(studentOptional).isEmpty
    }

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
