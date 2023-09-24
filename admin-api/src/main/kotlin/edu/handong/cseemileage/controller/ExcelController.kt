package edu.handong.cseemileage.controller

import edu.handong.cseemileage.excel.ExcelUtils
import edu.handong.cseemileage.excel.strategy.CategoryOnly
import edu.handong.cseemileage.excel.strategy.DownloadStrategy
import edu.handong.cseemileage.excel.strategy.Global
import edu.handong.cseemileage.excel.strategy.ItemOnly
import edu.handong.cseemileage.excel.strategy.SemesterIn
import edu.handong.cseemileage.excel.strategy.SemesterOnly
import edu.handong.cseemileage.exception.mileage.semesterItem.SemesterPatternException
import edu.handong.cseemileage.repository.mileage.CategoryRepository
import edu.handong.cseemileage.repository.mileage.ItemRepository
import edu.handong.cseemileage.repository.mileage.SemesterItemRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayInputStream
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/excel")
@Tag(name = "엑셀 API")
class ExcelController @Autowired constructor(
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

    /**
     * 엑셀 다운로드
     * 그룹핑, 통계적 요소가 들어가지 않은 단순 조회용 엑셀 파일 다운로드 기능
     *
     * Todo: excelType이 잘 못 들어왔을 경우 예외 처리
     * Todo: (확장기능) category, item, semester 별로 그룹핑한 엑셀 파일 다운로드 기능 개발
     *
     * @param excelType 엑셀 파일의 종류: category, item, semester, global, semesterIn
     * */
    @Operation(summary = "엑셀 다운로드 by excelType(다운로드 전략)", description = "전략 종류: category, item, semesterItem, global, semesterIn")
    @Parameters(
        value = [
            Parameter(name = "excelType", description = "다운로드 전략", required = true, example = "category"),
            Parameter(name = "semester", description = "학기", required = false, example = "2023-02")
        ]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "엑셀 다운로드 성공"),
            ApiResponse(responseCode = "500", description = "엑셀 다운로드 실패 - 학기 형식 오류")
        ]
    )
    @GetMapping("/download/{excelType}")
    fun downloadCategory(
        response: HttpServletResponse,
        @PathVariable("excelType") excelType: String,
        @RequestParam(defaultValue = "", required = false) semester: String
    ) {
        if (!semester.matches("^(\\d{4}-(01|02))$".toRegex())) {
            throw SemesterPatternException()
        }
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
}
