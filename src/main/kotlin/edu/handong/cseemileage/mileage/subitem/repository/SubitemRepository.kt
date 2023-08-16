package edu.handong.cseemileage.mileage.subitem.repository

import edu.handong.cseemileage.mileage.subitem.domain.Subitem
import org.springframework.data.repository.CrudRepository

interface SubitemRepository : CrudRepository<Subitem, Int>
