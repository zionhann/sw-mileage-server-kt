package edu.handong.cseemileage.mileage.subitem.repository

import edu.handong.cseemileage.mileage.subitem.domain.Subitem
import org.springframework.data.jpa.repository.JpaRepository

interface SubitemRepository : JpaRepository<Subitem, Int> {
    fun findTopByOrderByIdDesc(): Subitem?
}
