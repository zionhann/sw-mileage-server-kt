package edu.handong.cseemileage.student.service

import edu.handong.cseemileage.mileage.category.exception.CategoryNotFoundException
import edu.handong.cseemileage.student.domain.Student
import edu.handong.cseemileage.student.dto.StudentForm
import edu.handong.cseemileage.student.exception.DuplicateStudentException
import edu.handong.cseemileage.student.exception.StudentNotFoundException
import edu.handong.cseemileage.student.repository.StudentRepository
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
