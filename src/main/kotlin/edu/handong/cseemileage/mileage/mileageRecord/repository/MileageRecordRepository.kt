package edu.handong.cseemileage.mileage.mileageRecord.repository

import edu.handong.cseemileage.mileage.mileageRecord.domain.MileageRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MileageRecordRepository : JpaRepository<MileageRecord, Int> {
    @Query("SELECT r FROM MileageRecord r JOIN FETCH r.semesterItem s WHERE s.semesterName = :name")
    fun findAllBySemesterName(name: String): List<MileageRecord>
}
