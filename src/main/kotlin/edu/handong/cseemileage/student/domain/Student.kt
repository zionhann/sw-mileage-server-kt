package edu.handong.cseemileage.student.domain

import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
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

    @Column(name = "mobile", length = 20)
    var mobile: String?,

    @Column(name = "email", length = 50)
    var email: String?,

    @Column(name = "school", length = 40)
    var school: String?,

    @Column(name = "major1", length = 60)
    var major1: String?,

    @Column(name = "major2", length = 60)
    var major2: String?,

    @Column(name = "grade_level", columnDefinition = "tinyint(2)")
    var gradeLevel: Int?,

    @Column(name = "semester_count", columnDefinition = "tinyint(2)")
    var semesterCount: Int?
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 11)
    var id: Int? = null

    @ColumnDefault("0")
    @Column(name = "login_count", nullable = false, columnDefinition = "smallint(6)")
    var loginCount: Int = 1

    @Column(name = "last_login_date", columnDefinition = "timestamp", nullable = false)
    var lastLoginDate: LocalDateTime = LocalDateTime.now()

    @UpdateTimestamp
    @Column(name = "mod_date", columnDefinition = "timestamp")
    var modDate: LocalDateTime? = null

    @CreationTimestamp
    @Column(name = "reg_date", columnDefinition = "timestamp")
    var regDate: LocalDateTime? = null
}
