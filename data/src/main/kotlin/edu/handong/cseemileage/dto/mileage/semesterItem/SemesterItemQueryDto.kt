package edu.handong.cseemileage.dto.mileage.semesterItem

import edu.handong.cseemileage.domain.mileage.Category
import edu.handong.cseemileage.domain.mileage.Item
import edu.handong.cseemileage.domain.mileage.SemesterItem

data class SemesterItemQueryDto(
    val semesterItem: SemesterItem,
    val item: Item,
    val category: Category,
    val recordCount: Long
)
