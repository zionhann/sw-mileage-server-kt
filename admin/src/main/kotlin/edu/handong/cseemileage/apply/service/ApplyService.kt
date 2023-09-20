package edu.handong.cseemileage.apply.service

import edu.handong.cseemileage.apply.domain.Apply
import edu.handong.cseemileage.apply.dto.ApplyForm
import edu.handong.cseemileage.apply.exception.ApplyNotFoundException
import edu.handong.cseemileage.apply.exception.DuplicateApplyException
import edu.handong.cseemileage.apply.repository.ApplyRepository
import edu.handong.cseemileage.student.exception.StudentNotFoundException
import edu.handong.cseemileage.student.repository.StudentRepository
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
