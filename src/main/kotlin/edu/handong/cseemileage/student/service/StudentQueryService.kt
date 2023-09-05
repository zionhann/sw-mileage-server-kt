package edu.handong.cseemileage.student.service

import edu.handong.cseemileage.student.dto.StudentDto
import edu.handong.cseemileage.student.repository.StudentRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class StudentQueryService(
    val studentRepository: StudentRepository,
    val modelMapper: ModelMapper
) {
    fun getStudents(): List<StudentDto.Info> {
        return studentRepository
            .findAll()
            .map {
                StudentDto.Info(
                    id = it.id,
                    name = it.name,
                    sid = it.sid,
                    department = it.department,
                    major1 = it.major1,
                    major2 = it.major2,
                    year = it.year,
                    semesterCount = it.semesterCount,
                    loginCount = it.loginCount,
                    lastLoginDate = it.lastLoginDate,
                    isChecked = it.isChecked
                )
            }
    }
}
