package edu.handong.cseemileage.domain.mileage

import edu.handong.cseemileage.domain.BaseEntity
import edu.handong.cseemileage.dto.mileage.item.ItemForm
import edu.handong.cseemileage.utils.Utils.Companion.booleanToString
import org.hibernate.annotations.ColumnDefault
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: Category,

    @Column(name = "name", nullable = false, length = 30)
    var name: String
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 11)
    var id: Int = 0

    @ColumnDefault("0")
    @Column(name = "item_max_points", nullable = false)
    var itemMaxPoints: Float = 0f

    @ColumnDefault("0")
    @Column(columnDefinition = "tinyint(1)", nullable = false, name = "is_portfolio")
    var isPortfolio: Boolean = false

    @Column(name = "description1", length = 300)
    var description1: String? = null

    @Column(name = "description2", length = 300)
    var description2: String? = null

    @Column(name = "stu_type", length = 3)
    var stuType: String? = null

    @ColumnDefault("'Y'")
    @Column(columnDefinition = "char(1)", nullable = false, name = "is_visible")
    var isVisible: String = "Y"

    @ColumnDefault("'N'")
    @Column(columnDefinition = "char(1)", nullable = false, name = "is_student_visible")
    var isStudentVisible: String = "N"

    @ColumnDefault("'N'")
    @Column(columnDefinition = "char(1)", nullable = false, name = "is_student_input")
    var isStudentInput: String = "N"

    @Transient
    @ColumnDefault("'N'")
    @Column(columnDefinition = "char(1)", nullable = false, name = "has_file_description")
    var hasFileDescription: String = "N"

    @Transient
    @Column(name = "file_description", length = 300)
    var fileDescription: String? = null

    @OneToMany(mappedBy = "item")
    var semesterItems: MutableList<SemesterItem> = mutableListOf()

    companion object {
        fun createItem(form: ItemForm, category: Category): Item {
            val item = Item(
                category = category,
                name = form.itemName!!
            ).apply {
                itemMaxPoints = form.itemMaxPoints ?: 0f
                description1 = form.description1
                description2 = form.description2
                stuType = form.stuType
                isPortfolio = form.flags?.isPortfolio ?: false
                isVisible = booleanToString(form.flags?.isVisible ?: true)
                isStudentVisible = booleanToString(form.flags?.isStudentVisible ?: false)
                isStudentInput = booleanToString(form.flags?.isStudentEditable ?: false)
            }
            category.addItem(item)
            return item
        }
    }

    fun addSemesterItem(semesterItem: SemesterItem) {
        semesterItems.add(semesterItem)
    }

    fun update(form: ItemForm, category: Category): Int {
        this.apply {
            name = form.itemName ?: name
            this@Item.category = category
            itemMaxPoints = form.itemMaxPoints ?: itemMaxPoints
            description1 = form.description1 ?: description1
            description2 = form.description2 ?: description2
            stuType = form.stuType ?: stuType
            isPortfolio = form.flags?.isPortfolio ?: isPortfolio
            isVisible = form.flags?.isVisible?.let { booleanToString(it) } ?: isVisible
            isStudentVisible = form.flags?.isStudentVisible?.let { booleanToString(it) } ?: isStudentVisible
            isStudentInput = form.flags?.isStudentEditable?.let { booleanToString(it) } ?: isStudentInput
        }
        return id!!
    }
}
