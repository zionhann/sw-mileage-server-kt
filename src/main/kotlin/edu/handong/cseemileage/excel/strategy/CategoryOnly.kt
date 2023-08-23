package edu.handong.cseemileage.excel.strategy

import edu.handong.cseemileage.excel.ExcelUtils.Companion.addCategoryColumns
import edu.handong.cseemileage.excel.dto.ExcelDto
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import org.springframework.stereotype.Component

@Component
class CategoryOnly(
    val categoryRepository: CategoryRepository
) : DownloadStrategy {
    override var semester: String = ""
    override var description: String = "글로벌 카테고리 단독 조회"
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