package edu.handong.cseemileage.excel.controller

import edu.handong.cseemileage.excel.ExcelUtils
import edu.handong.cseemileage.excel.service.ExcelService
import edu.handong.cseemileage.excel.strategy.CategoryOnly
import edu.handong.cseemileage.excel.strategy.DownloadStrategy
import edu.handong.cseemileage.excel.strategy.Global
import edu.handong.cseemileage.excel.strategy.ItemOnly
import edu.handong.cseemileage.excel.strategy.SemesterIn
import edu.handong.cseemileage.excel.strategy.SemesterOnly
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.repository.ItemRepository
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepository
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/excel")
class ExcelController @Autowired constructor(
    val excelService: ExcelService,
    val categoryRepository: CategoryRepository,
    val itemRepository: ItemRepository,
    val semesterItemRepository: SemesterItemRepository
) {
    companion object {
        const val EXCEL_TYPE_CATEGORY_ONLY = "category"
        const val EXCEL_TYPE_ITEM_ONLY = "item"
        const val EXCEL_TYPE_SEMESTER_ONLY = "semester"
        const val EXCEL_TYPE_GLOBAL = "global"
        const val EXCEL_TYPE_SEMESTER_IN = "semesterIn"
    }

    @Deprecated(message = "엑셀 구현 중 참고용 기본 코드로만 사용할 예정")
    @GetMapping("/test")
    fun downloadExcel(response: HttpServletResponse) {
        val workbook: Workbook = HSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("sheet1")
        var rowNo = 0

        val headerRow: Row = sheet.createRow(rowNo++)
        headerRow.createCell(0).setCellValue("header1")

        for (i in 1..10) {
            val row = sheet.createRow(rowNo++)
            row.createCell(0).setCellValue("value $i")
        }

        response.contentType = "application/octet-stream"
        response.setHeader("Content-Disposition", "attachment;filename=downloadTest.xls")

        workbook.write(response.outputStream)
        workbook.close()
    }

    /**
     * 엑셀 다운로드
     * 그룹핑, 통계적 요소가 들어가지 않은 단순 조회용 엑셀 파일 다운로드 기능
     *
     * Todo: excelType이 잘 못 들어왔을 경우 예외 처리
     * Todo: (확장기능) category, item, semester 별로 그룹핑한 엑셀 파일 다운로드 기능 개발
     *
     * @param excelType 엑셀 파일의 종류: category, item, semester, global, semesterIn
     * */
    @GetMapping("/download/{excelType}")
    fun downloadCategory(
        response: HttpServletResponse,
        @PathVariable("excelType") excelType: String,
        @RequestParam(defaultValue = "", required = false) semester: String
    ) {
        response.contentType = "application/octet-stream"
        response.setHeader("Content-Disposition", "attachment;filename=$excelType$semester.xls")

        var downloadStrategy: DownloadStrategy? = when (excelType) {
            EXCEL_TYPE_CATEGORY_ONLY -> CategoryOnly(categoryRepository)
            EXCEL_TYPE_ITEM_ONLY -> ItemOnly(itemRepository)
            EXCEL_TYPE_SEMESTER_ONLY -> SemesterOnly(semesterItemRepository)
            EXCEL_TYPE_GLOBAL -> Global(itemRepository)
            EXCEL_TYPE_SEMESTER_IN -> SemesterIn(semesterItemRepository)
            else -> null
        }
        if (downloadStrategy != null) {
            downloadStrategy.semester = semester
        }

        downloadStrategy?.let {
            val excelUtils = ExcelUtils(it)
            val stream: ByteArrayInputStream = excelUtils.createListToExcel()
            IOUtils.copy(stream, response.outputStream)
        }
    }

    @Deprecated(message = "엑셀 구현 중 참고용 기본 코드로만 사용할 예정")
    @PostMapping("/test")
    fun uploadExcel(
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<String> {
        excelService.save(file)
        return ResponseEntity.status(HttpStatus.OK).body("엑셀 파일 업로드 성공")
    }
}
