package edu.handong.cseemileage.mileage.mileageRecord.domain

import edu.handong.cseemileage.BaseEntity
import edu.handong.cseemileage.mileage.mileageRecord.dto.MileageRecordForm
import edu.handong.cseemileage.mileage.semesterItem.domain.SemesterItem
import edu.handong.cseemileage.student.domain.Student
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
@Table(name = "_sw_mileage_record")
class MileageRecord(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_item_id", nullable = false)
    var semesterItem: SemesterItem,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    var student: Student
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 11)
    var id: Int = 0

    // 새로 추가된 필드
    @ColumnDefault("1")
    @Column(name = "count")
    var counts: Float? = 1f

    @Column(name = "mileage_point", columnDefinition = "smallint")
    var points: Int? = null

    @Column(name = "extra_point", columnDefinition = "smallint")
    var extraPoints: Int? = null

    @Column(name = "description1", length = 300)
    var description1: String? = null

    @Column(name = "description2", length = 300)
    var description2: String? = null

    companion object {
        fun createMileageRecord(form: MileageRecordForm, semesterItem: SemesterItem, student: Student): MileageRecord {
            val mileageRecord = MileageRecord(
                semesterItem = semesterItem,
                student = student
            ).apply {
                counts = form.counts
                points = form.points
                extraPoints = form.extraPoints
                description1 = form.description1
                description2 = form.description2
            }
            semesterItem.addRecord(mileageRecord)
            student.addRecord(mileageRecord)
            return mileageRecord
        }
    }

    fun update(form: MileageRecordForm, semesterItem: SemesterItem, student: Student): Int {
        this.apply {
            this@MileageRecord.semesterItem = semesterItem
            this@MileageRecord.student = student
            counts = form.counts ?: counts
            points = form.points ?: points
            extraPoints = form.extraPoints ?: extraPoints
            description1 = form.description1 ?: description1
            description2 = form.description2 ?: description2
        }
        return id!!
    }
}
