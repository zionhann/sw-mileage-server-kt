package edu.handong.cseemileage.mileage.semesterItem.repository

import edu.handong.cseemileage.mileage.semesterItem.domain.SemesterItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SemesterItemRepository : JpaRepository<SemesterItem, Int> {
    fun findTopByOrderByIdDesc(): SemesterItem?
    fun findAllBySemesterName(name: String): List<SemesterItem>
    fun countBySemesterName(name: String): Long

    @Query("SELECT s FROM SemesterItem s JOIN FETCH s.item i JOIN FETCH i.category WHERE s.semesterName = :name")
    fun findAllWithItemAndCategory(name: String): List<SemesterItem>
}
