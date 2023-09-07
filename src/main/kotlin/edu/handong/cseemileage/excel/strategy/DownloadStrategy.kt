package edu.handong.cseemileage.excel.strategy

import edu.handong.cseemileage.excel.dto.ExcelDto
import java.lang.reflect.Field

interface DownloadStrategy {

    companion object {
        /**
         * 여러 @Entity의 값들이 합쳐진 엑셀 파일을 만들기 위해 ExcelDto에 아래와 같은 타입 지정이 필요하다.
         * @Entity의 이름과 같아야 한다.
         */
        const val EXCEL_DTO_CATEGORY = "Category"
        const val EXCEL_DTO_ITEM = "Item"
        const val EXCEL_DTO_SEMESTER = "SemesterItem"

        fun addCategoryColumns(list: MutableList<ExcelDto>) {
            list.add(ExcelDto("id", "카테고리 ID", EXCEL_DTO_CATEGORY))
            list.add(ExcelDto("orderIdx", "순서", EXCEL_DTO_CATEGORY))
            list.add(ExcelDto("categoryMaxPoints", "카테고리 최대 마일리지", EXCEL_DTO_CATEGORY))
            list.add(ExcelDto("name", "카테고리 이름", EXCEL_DTO_CATEGORY))
            list.add(ExcelDto("description1", "카테고리 설명1", EXCEL_DTO_CATEGORY))
            list.add(ExcelDto("description2", "카테고리 설명2", EXCEL_DTO_CATEGORY))
            list.add(ExcelDto("isMulti", "다중 하위 항목 여부", EXCEL_DTO_CATEGORY))
            list.add(ExcelDto("itemType", "하위 항목 유형", EXCEL_DTO_CATEGORY))
            list.add(ExcelDto("modDate", "카테고리 마지막 수정일", EXCEL_DTO_CATEGORY))
            list.add(ExcelDto("regDate", "카테고리 등록일", EXCEL_DTO_CATEGORY))
        }

        fun addItemColumns(list: MutableList<ExcelDto>) {
            list.add(ExcelDto("id", "세부 항목 ID", EXCEL_DTO_ITEM))
            list.add(ExcelDto("itemMaxPoints", "세부 항목 최대 마일리지", EXCEL_DTO_ITEM))
            list.add(ExcelDto("name", "세부 항목 이름", EXCEL_DTO_ITEM))
            list.add(ExcelDto("isPortfolio", "포트폴리오 여부", EXCEL_DTO_ITEM))
            list.add(ExcelDto("description1", "세부 항목 설명1", EXCEL_DTO_ITEM))
            list.add(ExcelDto("description2", "세부 항목 설명2", EXCEL_DTO_ITEM))
            list.add(ExcelDto("stuType", "학생 유형", EXCEL_DTO_ITEM))
            list.add(ExcelDto("isVisible", "보이기 여부", EXCEL_DTO_ITEM))
            list.add(ExcelDto("isStudentVisible", "학생 보이기 여부", EXCEL_DTO_ITEM))
            list.add(ExcelDto("isStudentInput", "학생 입력 여부", EXCEL_DTO_ITEM))
            list.add(ExcelDto("isMulti", "다중 학기별 항목 여부", EXCEL_DTO_ITEM))
            list.add(ExcelDto("hasFileDescription", "파일 설명 여부", EXCEL_DTO_ITEM))
            list.add(ExcelDto("fileDescription", "파일 설명", EXCEL_DTO_ITEM))
            list.add(ExcelDto("modDate", "세부 항목 마지막 수정일", EXCEL_DTO_ITEM))
            list.add(ExcelDto("regDate", "세부 항목 등록일", EXCEL_DTO_ITEM))
        }

        fun addSemesterItemColumns(list: MutableList<ExcelDto>) {
            list.add(ExcelDto("id", "학기 정보 ID", EXCEL_DTO_SEMESTER))
            list.add(ExcelDto("semesterName", "학기", EXCEL_DTO_SEMESTER))
            list.add(ExcelDto("pointValue", "가중치", EXCEL_DTO_SEMESTER))
            list.add(ExcelDto("itemMaxPoints", "학기별 항목 최대 마일리지", EXCEL_DTO_SEMESTER))
            list.add(ExcelDto("modDate", "학기별 항목 마지막 수정일", EXCEL_DTO_ITEM))
            list.add(ExcelDto("regDate", "학기별 항목 등록일", EXCEL_DTO_ITEM))
        }
    }

    var semester: String
    var description: String
    fun getExcelDtoList(): List<ExcelDto>
    fun getCount(): Long
    fun getList(): List<*>?
    fun getValue(obj: Any, fieldName: String, excelDtoType: String): Any
    fun getBasicValue(obj: Any, fieldName: String): Any {
        val field: Field = try {
            obj.javaClass.getDeclaredField(fieldName)
        } catch (e: NoSuchFieldException) {
            obj.javaClass.superclass.getDeclaredField(fieldName)
        }
        field.isAccessible = true
        field.get(obj)?.let { return it }
        return ""
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
