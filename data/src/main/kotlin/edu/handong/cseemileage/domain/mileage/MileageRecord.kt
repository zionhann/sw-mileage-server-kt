package edu.handong.cseemileage.domain.mileage

import edu.handong.cseemileage.domain.BaseEntity
import edu.handong.cseemileage.dto.mileage.record.MileageRecordForm
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

    @Column(name = "name", length = 20)
    var name: String,

    @Column(name = "sid", length = 12)
    var sid: String
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
        fun createMileageRecord(form: MileageRecordForm, semesterItem: SemesterItem, name: String, sid: String): MileageRecord {
            val mileageRecord = MileageRecord(
                semesterItem = semesterItem,
                name = name,
                sid = sid
            ).apply {
                counts = form.counts
                extraPoints = form.extraPoints
                description1 = form.description1
                description2 = form.description2
            }
            semesterItem.addRecord(mileageRecord)
            return mileageRecord
        }
    }

    fun update(form: MileageRecordForm, semesterItem: SemesterItem, name: String, sid: String): Int {
        this.apply {
            this@MileageRecord.semesterItem = semesterItem
            this@MileageRecord.name = name
            this@MileageRecord.sid = sid
            counts = form.counts ?: counts
            extraPoints = form.extraPoints ?: extraPoints
            description1 = form.description1 ?: description1
            description2 = form.description2 ?: description2
        }
        return id!!
    }
}
