package edu.handong.cseemileage.service.student

import edu.handong.cseemileage.dto.account.student.StudentDto
import edu.handong.cseemileage.repository.account.StudentRepository
import org.springframework.stereotype.Service

@Service
class StudentQueryService(
    val studentRepository: StudentRepository
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
                    isChecked = it.isChecked,
                    modDate = it.modDate
                )
            }
    }
}
