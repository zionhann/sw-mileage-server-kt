package edu.handong.cseemileage.mileage.record.repository

import edu.handong.cseemileage.mileage.record.domain.Record
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RecordRepository : JpaRepository<Record, Int>
