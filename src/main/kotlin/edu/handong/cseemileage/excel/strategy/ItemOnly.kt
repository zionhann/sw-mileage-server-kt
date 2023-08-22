package edu.handong.cseemileage.excel.strategy

import edu.handong.cseemileage.excel.ExcelUtils.Companion.addItemColumns
import edu.handong.cseemileage.excel.dto.ExcelDto
import edu.handong.cseemileage.mileage.item.repository.ItemRepository

class ItemOnly(
    val itemRepository: ItemRepository
) : DownloadStrategy {
    override var semester: String = ""
    override var description: String = "글로벌 항목 단독 조회"
    override fun getExcelDtoList(): List<ExcelDto> {
        val list: MutableList<ExcelDto> = ArrayList()
        addItemColumns(list)
        return list
    }

    override fun getCount(): Long {
        return itemRepository.count()
    }

    override fun getList(): List<*>? {
        return itemRepository.findAll()
    }
}
