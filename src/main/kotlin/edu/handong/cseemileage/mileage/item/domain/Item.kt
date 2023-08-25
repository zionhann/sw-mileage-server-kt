package edu.handong.cseemileage.mileage.item.domain

import edu.handong.cseemileage.BaseEntity
import edu.handong.cseemileage.mileage.category.domain.Category
import edu.handong.cseemileage.mileage.item.dto.ItemForm
import edu.handong.cseemileage.mileage.semesterItem.domain.SemesterItem
import edu.handong.cseemileage.utils.Utils
import org.hibernate.annotations.ColumnDefault
import org.jetbrains.annotations.NotNull
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

@Entity
@Table(name = "_sw_mileage_item")
class Item(
    @field: NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: Category,

    @field: NotNull
    @Column(name = "name", length = 30)
    var name: String,

    @ColumnDefault("0")
    @Column(columnDefinition = "tinyint(1)", nullable = false, name = "is_portfolio")
    var isPortfolio: Boolean,

    @Column(name = "description1", length = 300)
    var description1: String? = null,

    @Column(name = "description2", length = 300)
    var description2: String? = null,

    @Column(name = "stu_type", length = 3)
    var stuType: String,

    @OneToMany(mappedBy = "item")
    var semesterItems: MutableList<SemesterItem> = mutableListOf(),

    @ColumnDefault("'Y'")
    @Column(columnDefinition = "char(1)", nullable = false, name = "is_visible")
    var isVisible: String = "Y",

    @ColumnDefault("'N'")
    @Column(columnDefinition = "char(1)", nullable = false, name = "is_student_visible")
    var isStudentVisible: String = "N",

    @ColumnDefault("'N'")
    @Column(columnDefinition = "char(1)", nullable = false, name = "is_student_input")
    var isStudentInput: String = "N",

    @ColumnDefault("'N'")
    @Column(columnDefinition = "char(1)", nullable = false, name = "is_multi")
    var isMulti: String = "N",

    @ColumnDefault("'N'")
    @Column(columnDefinition = "char(1)", nullable = false, name = "has_file_description")
    var hasFileDescription: String = "N",

    @Column(name = "file_description", length = 300)
    var fileDescription: String? = null

) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 11)
    var id: Int? = null

    companion object {
        fun createItem(form: ItemForm, category: Category): Item {
            val item = Item(
                category = category,
                name = form.itemName!!,
                isPortfolio = form.flags.isPortfolio!!,
                description1 = form.description1,
                description2 = form.description2,
                stuType = form.stuType!!,
                isVisible = Utils.booleanToString(form.flags.isVisible!!),
                isStudentVisible = Utils.booleanToString(form.flags.isStudentVisible!!),
                isStudentInput = Utils.booleanToString(form.flags.isStudentEditable!!),
                isMulti = Utils.booleanToString(form.flags.isMultiple!!)
            )
            category.addItem(item)
            return item
        }
    }

    fun addSemesterItem(semesterItem: SemesterItem) {
        semesterItems.add(semesterItem)
    }
}
