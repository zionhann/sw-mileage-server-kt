package edu.handong.cseemileage.mileage.item.repository

import edu.handong.cseemileage.mileage.item.domain.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Int> {
    fun findTopByOrderByIdDesc(): Item?
}
