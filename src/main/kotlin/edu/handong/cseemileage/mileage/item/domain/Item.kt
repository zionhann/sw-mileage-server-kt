package edu.handong.cseemileage.mileage.item.domain

import edu.handong.cseemileage.mileage.category.domain.Category
import edu.handong.cseemileage.mileage.item.dto.ItemForm
import edu.handong.cseemileage.mileage.semester.domain.Semester
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
import javax.persistence.OneToMany
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
class Item(
    @field: NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: Category,

    @field: NotNull
    @Column(name = "name", nullable = false, length = 30)
    var name: String,

    @ColumnDefault("0")
    @Column(columnDefinition = "tinyint(1)", nullable = false, name = "is_portfolio")
    var isPortfolio: Int,

    @Column(name = "description1", length = 300)
    var description1: String? = null,

    @Column(name = "description2", length = 300)
    var description2: String,

    @Column(name = "stu_type", length = 3)
    var stuType: String,

    @OneToMany(mappedBy = "item")
    var semesterItems: MutableList<Semester> = mutableListOf()
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

    companion object {
        fun createItem(form: ItemForm, category: Category): Item {
            var item = Item(
                category = category,
                name = form.itemName,
                isPortfolio = form.isPortfolio,
                description1 = form.description1,
                description2 = form.description2,
                stuType = form.stuType
            )
            category.addItem(item)
            return item
        }
    }

    /**
     * 양방향 매핑 - 연관관계 편의 메소드
     * */
    fun addSemesterItem(semester: Semester) {
        semesterItems.add(semester)
    }
}
