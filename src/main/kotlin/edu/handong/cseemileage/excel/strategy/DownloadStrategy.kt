package edu.handong.cseemileage.excel.strategy

import edu.handong.cseemileage.excel.dto.ExcelDto
import java.lang.reflect.Field

interface DownloadStrategy {

    var semester: String
    var description: String
    fun getExcelDtoList(): List<ExcelDto>
    fun getCount(): Long
    fun getList(): List<*>?
    fun getValue(obj: Any, fieldName: String, excelDtoType: String): Any
    fun getBasicValue(obj: Any, fieldName: String): Any {
        val field: Field = obj.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true
        return field.get(obj)
    }

    fun getCategoryValue(obj: Any, fieldName: String): Any {
        val categoryObj = getCategoryObj(obj)
        return getBasicValue(categoryObj, fieldName)
    }

    fun getCategoryObj(obj: Any): Any {
        val categoryField: Field = obj.javaClass.getDeclaredField("category")
        categoryField.isAccessible = true
        return categoryField.get(obj)
    }

    fun getItemValue(obj: Any, fieldName: String): Any {
        val itemObj = getItemObj(obj)
        return getBasicValue(itemObj, fieldName)
    }

    fun getItemObj(obj: Any): Any {
        val itemField: Field = obj.javaClass.getDeclaredField("item")
        itemField.isAccessible = true
        return itemField.get(obj)
    }
}
