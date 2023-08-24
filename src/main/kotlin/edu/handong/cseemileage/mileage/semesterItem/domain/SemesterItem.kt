package edu.handong.cseemileage.mileage.semesterItem.domain

import edu.handong.cseemileage.BaseEntity
import edu.handong.cseemileage.mileage.category.domain.Category
import edu.handong.cseemileage.mileage.item.domain.Item
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemForm
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
@Table(name = "_sw_mileage_semester_item")
class SemesterItem(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    var item: Item,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: Category,

    @ColumnDefault("0")
    @Column(name = "point_value", nullable = false)
    var pointValue: Float = 0f,

    @ColumnDefault("0")
    @Column(name = "item_max_points", nullable = false)
    var itemMaxPoints: Float = 0f,

    @ColumnDefault("'2023-02'")
    @Column(name = "semester_name", nullable = false, columnDefinition = "char(7)")
    var semesterName: String
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 11)
    var id: Int? = null

    // 새로 추가된 필드
    @ColumnDefault("0")
    @Column(name = "category_max_points", nullable = false)
    var categoryMaxPoints: Float = 0f

    companion object {
        fun createSemesterItem(form: SemesterItemForm, item: Item, category: Category): SemesterItem {
            val semesterItem = SemesterItem(
                item,
                category,
                form.weight,
                form.maxPoints,
                form.name
            )
            item.addSemesterItem(semesterItem)
            return semesterItem
        }
    }
}
