package edu.handong.cseemileage.mileage.category.domain

import edu.handong.cseemileage.mileage.category.dto.CategoryForm
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "_sw_mileage_category")
class Category(
    @NotNull
    @Column(name = "name", nullable = false, length = 20)
    var name: String? = null,

    @Column(name = "description", length = 300)
    var description: String? = null,

    @NotNull
    @Column(name = "max_points", nullable = false, length = 11)
    var maxPoints: Int? = 0
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 11)
    var id: Int? = null

    @UpdateTimestamp
    @Column(columnDefinition = "timestamp", name = "mod_date")
    var modDate: LocalDateTime? = null

    @CreationTimestamp
    @Column(columnDefinition = "timestamp", name = "reg_date")
    var regDate: LocalDateTime? = null

    fun update(form: CategoryForm): Int {
        this.name = form.title
        this.description = form.description
        this.maxPoints = form.maxPoints

        return id!!
    }
}
