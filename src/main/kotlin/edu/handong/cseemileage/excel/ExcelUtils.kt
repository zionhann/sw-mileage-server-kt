package edu.handong.cseemileage.excel

import edu.handong.cseemileage.excel.dto.ExcelDto
import edu.handong.cseemileage.excel.strategy.DownloadStrategy
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class ExcelUtils(
    private val downloadStrategy: DownloadStrategy
) {

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
            list.add(ExcelDto("name", "카테고리 이름", EXCEL_DTO_CATEGORY))
            list.add(ExcelDto("description1", "카테고리 설명1", EXCEL_DTO_CATEGORY))
            list.add(ExcelDto("description2", "카테고리 설명2", EXCEL_DTO_CATEGORY))
            list.add(ExcelDto("maxPoints", "카테고리 최대 마일리지", EXCEL_DTO_CATEGORY))
            list.add(ExcelDto("isMulti", "다중 하위 항목 여부", EXCEL_DTO_CATEGORY))
            list.add(ExcelDto("itemType", "하위 항목 유형", EXCEL_DTO_CATEGORY))
            list.add(ExcelDto("modDate", "카테고리 마지막 수정일", EXCEL_DTO_CATEGORY))
            list.add(ExcelDto("regDate", "카테고리 등록일", EXCEL_DTO_CATEGORY))
        }

        fun addItemColumns(list: MutableList<ExcelDto>) {
            list.add(ExcelDto("id", "세부 항목 ID", EXCEL_DTO_ITEM))
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
            list.add(ExcelDto("count", "가중치", EXCEL_DTO_SEMESTER))
            list.add(ExcelDto("itemMaxPoints", "학기별 항목 최대 마일리지", EXCEL_DTO_SEMESTER))
            list.add(ExcelDto("categoryMaxPoints", "학기별 카테고리 최대 마일리지", EXCEL_DTO_SEMESTER))
            list.add(ExcelDto("modDate", "학기별 항목 마지막 수정일", EXCEL_DTO_ITEM))
            list.add(ExcelDto("regDate", "학기별 항목 등록일", EXCEL_DTO_ITEM))
        }
    }

    fun createListToExcel(): ByteArrayInputStream {
        val count = downloadStrategy.getCount()
        val excelDtoList: List<ExcelDto> = downloadStrategy.getExcelDtoList()

        val workbook: Workbook = HSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("${downloadStrategy.description} List")
        var row: Row
        var cell: Cell
        var rowNo = 0

        val headerSize = excelDtoList.size

        // 테이블 헤더 스타일 설정
        val headerStyle = workbook.createCellStyle()
        // 경계선 설정
        headerStyle.borderTop = BorderStyle.THIN
        headerStyle.borderBottom = BorderStyle.THIN
        headerStyle.borderLeft = BorderStyle.THIN
        headerStyle.borderRight = BorderStyle.THIN
        // 색상
        headerStyle.fillForegroundColor = HSSFColor.HSSFColorPredefined.GREY_40_PERCENT.index
        headerStyle.fillPattern = FillPatternType.SOLID_FOREGROUND
        // 헤더 가운데 정렬
        headerStyle.alignment = HorizontalAlignment.CENTER

        // 헤더 생성
        row = sheet.createRow(rowNo++)
        for (i in 0 until headerSize) {
            cell = row.createCell(i)
            cell.cellStyle = headerStyle
            cell.setCellValue(excelDtoList[i].columnName)
        }

        // 내용 생성
        val list = downloadStrategy.getList()

        if (list != null) {
            for (o in list) {
                val dataRow = sheet.createRow(rowNo++)
                for (columnIndex in excelDtoList.indices) {
                    cell = dataRow.createCell(columnIndex)
                    val value: Any? = o?.let {
                        downloadStrategy.getValue(
                            it,
                            excelDtoList[columnIndex].columnId,
                            excelDtoList[columnIndex].type
                        )
                    }
                    cell.setCellValue((value ?: "").toString())
                }
            }
        }

        // 컬럼의 넓이를 자동으로 설정
        for (columnIndex in excelDtoList.indices) {
            sheet.autoSizeColumn(columnIndex)
        }

        val outputStream = ByteArrayOutputStream()
        outputStream.flush()
        workbook.write(outputStream)
        outputStream.flush()
        return ByteArrayInputStream(outputStream.toByteArray())
    }
}
