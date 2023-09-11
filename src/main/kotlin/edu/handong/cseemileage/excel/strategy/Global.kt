package edu.handong.cseemileage.excel.strategy
import edu.handong.cseemileage.excel.dto.ExcelDto
import edu.handong.cseemileage.excel.strategy.DownloadStrategy.Companion.EXCEL_DTO_CATEGORY
import edu.handong.cseemileage.excel.strategy.DownloadStrategy.Companion.addCategoryColumns
import edu.handong.cseemileage.excel.strategy.DownloadStrategy.Companion.addItemColumns
import edu.handong.cseemileage.mileage.item.repository.ItemRepository

class Global(
    val itemRepository: ItemRepository
) : DownloadStrategy {
    override var semester: String = ""
    override var description: String = "글로벌 카테고리 + 항목 조회"
    override fun getHSSFColor(): Short {
        return super.getCategoryHSSFColor()
    }

    override fun getExcelDtoList(): List<ExcelDto> {
        val list: MutableList<ExcelDto> = ArrayList()
        addCategoryColumns(list)
        addItemColumns(list)
        return list
    }

    override fun getCount(): Long {
        return itemRepository.count()
    }

    override fun getList(): List<*>? {
        return itemRepository.findAllWithCategory()
    }

    override fun getValue(obj: Any, fieldName: String, excelDtoType: String): Any {
        if (excelDtoType == EXCEL_DTO_CATEGORY) {
            return super.getCategoryValue(obj, fieldName)
        }
        return super.getBasicValue(obj, fieldName)
    }
}
