package edu.handong.cseemileage.student.service

import edu.handong.cseemileage.student.dto.StudentDto
import edu.handong.cseemileage.student.repository.StudentRepository
import org.springframework.stereotype.Service

@Service
class StudentQueryService(
    val studentRepository: StudentRepository
) {
    fun getStudents(): List<StudentDto.Info> {
        return studentRepository
            .findAll()
            .map {
                StudentDto.Info(it)
            }
    }
}
