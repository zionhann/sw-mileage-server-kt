package edu.handong.cseemileage.domain.acount

import edu.handong.cseemileage.domain.BaseEntity
import edu.handong.cseemileage.dto.account.student.StudentForm
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
class Student : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 11)
    var id: Int? = null

    @Column(name = "name", length = 20)
    var name: String? = null

    @Column(name = "sid", length = 12)
    var sid: String? = null

    @Column(name = "school", length = 40)
    var department: String? = null

    @Column(name = "major_1", length = 60)
    var major1: String? = null

    @Column(name = "major_2", length = 60)
    var major2: String? = null

    @ColumnDefault("0")
    @Column(name = "grade_level", columnDefinition = "tinyint(2)")
    var year: Int? = 0

    @ColumnDefault("0")
    @Column(name = "semester_count", columnDefinition = "tinyint(2)")
    var semesterCount: Int? = 0

    @ColumnDefault("0")
    @Column(name = "login_count", columnDefinition = "smallint(6)")
    var loginCount: Int? = 0

    @CreationTimestamp
    @Column(name = "last_login_date", nullable = false, columnDefinition = "datetime")
    var lastLoginDate: LocalDateTime = LocalDateTime.now()

    @ColumnDefault("0")
    @Column(name = "is_checked", length = 11)
    var isChecked: Boolean = false

    companion object {
        fun createStudent(form: StudentForm): Student {
            val student = Student().apply {
                name = form.name
                sid = form.sid
                department = form.department
                major1 = form.major1
                major2 = form.major2
                year = form.year
                semesterCount = form.semesterCount
                isChecked = form.isChecked ?: false
            }
            return student
        }
    }

    fun update(form: StudentForm): Int {
        this.apply {
            name = form.name ?: name
            sid = form.sid ?: sid
            department = form.department ?: department
            major1 = form.major1 ?: major1
            major2 = form.major2 ?: major2
            year = form.year ?: year
            semesterCount = form.semesterCount ?: semesterCount
            isChecked = form.isChecked ?: isChecked
        }
        return id!!
    }
}
