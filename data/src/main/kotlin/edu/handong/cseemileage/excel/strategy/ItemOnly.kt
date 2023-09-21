package edu.handong.cseemileage.excel.strategy

import edu.handong.cseemileage.dto.excel.ExcelDto
import edu.handong.cseemileage.excel.strategy.DownloadStrategy.Companion.addItemColumns
import edu.handong.cseemileage.repository.mileage.ItemRepository

class ItemOnly(
    val itemRepository: ItemRepository
) : DownloadStrategy {
    override var semester: String = ""
    override var description: String = "글로벌 항목 단독 조회"
    override fun getHSSFColor(): Short {
        return super.getItemHSSFColor()
    }

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

    override fun getValue(obj: Any, fieldName: String, excelDtoType: String): Any {
        return super.getBasicValue(obj, fieldName)
    }
}
