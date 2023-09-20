package edu.handong.cseemileage.apply.service

import edu.handong.cseemileage.apply.domain.Apply
import edu.handong.cseemileage.apply.dto.ApplyDto
import edu.handong.cseemileage.apply.exception.ApplyNotFoundException
import edu.handong.cseemileage.apply.repository.ApplyRepository
import edu.handong.cseemileage.student.dto.StudentDto
import edu.handong.cseemileage.student.exception.StudentNotFoundException
import edu.handong.cseemileage.student.repository.StudentRepository
import edu.handong.cseemileage.utils.Utils.Companion.stringToBoolean
import org.springframework.stereotype.Service

@Service
class ApplyQueryService(
    val repository: ApplyRepository,
    val studentRepository: StudentRepository
) {
    fun getApplies(): List<ApplyDto.Info> {
        val applies = repository.findAll()
        return applies.map { apply ->
            createApplyDto(apply)
        }
    }

    fun getApply(applyId: Int): ApplyDto.Info {
        return createApplyDto(
            repository.findById(applyId)
                .orElseThrow { ApplyNotFoundException() }
        )
    }

    fun getAppliesBySid(sid: String): List<ApplyDto.Info> {
        return repository.findAllByStudent(
            studentRepository.findBySid(sid)
                .orElseThrow { StudentNotFoundException() }
        ).map { apply ->
            createApplyDto(apply)
        }
    }
    fun getAppliesBySemesterName(semesterName: String): List<ApplyDto.Info> {
        return repository.findAllBySemesterName(semesterName)
            .map { apply ->
                createApplyDto(apply)
            }
    }

    private fun createApplyDto(apply: Apply): ApplyDto.Info {
        return ApplyDto.Info(
            id = apply.id,
            semesterName = apply.semesterName,
            isApproved = stringToBoolean(apply.isApproved),
            student = StudentDto.Info(
                id = apply.student.id,
                name = apply.student.name,
                sid = apply.student.sid,
                department = apply.student.department,
                major1 = apply.student.major1,
                major2 = apply.student.major2,
                year = apply.student.year,
                semesterCount = apply.student.semesterCount,
                loginCount = apply.student.loginCount,
                lastLoginDate = apply.student.lastLoginDate,
                isChecked = apply.student.isChecked,
                modDate = apply.student.modDate

            ),
            modDate = apply.modDate
        )
    }
}
