package edu.handong.cseemileage.domain.mileage

import edu.handong.cseemileage.domain.BaseEntity
import edu.handong.cseemileage.domain.acount.Student
import edu.handong.cseemileage.dto.mileage.apply.ApplyForm
import edu.handong.cseemileage.utils.Utils.Companion.booleanToString
import edu.handong.cseemileage.utils.Utils.Companion.stringToBoolean
import org.hibernate.annotations.ColumnDefault
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "_sw_mileage_apply")
class Apply(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    var student: Student
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 11)
    var id: Int = 0

    @ColumnDefault("'2023-02'")
    @Column(name = "semester_name", nullable = false, columnDefinition = "char(7)")
    var semesterName: String = "2023-02"

    @ColumnDefault("'N'")
    @Column(name = "is_approved", nullable = false, columnDefinition = "char(1)")
    var isApproved: String = "N"

    companion object {
        fun createApply(form: ApplyForm, student: Student): Apply {
            val apply = Apply(
                student = student
            ).apply {
                semesterName = form.semesterName ?: "2023-02"
                isApproved = booleanToString(form.isApproved ?: false)
            }
            return apply
        }
    }

    fun update(form: ApplyForm, student: Student): Int {
        this.apply {
            this@Apply.student = student
            semesterName = form.semesterName ?: semesterName
            isApproved = booleanToString(form.isApproved ?: stringToBoolean(isApproved))
        }
        return id!!
    }
}
