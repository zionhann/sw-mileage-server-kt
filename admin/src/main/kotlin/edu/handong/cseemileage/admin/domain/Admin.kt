package edu.handong.cseemileage.admin.domain

import edu.handong.cseemileage.BaseEntity
import edu.handong.cseemileage.admin.dto.AdminForm
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
@Table(name = "_sw_manager_google")
class Admin(
    @Column(name = "name", nullable = false, length = 20)
    var name: String,

    @Column(name = "email", nullable = false, length = 30)
    var email: String

) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 11)
    var id: Int? = null

    @ColumnDefault("0")
    @Column(name = "level", columnDefinition = "tinyint(4)")
    var level: Int? = 0

    @ColumnDefault("0")
    @Column(name = "login_count", length = 11)
    var loginCount: Int? = 0

    @CreationTimestamp
    @Column(name = "last_login_date", nullable = false, columnDefinition = "datetime")
    var lastLoginDate: LocalDateTime = LocalDateTime.now()

    companion object {
        fun createAdmin(form: AdminForm): Admin {
            val admin = Admin(
                name = form.name!!,
                email = form.email!!
            ).apply {
                level = form.level
            }
            return admin
        }
    }

    fun update(form: AdminForm): Int {
        this.apply {
            name = form.name ?: name
            email = form.email ?: email
            level = form.level ?: level
        }
        return id!!
    }
}
