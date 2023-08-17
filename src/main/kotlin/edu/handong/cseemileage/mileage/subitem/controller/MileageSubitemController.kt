package edu.handong.cseemileage.mileage.subitem.controller

import edu.handong.cseemileage.mileage.subitem.dto.SubitemDto
import edu.handong.cseemileage.mileage.subitem.dto.SubitemForm
import edu.handong.cseemileage.mileage.subitem.service.SubitemQueryService
import edu.handong.cseemileage.mileage.subitem.service.SubitemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/mileage/items")
class MileageSubitemController @Autowired constructor(
    val subitemService: SubitemService,
    val subitemQueryService: SubitemQueryService
) {
    @PostMapping
    fun createSubitem(
        @RequestBody @Valid
        form: SubitemForm
    ) {
        return subitemService.saveSubitem(form)
    }

    @GetMapping
    fun getSubitems(): ResponseEntity<SubitemDto> {
        val subitems = subitemQueryService.getSubitems()
        return ResponseEntity.ok(SubitemDto(subitems))
    }

    @PatchMapping("/{subitemId}")
    fun modifySubitem(
        @PathVariable("subitemId")
        subitemId: Int,
        @RequestBody @Valid
        form: SubitemForm
    ) {
        return subitemService.modifySubitem(subitemId, form)
    }

    @DeleteMapping("/{subitemId}")
    fun deleteSubitem(
        @PathVariable("subitemId")
        subitemId: Int
    ) {
        return subitemService.deleteSubitem(subitemId)
    }
}
