package edu.handong.cseemileage.mileage.semester.domain

import edu.handong.cseemileage.mileage.item.domain.Item
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
@Table(name = "semester")
class Semester(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subitem_id", nullable = false)
    var item: Item,

    @ColumnDefault("0")
    @Column(name = "weight", nullable = false)
    var weight: Float = 0f,

    @ColumnDefault("0")
    @Column(name = "max_points", nullable = false)
    var maxPoints: Float = 0f,

    @Column(name = "name", nullable = false, columnDefinition = "char(7)")
    var name: String
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 11)
    var id: Int? = null
}
