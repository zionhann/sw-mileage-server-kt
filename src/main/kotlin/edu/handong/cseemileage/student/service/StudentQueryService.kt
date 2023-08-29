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
                modelMapper.map(it, StudentDto.Info::class.java)
            }
    }
}
