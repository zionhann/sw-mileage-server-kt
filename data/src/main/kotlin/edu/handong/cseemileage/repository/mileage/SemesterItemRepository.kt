package edu.handong.cseemileage.repository.mileage

import edu.handong.cseemileage.domain.mileage.SemesterItem
import edu.handong.cseemileage.dto.mileage.semesterItem.SemesterItemQueryDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SemesterItemRepository : JpaRepository<SemesterItem, Int> {
    fun findTopByOrderByIdDesc(): SemesterItem?
    fun findAllBySemesterName(name: String): List<SemesterItem>
    fun countBySemesterName(name: String): Long
    fun findAllByItemId(itemId: Int): List<SemesterItem>
    fun findBySemesterNameAndItemId(semesterName: String, itemId: Int): SemesterItem?

    @Query(
        "SELECT s " +
            "FROM SemesterItem s " +
            "JOIN FETCH s.item i " +
            "JOIN FETCH i.category c " +
            "WHERE s.semesterName = :name"
    )
    fun findAllWithItemAndCategory(name: String): List<SemesterItem>

    @Query(
        "SELECT new edu.handong.cseemileage.dto.mileage.semesterItem.SemesterItemQueryDto(s, i, c, COUNT(r)) " +
            "FROM SemesterItem s " +
            "JOIN s.item i " +
            "JOIN i.category c " +
            "LEFT JOIN s.records r " +
            "WHERE s.semesterName = :name " +
            "GROUP BY s"
    )
    fun findAllWithItemAndCategoryAndRecordCount(name: String): List<SemesterItemQueryDto>
}
