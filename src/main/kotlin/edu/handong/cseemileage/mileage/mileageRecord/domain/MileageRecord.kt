package edu.handong.cseemileage.mileage.mileageRecord.domain

import edu.handong.cseemileage.BaseEntity
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
    var student: Student,

    @Column(name = "description1", length = 300)
    var description1: String? = null,

    @Column(name = "description2", length = 300)
    var description2: String? = null,

    var counts: Int = 1
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 11)
    var id: Int? = null

    // 새로 추가된 필드
    @ColumnDefault("1")
    @Column(name = "count")
    var count: Float = 1f

    @Column(name = "mileage_point", columnDefinition = "smallint")
    var mileagePoint: Int? = 0

    @Column(name = "extra_point", columnDefinition = "smallint")
    var extraPoint: Int? = 0
}
