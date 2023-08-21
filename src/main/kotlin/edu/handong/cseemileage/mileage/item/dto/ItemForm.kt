package edu.handong.cseemileage.mileage.item.dto

data class ItemForm(
    val categoryId: Int,
    val itemName: String,
    val isPortfolio: Int,
    val description1: String,
    val description2: String,
    val stuType: String
)
