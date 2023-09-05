package edu.handong.cseemileage.mileage.category.repository

import edu.handong.cseemileage.mileage.category.domain.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CategoryRepository : JpaRepository<Category, Int> {
    fun findTopByOrderByIdDesc(): Category?
    fun findByName(name: String): Category?

    @Query("SELECT c FROM Category c JOIN FETCH c.items")
    fun findAllWithItems(): List<Category>
}
