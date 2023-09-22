package edu.handong.cseemileage.domain

import lombok.experimental.SuperBuilder
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@SuperBuilder
@EntityListeners(AuditingEntityListener::class)
class BaseEntity {
    @UpdateTimestamp
    @Column(name = "mod_date", nullable = false, columnDefinition = "datetime")
    var modDate: LocalDateTime? = LocalDateTime.now()

    @CreationTimestamp
    @Column(name = "reg_date", nullable = false, columnDefinition = "datetime")
    var regDate: LocalDateTime? = LocalDateTime.now()
}
