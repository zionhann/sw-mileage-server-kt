package edu.handong.cseemileage.excel

import edu.handong.cseemileage.excel.dto.ExcelDto
import edu.handong.cseemileage.excel.strategy.DownloadStrategy
import edu.handong.cseemileage.excel.strategy.Global
import edu.handong.cseemileage.excel.strategy.SemesterIn
import org.apache.poi.hssf.usermodel.HSSFWorkbook
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

        // 기본 스타일 설정
        val basicStyle = workbook.createCellStyle()
        // 가운데 정렬
        basicStyle.alignment = HorizontalAlignment.CENTER
        // 폰트 크기
        val font = workbook.createFont()
        font.fontHeightInPoints = 14.toShort()
        basicStyle.setFont(font)

        // 헤더 스타일 설정
        val headerStyle = workbook.createCellStyle()
        headerStyle.cloneStyleFrom(basicStyle)
        // border
        headerStyle.borderTop = BorderStyle.THIN
        headerStyle.borderBottom = BorderStyle.THIN
        headerStyle.borderLeft = BorderStyle.THIN
        headerStyle.borderRight = BorderStyle.THIN
        // 색상
        headerStyle.fillForegroundColor = downloadStrategy.getHSSFColor()
        headerStyle.fillPattern = FillPatternType.SOLID_FOREGROUND

        // 헤더 생성
        row = sheet.createRow(rowNo++)
        if (downloadStrategy is Global || downloadStrategy is SemesterIn) {
            for (i in 0 until headerSize) {
                cell = row.createCell(i)
                val cellStyle = workbook.createCellStyle()
                cellStyle.cloneStyleFrom(headerStyle)
                cell.cellStyle = when (excelDtoList[i].type) {
                    DownloadStrategy.EXCEL_DTO_CATEGORY -> {
                        cellStyle.fillForegroundColor = downloadStrategy.getCategoryHSSFColor()
                        cellStyle
                    }
                    DownloadStrategy.EXCEL_DTO_ITEM -> {
                        cellStyle.fillForegroundColor = downloadStrategy.getItemHSSFColor()
                        cellStyle
                    }
                    DownloadStrategy.EXCEL_DTO_SEMESTER -> {
                        cellStyle.fillForegroundColor = downloadStrategy.getSemesterHSSFColor()
                        cellStyle
                    }
                    else -> cellStyle
                }
                cell.setCellValue(excelDtoList[i].columnName)
            }
        } else {
            for (i in 0 until headerSize) {
                cell = row.createCell(i)
                cell.cellStyle = headerStyle
                cell.setCellValue(excelDtoList[i].columnName)
            }
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
                    cell.cellStyle = basicStyle
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
