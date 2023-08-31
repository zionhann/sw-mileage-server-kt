package edu.handong.cseemileage.mileage.category.repository

import edu.handong.cseemileage.mileage.category.domain.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Int> {
    fun findTopByOrderByIdDesc(): Category?
    fun findByName(name: String): Category?
}
