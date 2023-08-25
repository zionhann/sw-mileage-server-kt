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
