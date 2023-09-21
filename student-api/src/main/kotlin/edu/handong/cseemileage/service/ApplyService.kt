package edu.handong.cseemileage.service

import edu.handong.cseemileage.domain.mileage.Apply
import edu.handong.cseemileage.dto.mileage.apply.ApplyForm
import edu.handong.cseemileage.exception.account.student.StudentNotFoundException
import edu.handong.cseemileage.exception.mileage.apply.ApplyNotFoundException
import edu.handong.cseemileage.exception.mileage.apply.DuplicateApplyException
import edu.handong.cseemileage.repository.account.StudentRepository
import edu.handong.cseemileage.repository.mileage.ApplyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ApplyService(
    val repository: ApplyRepository,
    val studentRepository: StudentRepository
) {
    fun saveApply(form: ApplyForm): Int {
        validateDuplicateApply(form, 0)
        val student = form.studentId?.let {
            studentRepository
                .findById(it)
                .orElseThrow { StudentNotFoundException() }
        }
        val apply = Apply.createApply(form, student!!)
        repository.save(apply)
        return apply.id!!
    }

    private fun validateDuplicateApply(form: ApplyForm, applyId: Int) {
        val student = studentRepository.findById(form.studentId!!)
            .orElseThrow { StudentNotFoundException() }
        val foundApply = repository.findBySemesterNameAndStudent(form.semesterName ?: "2023-02", student)
        if (foundApply != null && foundApply.id != applyId) {
            throw DuplicateApplyException()
        }
    }

    fun modifyApply(applyId: Int, form: ApplyForm): Int {
        validateDuplicateApply(form, applyId)
        val student = form.studentId?.let {
            studentRepository
                .findById(it)
                .orElseThrow { StudentNotFoundException() }
        }
        return repository
            .findById(applyId)
            .orElseThrow { ApplyNotFoundException() }
            .update(form, student!!)
    }

    fun deleteApply(applyId: Int): Int {
        repository.deleteById(applyId)
        return applyId
    }
}
