package edu.handong.cseemileage.mileage.semester.repository

import edu.handong.cseemileage.mileage.semester.domain.Semester
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SemesterRepository : JpaRepository<Semester, Int> {
    fun findTopByOrderByIdDesc(): Semester?
    fun findAllByName(name: String): List<Semester>
    fun countByName(name: String): Long

    @Query("SELECT s FROM Semester s JOIN FETCH s.item i JOIN FETCH i.category WHERE s.name = :name")
    fun findAllWithItemAndCategory(name: String): List<Semester>
}
