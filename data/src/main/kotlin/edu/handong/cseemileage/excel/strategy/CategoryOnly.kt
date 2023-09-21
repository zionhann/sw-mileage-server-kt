package edu.handong.cseemileage.excel.strategy

import edu.handong.cseemileage.dto.excel.ExcelDto
import edu.handong.cseemileage.excel.strategy.DownloadStrategy.Companion.addCategoryColumns
import edu.handong.cseemileage.repository.mileage.CategoryRepository
import org.springframework.stereotype.Component

@Component
class CategoryOnly(
    val categoryRepository: CategoryRepository
) : DownloadStrategy {
    override var semester: String = ""
    override var description: String = "글로벌 카테고리 단독 조회"
    override fun getHSSFColor(): Short {
        return super.getCategoryHSSFColor()
    }

    override fun getExcelDtoList(): List<ExcelDto> {
        val list: MutableList<ExcelDto> = ArrayList()
        addCategoryColumns(list)
        return list
    }

    override fun getCount(): Long {
        return categoryRepository.count()
    }

    override fun getList(): List<*>? {
        return categoryRepository.findAll()
    }

    override fun getValue(obj: Any, fieldName: String, excelDtoType: String): Any {
        return super.getBasicValue(obj, fieldName)
    }
}
