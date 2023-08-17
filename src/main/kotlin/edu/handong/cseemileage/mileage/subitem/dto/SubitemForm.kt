package edu.handong.cseemileage.mileage.subitem.dto

data class SubitemForm(
    val categoryId: Int,
    val subitemName: String,
    val weight: Float,
    val isPortfolio: Int,
    val maxPoint: Float,
    val description1: String,
    val description2: String,
    val semester: String,
    val stuType: String
)
