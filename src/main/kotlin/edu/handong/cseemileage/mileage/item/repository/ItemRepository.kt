package edu.handong.cseemileage.mileage.item.repository

import edu.handong.cseemileage.mileage.item.domain.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ItemRepository : JpaRepository<Item, Int> {
    fun findTopByOrderByIdDesc(): Item?
    fun findByName(name: String): Item
    fun findAllByName(name: String): List<Item>

    @Query("SELECT i FROM Item i JOIN FETCH i.category")
    fun findAllWithCategory(): List<Item>
}
