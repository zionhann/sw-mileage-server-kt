package edu.handong.cseemileage.mileage.mileageRecord.repository

import edu.handong.cseemileage.mileage.mileageRecord.domain.MileageRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MileageRecordRepository : JpaRepository<MileageRecord, Int> {
    @Query("SELECT r FROM MileageRecord r JOIN FETCH r.semesterItem s WHERE s.semesterName = :name")
    fun findAllBySemesterName(name: String): List<MileageRecord>

    @Query("SELECT r FROM MileageRecord r JOIN FETCH r.semesterItem i WHERE r.sid = :sid")
    fun findAllByStudentId(sid: String): List<MileageRecord>

    @Query("SELECT r FROM MileageRecord r JOIN FETCH r.semesterItem i WHERE i.id = :semesterItemId")
    fun findAllBySemesterItemId(semesterItemId: Int): List<MileageRecord>

    @Query("SELECT r FROM MileageRecord r JOIN FETCH r.semesterItem i JOIN FETCH i.item it")
    fun findAllWithAllReferences(): List<MileageRecord>

    @Query("SELECT r FROM MileageRecord r WHERE r.semesterItem.id = :semesterItemId")
    fun findAllBySemesterItemIdWithStudent(semesterItemId: Int): List<MileageRecord>
}
