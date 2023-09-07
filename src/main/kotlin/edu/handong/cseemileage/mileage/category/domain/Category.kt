package edu.handong.cseemileage.mileage.category.domain

import edu.handong.cseemileage.BaseEntity
import edu.handong.cseemileage.mileage.category.dto.CategoryForm
import edu.handong.cseemileage.mileage.item.domain.Item
import org.hibernate.annotations.ColumnDefault
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "_sw_mileage_category")
class Category(
    @Column(name = "name", nullable = false, length = 30)
    var name: String
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 11)
    var id: Int = 0

    @ColumnDefault("0")
    @Column(name = "category_max_points", nullable = false)
    var categoryMaxPoints: Float = 0f

    @Column(name = "description1", length = 300)
    var description1: String? = null

    @Column(name = "description2", length = 300)
    var description2: String? = null

    @ColumnDefault("0")
    @Column(name = "order_idx", nullable = false, length = 11)
    var orderIdx: Int = 0

    @ColumnDefault("'R'")
    @Column(name = "item_type", nullable = false, columnDefinition = "char(1)")
    var itemType: String = "R"

    @ColumnDefault("0")
    @Column(columnDefinition = "tinyint(1)", nullable = false, name = "is_multi")
    var isMulti: Boolean = false

    @OneToMany(mappedBy = "category")
    var items: MutableList<Item> = mutableListOf()

    fun update(form: CategoryForm): Int {
        this.apply {
            name = form.title ?: name
            orderIdx = form.orderIdx ?: orderIdx
            categoryMaxPoints = form.categoryMaxPoints ?: categoryMaxPoints
            description1 = form.description1 ?: description1
            description2 = form.description2 ?: description2
            itemType = form.itemType ?: itemType
            isMulti = form.isMulti ?: isMulti
        }
        return id!!
    }

    /**
     * 양방향 매핑 - 연관관계 편의 메서드
     * */
    fun addItem(item: Item) {
        items.add(item)
    }
}
