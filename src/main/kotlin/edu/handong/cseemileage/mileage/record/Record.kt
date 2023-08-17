package edu.handong.cseemileage.mileage.record

import edu.handong.cseemileage.mileage.semester.domain.Semester
import edu.handong.cseemileage.student.domain.Student
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
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
class Record(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id", nullable = false)
    var semester: Semester,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    var student: Student,

    @Column(name = "description1", length = 300)
    var description1: String?,

    @Column(name = "description2", length = 300)
    var description2: String?
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 11)
    var id: Int? = null

    @UpdateTimestamp
    @Column(name = "mod_date", columnDefinition = "timestamp")
    var modDate: LocalDateTime? = null

    @CreationTimestamp
    @Column(name = "reg_date", columnDefinition = "timestamp")
    var regDate: LocalDateTime? = null
}
