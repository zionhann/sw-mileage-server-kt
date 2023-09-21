package edu.handong.cseemileage.controller

import edu.handong.cseemileage.dto.account.student.StudentDto
import edu.handong.cseemileage.dto.account.student.StudentForm
import edu.handong.cseemileage.exception.account.student.StudentCannotDeleteException
import edu.handong.cseemileage.service.student.StudentQueryService
import edu.handong.cseemileage.service.student.StudentService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/mileage/students")
@CrossOrigin(origins = ["*"])
class StudentController(
    val studentService: StudentService,
    val studentQueryService: StudentQueryService
) {

    /**
     * 학생 등록
     */
    @PostMapping
    fun registerStudent(
        @RequestBody @Valid
        form: StudentForm
    ): ResponseEntity<Map<String, Int>> {
        val registeredId = studentService.register(form)
        return ResponseEntity.created(
            URI.create("/api/mileage/students/$registeredId")
        ).body(mapOf("id" to registeredId))
    }

    /**
     * 학생 목록 조회
     */
    @GetMapping
    fun getStudents(): ResponseEntity<StudentDto> {
        val students = studentQueryService.getStudents()
        return ResponseEntity.ok(
            StudentDto(
                list = students,
                count = students.size,
                description = "학생 전체 조회"
            )
        )
    }

    @PatchMapping("/{studentId}")
    fun modifyStudent(
        @PathVariable studentId: Int,
        @RequestBody @Valid
        form: StudentForm
    ): ResponseEntity<Map<String, Int>> {
        val modifiedId = studentService.modifyStudent(studentId, form)
        return ResponseEntity.ok(mapOf("id" to modifiedId))
    }

    @DeleteMapping("/{studentId}")
    fun deleteStudent(
        @PathVariable
        studentId: Int
    ): ResponseEntity<Map<String, Int>> {
        try {
            val removedId = studentService.deleteStudent(studentId)
            return ResponseEntity.ok(mapOf("id" to removedId))
        } catch (e: DataIntegrityViolationException) {
            throw StudentCannotDeleteException()
        }
    }
}
