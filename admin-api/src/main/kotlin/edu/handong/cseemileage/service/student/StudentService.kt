package edu.handong.cseemileage.service.student

import edu.handong.cseemileage.domain.acount.Student
import edu.handong.cseemileage.dto.account.student.StudentForm
import edu.handong.cseemileage.exception.account.student.DuplicateStudentException
import edu.handong.cseemileage.exception.account.student.StudentNotFoundException
import edu.handong.cseemileage.exception.mileage.category.CategoryNotFoundException
import edu.handong.cseemileage.repository.account.StudentRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class StudentService(
    val studentRepository: StudentRepository
) {
    fun register(form: StudentForm): Int {
        validateDuplicateStudent(form.sid!!, 0)
        val student = Student.createStudent(form)
        val saved = studentRepository.save(student)
        return saved.id!!
    }

    private fun validateDuplicateStudent(sid: String, id: Int) {
        if (id > 0) {
            val findStudents = studentRepository.findById(id).orElseThrow { StudentNotFoundException() }
            if (findStudents.sid == sid) {
                return
            }
        }
        val findStudents = studentRepository.findBySid(sid)
        if (findStudents.isPresent) {
            throw DuplicateStudentException()
        }
    }

    fun modifyStudent(id: Int, form: StudentForm): Int {
        validateDuplicateStudent(form.sid!!, id)
        return studentRepository
            .findById(id)
            .orElseThrow { CategoryNotFoundException() }
            .update(form)
    }

    fun deleteStudent(studentId: Int): Int {
        studentRepository.deleteById(studentId)
        return studentId
    }
}
