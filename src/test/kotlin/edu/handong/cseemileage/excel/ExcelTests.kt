package edu.handong.cseemileage.excel

import edu.handong.cseemileage.excel.dto.ExcelDto
import edu.handong.cseemileage.excel.strategy.CategoryOnly
import edu.handong.cseemileage.excel.strategy.DownloadStrategy
import edu.handong.cseemileage.excel.strategy.ItemOnly
import edu.handong.cseemileage.excel.strategy.SemesterOnly
import edu.handong.cseemileage.mileage.category.domain.Category
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.domain.Item
import edu.handong.cseemileage.mileage.item.repository.ItemRepository
import edu.handong.cseemileage.mileage.semesterItem.domain.SemesterItem
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testng.AssertJUnit.assertTrue

@SpringBootTest
class ExcelTests @Autowired constructor(
    val itemRepository: ItemRepository,
    val categoryRepository: CategoryRepository,
    val semesterItemRepository: SemesterItemRepository
) {
    @Test
    fun getExcelDtoList() {
        val classList: List<Class<*>> = listOf(Category::class.java, Item::class.java, SemesterItem::class.java)
        for (anyClass in classList) {
            val strategy: DownloadStrategy = when (anyClass) {
                Category::class.java -> CategoryOnly(categoryRepository)
                Item::class.java -> ItemOnly(itemRepository)
                SemesterItem::class.java -> SemesterOnly(semesterItemRepository)
                else -> throw IllegalArgumentException("Invalid class type")
            }
            val excelDtos = strategy.getExcelDtoList()
            excelDtos.forEach { excelDto ->
                val fieldExists = isFieldExists(anyClass, excelDto)
                when (fieldExists) {
                    false -> {
                        val superFieldExists = isFieldExists(anyClass.superclass, excelDto)
                        // TODO: Log로 바꿔보기
                        printColumnId(superFieldExists, excelDto)
                        assertTrue(superFieldExists)
                    }

                    else -> printColumnId(fieldExists, excelDto)
                }
            }
        }
    }

    fun isFieldExists(anyClass: Class<*>, excelDto: ExcelDto): Boolean {
        val fieldExists = anyClass.declaredFields.any { field ->
            field.name == excelDto.columnId
        }
        return fieldExists
    }

    fun printColumnId(fieldExists: Boolean, excelDto: ExcelDto) {
        println(excelDto.columnId + " : " + fieldExists)
    }
}
