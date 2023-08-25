package edu.handong.cseemileage.student.domain

import edu.handong.cseemileage.BaseEntity
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "_sw_student")
class Student(
    @Column(name = "name", length = 20)
    var name: String?,

    @Column(name = "sid", length = 12)
    var sid: String?,

    @Column(name = "school", length = 40)
    var department: String?,

    @Column(name = "major_1", length = 60)
    var major1: String?,

    @Column(name = "major_2", length = 60)
    var major2: String?,

    @ColumnDefault("0")
    @Column(name = "grade_level", columnDefinition = "tinyint(2)")
    var year: Int?,

    @ColumnDefault("0")
    @Column(name = "semester_count", columnDefinition = "tinyint(2)")
    var semesterCount: Int?
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 11)
    var id: Int? = null

    @ColumnDefault("0")
    @Column(name = "login_count", columnDefinition = "smallint(6)")
    var loginCount: Int = 1

    @CreationTimestamp
    @Column(name = "last_login_date", nullable = false, columnDefinition = "datetime")
    var lastLoginDate: LocalDateTime = LocalDateTime.now()

    // 새로 추가된 필드
    @ColumnDefault("0")
    @Column(name = "is_checked", length = 11)
    var isChecked: Int = 0
}
