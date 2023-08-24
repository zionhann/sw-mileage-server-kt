package edu.handong.cseemileage.mileage.category.domain

import edu.handong.cseemileage.BaseEntity
import edu.handong.cseemileage.mileage.category.dto.CategoryForm
import edu.handong.cseemileage.mileage.item.domain.Item
import org.hibernate.annotations.ColumnDefault
import org.jetbrains.annotations.NotNull
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
    @Column(name = "name", length = 30)
    var name: String? = null,

    @Column(name = "description1", length = 300)
    var description1: String? = null,

    @NotNull
    @Column(name = "max_points", nullable = false, length = 11)
    var maxPoints: Int? = 0,

    @OneToMany(mappedBy = "category")
    var items: MutableList<Item> = mutableListOf()
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 11)
    var id: Int? = null

    // 새로 추가된 필드
    @ColumnDefault("0")
    @Column(name = "order_idx", nullable = false, length = 11)
    var orderIdx: Int = 0

    @Column(name = "description2", length = 300)
    var description2: String? = null

    @ColumnDefault("'R'")
    @Column(name = "item_type", nullable = false, columnDefinition = "char(1)")
    var itemType: String = "R"

    @ColumnDefault("'0'")
    @Column(name = "is_multi", nullable = false, columnDefinition = "char(1)")
    var isMulti: String = "0"

    fun update(form: CategoryForm): Int {
        this.name = form.title
        this.description1 = form.description
        this.maxPoints = form.maxPoints

        return id!!
    }

    /**
     * 양방향 매핑 - 연관관계 편의 메서드
     * */
    fun addItem(item: Item) {
        items.add(item)
    }
}
