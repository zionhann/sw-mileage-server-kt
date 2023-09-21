package edu.handong.cseemileage.repository.mileage

import edu.handong.cseemileage.domain.mileage.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CategoryRepository : JpaRepository<Category, Int> {
    fun findTopByOrderByIdDesc(): Category?
    fun findByName(name: String): Category?

    @Query("SELECT c FROM Category c JOIN FETCH c.items")
    fun findAllWithItems(): List<Category>
}
