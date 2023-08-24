package edu.handong.cseemileage.mileage.mileageRecord.repository

import edu.handong.cseemileage.mileage.mileageRecord.domain.MileageRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MileageRecordRepository : JpaRepository<MileageRecord, Int>
