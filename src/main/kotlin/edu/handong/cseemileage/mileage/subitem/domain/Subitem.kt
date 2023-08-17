package edu.handong.cseemileage.mileage.subitem.domain

import edu.handong.cseemileage.mileage.category.domain.Category
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.jetbrains.annotations.NotNull
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

/**
 * @NotNull 붙이는 기준(& 조건)
 * - nullable = false
 * - default 값이 없는 경우
 *
 * 제외 조건
 * - PK
 * */
@Entity
@Table(name = "_sw_mileage_subitem")
class Subitem(
    @field: NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: Category? = null,

    @field: NotNull
    @Column(name = "subitem_name", nullable = false, length = 30)
    var subitemName: String? = null,

    @ColumnDefault("0")
    @Column(name = "weight", nullable = false)
    var weight: Float? = null,

    @ColumnDefault("0")
    @Column(columnDefinition = "tinyint(1)", nullable = false, name = "isPortfolio")
    var isPortfolio: Int? = null,

    @ColumnDefault("0")
    @Column(name = "maxpoint", nullable = false)
    var maxPoint: Float? = null,

    @Column(name = "description1", length = 300)
    var description1: String? = null,

    @Column(name = "description2", length = 300)
    var description2: String? = null,

    @field: NotNull
    @Column(name = "semester", columnDefinition = "char(7)", nullable = false)
    var semester: String? = null,

    @Column(name = "stu_type", length = 3)
    var stuType: String? = null
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
}
