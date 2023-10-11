package edu.handong.cseemileage.domain.mileage

import edu.handong.cseemileage.domain.BaseEntity
import edu.handong.cseemileage.dto.mileage.category.CategoryForm
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

    @ColumnDefault("'A'")
    @Column(name = "type", nullable = false)
    var type: String = "A"

    @OneToMany(mappedBy = "category")
    var items: MutableList<Item> = mutableListOf()

    fun update(form: CategoryForm): Int {
        this.apply {
            name = form.title ?: name
            orderIdx = form.orderIdx ?: orderIdx
            categoryMaxPoints = form.categoryMaxPoints ?: categoryMaxPoints
            description1 = form.description1 ?: description1
            description2 = form.description2 ?: description2
            type = form.type ?: type
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
