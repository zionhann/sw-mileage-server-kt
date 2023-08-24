package edu.handong.cseemileage.student.service

import edu.handong.cseemileage.student.domain.Student
import edu.handong.cseemileage.student.dto.StudentForm
import edu.handong.cseemileage.student.repository.StudentRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class StudentService(
    val studentRepository: StudentRepository
) {

    fun register(form: StudentForm): Int {
        val entity = Student(
            name = form.name,
            sid = form.sid,
            school = form.department,
            major1 = form.major1,
            major2 = form.major2,
            gradeLevel = form.year,
            semesterCount = form.semesterCount
        )
        val saved = studentRepository.save(entity)

        return saved.id!!
    }
}
