package edu.handong.cseemileage.mileage.category.repository

import edu.handong.cseemileage.mileage.category.domain.Category
import org.springframework.data.repository.CrudRepository

interface CategoryRepository : CrudRepository<Category, Int>
