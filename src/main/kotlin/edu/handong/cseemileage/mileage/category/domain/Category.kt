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
    @Column(name = "cname", nullable = false, length = 20)
    var cname: String? = null,

    @Column(name = "description1", length = 300)
    var description1: String? = null,

    @NotNull
    @Column(name = "maxpoint", nullable = false, length = 11)
    var maxPoint: Int? = null
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 11)
    var id: Int? = null

    @UpdateTimestamp
    @Column(columnDefinition = "timestamp", name = "moddate")
    var modDate: LocalDateTime? = null

    @CreationTimestamp
    @Column(columnDefinition = "timestamp", name = "regdate")
    var regDate: LocalDateTime? = null

    fun update(form: CategoryForm): Int {
        this.cname = form.title
        this.description1 = form.description
        this.maxPoint = form.maxPoints

        return id!!
    }
}
