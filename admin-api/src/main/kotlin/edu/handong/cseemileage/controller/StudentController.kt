package edu.handong.cseemileage.controller

import edu.handong.cseemileage.dto.account.student.StudentDto
import edu.handong.cseemileage.dto.account.student.StudentForm
import edu.handong.cseemileage.exception.account.student.StudentCannotDeleteException
import edu.handong.cseemileage.service.student.StudentQueryService
import edu.handong.cseemileage.service.student.StudentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "학생 API")
@SecurityRequirement(name = "Bearer Authentication")
class StudentController(
    val studentService: StudentService,
    val studentQueryService: StudentQueryService
) {

    @Operation(summary = "학생 회원가입", description = "학생 사이트에서 최초 로그인하는 사용자에 대해 학생 계정을 생성합니다. 학생의 sid은 중복 불가합니다. 필수 요청 필드인 sid를 제외한 모든 필드는 생략 가능합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "학생 회원가입 성공"),
            ApiResponse(responseCode = "400", description = "sid 누락, sid 형식 오류(8자리 숫자), year/semesterCount 범위 초과 등 학생 회원가입 실패"),
            ApiResponse(responseCode = "500", description = "학생 sid 중복으로 인해 회원가입 실패")
        ]
    )
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

    @Operation(summary = "학생 목록 전체 조회", description = "현재 가입되어 있는 학생의 목록을 전체 조회합니다.")
    @ApiResponse(responseCode = "200", description = "학생 목록 전체 조회 성공")
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

    @Operation(summary = "학생 수정 by studentId(학생 PK)", description = "한 명의 학생 정보를 수정합니다. 필수 요청 필드인 sid를 제외한 모든 필드는 생략 가능합니다. 수정이 필요한 필드만 요청하세요.")
    @Parameter(name = "studentId", description = "학생 PK", required = true, example = "1")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "학생 수정 성공"),
            ApiResponse(responseCode = "400", description = "sid 누락, sid 형식 오류(8자리 숫자), year/semesterCount 범위 초과 등 학생 회원정보 수정 실패"),
            ApiResponse(responseCode = "500", description = "학생 sid 중복으로 인해 학생 수정 실패")
        ]
    )
    @PatchMapping("/{studentId}")
    fun modifyStudent(
        @PathVariable studentId: Int,
        @RequestBody @Valid
        form: StudentForm
    ): ResponseEntity<Map<String, Int>> {
        val modifiedId = studentService.modifyStudent(studentId, form)
        return ResponseEntity.ok(mapOf("id" to modifiedId))
    }

    @Operation(summary = "학생 탈퇴 by studentId(학생 PK)", description = "학생 계정을 삭제(탈퇴) 합니다.")
    @Parameter(name = "studentId", description = "학생 PK", required = true, example = "1")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "학생 삭제 성공"),
            ApiResponse(responseCode = "500", description = "학생 삭제 실패 - studentId에 해당하는 학생이 존재하지 않음")
        ]
    )
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
