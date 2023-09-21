package edu.handong.cseemileage.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import edu.handong.cseemileage.domain.mileage.Category
import edu.handong.cseemileage.dto.mileage.item.ItemDto
import edu.handong.cseemileage.dto.mileage.item.ItemForm
import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.ExceptionResponse
import edu.handong.cseemileage.repository.mileage.CategoryRepository
import edu.handong.cseemileage.repository.mileage.ItemRepository
import edu.handong.cseemileage.util.ExtractExceptionMessage.Companion.getExceptionMessage
import edu.handong.cseemileage.utils.Utils.Companion.stringToBoolean
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class MileageItemIntegrationTests @Autowired constructor(
    val mapper: ObjectMapper,
    val itemRepository: ItemRepository,
    val categoryRepository: CategoryRepository,
    val subitemController: MileageItemController,
    var mockMvc: MockMvc
) {

    companion object {
        const val NAME = "전공 상담"
        const val DESCRIPTION1 = "description1"
        const val DESCRIPTION2 = "description2"
        const val STU_TYPE = "C"
        const val IS_VISIBLE = "N"
        const val IS_PORTFOLIO = true
        const val IS_STUDENT_VISIBLE = "Y"
        const val IS_STUDENT_INPUT = "Y"
        const val IS_MULTI = "Y"

        const val UPDATE_NAME = "전공 상담 수정"
        const val UPDATE_DESCRIPTION1 = "description1 수정"
        const val UPDATE_DESCRIPTION2 = "description2 수정"
        const val UPDATE_STU_TYPE = "F"
        const val UPDATE_IS_VISIBLE = "Y"
        const val UPDATE_IS_PORTFOLIO = false
        const val UPDATE_IS_STUDENT_VISIBLE = "N"
        const val UPDATE_IS_STUDENT_INPUT = "N"
        const val UPDATE_IS_MULTI = "N"

        const val DEFAULT_IS_VISIBLE = "Y"
        const val DEFAULT_IS_PORTFOLIO = false
        const val DEFAULT_IS_STUDENT_VISIBLE = "N"
        const val DEFAULT_IS_STUDENT_INPUT = "N"
        const val DEFAULT_IS_MULTI = "N"
    }

    @BeforeEach
    fun setUp() {
    }

    @PostConstruct
    @Profile("dev") // CI 환경에서만 실행
    fun init() {
        val category = Category(NAME)
        categoryRepository.save(category)
    }

    @DisplayName("integration: 마일리지 항목 생성")
    @Test
    fun createSubitem() {
        // Given
        val form = createDefaultItemForm(NAME)
        val req = mapper.writeValueAsString(form)

        // When
        val mvcResult = mockMvc
            .post("/api/mileage/items") {
                contentType = MediaType.APPLICATION_JSON
                content = req
            }
            .andExpect { status { isCreated() } }
            .andDo { print() }
            .andReturn()

        val res = mapper.readValue(
            mvcResult.response.contentAsString,
            object : TypeReference<Map<String, Int>>() {}
        )
        // Then
        assertWithDefaultValue(res)
    }

    @DisplayName("integration: 마일리지 항목 전체 조회")
    @Test
    fun getSubitems() {
        // Given
        val form1 = createDefaultItemForm(NAME)
        val form2 = createDefaultItemForm("중복 이름 방지")

        val req1 = mapper.writeValueAsString(form1)
        val req2 = mapper.writeValueAsString(form2)

        mockMvc
            .post("/api/mileage/items") {
                contentType = MediaType.APPLICATION_JSON
                content = req1
            }
            .andExpect { status { isCreated() } }
            .andReturn()
        mockMvc
            .post("/api/mileage/items") {
                contentType = MediaType.APPLICATION_JSON
                content = req2
            }
            .andExpect { status { isCreated() } }
            .andReturn()

        // When
        val mvcResult = mockMvc
            .get("/api/mileage/items")
            .andExpect { status { isOk() } }
            .andDo { print() }
            .andReturn()

        val res = mapper.readValue(
            mvcResult.response.contentAsString,
            ItemDto::class.java
        )

        // Then
        assertThat(res.count).isEqualTo(2)
    }

    @DisplayName("integration: 마일리지 항목 수정")
    @Test
    fun modifySubitem() {
        // Given
        val form = createDefaultItemForm(NAME)
        val req = mapper.writeValueAsString(form)
        val modifyForm = createUpdateItemForm()
        val req2 = mapper.writeValueAsString(modifyForm)

        mockMvc
            .post("/api/mileage/items") {
                contentType = MediaType.APPLICATION_JSON
                content = req
            }
            .andExpect { status { isCreated() } }
            .andReturn()

        val id = itemRepository.findByName(NAME)?.id

        // When
        mockMvc
            .patch("/api/mileage/items/$id") {
                contentType = MediaType.APPLICATION_JSON
                content = req2
            }

        // Then
        assertWithUpdateValue(id)
    }

    @DisplayName("integration: 마일리지 항목 삭제")
    @Test
    fun deleteSubitem() {
        // Given
        val form = createDefaultItemForm("test1")
        val req = mapper.writeValueAsString(form)

        mockMvc
            .post("/api/mileage/items") {
                contentType = MediaType.APPLICATION_JSON
                content = req
            }
            .andExpect { status { isCreated() } }
            .andDo { print() }
            .andReturn()

        val id = itemRepository.findTopByOrderByIdDesc()?.id ?: 0

        // When
        mockMvc
            .delete("/api/mileage/items/$id")
            .andExpect { status { isOk() } }
            .andDo { print() }
            .andReturn()

        // Then
        assertThat(itemRepository.findById(id)).isEmpty
    }

    @DisplayName("service: category_id가 음수인 경우")
    @Test
    fun mileageItemIntegrationTests_202() {
        // Given
        val form = createFormCategoryIdNegative()
        val req = mapper.writeValueAsString(form)

        // When
        val mvcResult = mockMvc
            .post("/api/mileage/items") {
                contentType = MediaType.APPLICATION_JSON
                content = req
            }
            .andExpect { status { isBadRequest() } }
            .andDo { print() }
            .andReturn()

        val res = mapper.readValue(
            mvcResult.response.contentAsString,
            ExceptionResponse::class.java
        )
        // Then
        assertThat(res.error).isEqualTo(HttpStatus.BAD_REQUEST.reasonPhrase)
        assertThat(getExceptionMessage(mvcResult)).isEqualTo(ExceptionMessage.ITEM_CATEGORY_ID_NOT_POSITIVE)
    }

    @DisplayName("service: itemName이 null인 경우")
    @Test
    fun mileageItemIntegrationTests_228() {
        // Given
        val form = createFormItemNameNull()
        val req = mapper.writeValueAsString(form)

        // When
        val mvcResult = mockMvc
            .post("/api/mileage/items") {
                contentType = MediaType.APPLICATION_JSON
                content = req
            }
            .andExpect { status { isBadRequest() } }
            .andDo { print() }
            .andReturn()

        val res = mapper.readValue(
            mvcResult.response.contentAsString,
            ExceptionResponse::class.java
        )
        // Then
        assertThat(res.error).isEqualTo(HttpStatus.BAD_REQUEST.reasonPhrase)
        assertThat(getExceptionMessage(mvcResult)).isEqualTo(ExceptionMessage.ITEM_NAME_IS_EMPTY)
    }

    @DisplayName("service: itemName이 blank인 경우")
    @Test
    fun mileageItemIntegrationTests_254() {
        // Given
        val form = createFormItemNameBlank()
        val req = mapper.writeValueAsString(form)

        // When
        val mvcResult = mockMvc
            .post("/api/mileage/items") {
                contentType = MediaType.APPLICATION_JSON
                content = req
            }
            .andExpect { status { isBadRequest() } }
            .andDo { print() }
            .andReturn()

        val res = mapper.readValue(
            mvcResult.response.contentAsString,
            ExceptionResponse::class.java
        )
        // Then
        assertThat(res.error).isEqualTo(HttpStatus.BAD_REQUEST.reasonPhrase)
        assertThat(getExceptionMessage(mvcResult)).isEqualTo(ExceptionMessage.ITEM_NAME_IS_EMPTY)
    }

    @DisplayName("integration: service 의존성 주입")
    @Test
    fun getSubitemService() {
        assertThat(subitemController.itemService).isNotNull
    }

    @DisplayName("integration: query service 의존성 주입")
    @Test
    fun getSubitemQueryService() {
        assertThat(subitemController.itemQueryService).isNotNull
    }

    fun createDefaultItemForm(itemName: String): ItemForm {
        val categoryId = categoryRepository.findTopByOrderByIdDesc()?.id!!
        return ItemForm(
            categoryId = categoryId,
            itemName = itemName,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2,
            stuType = STU_TYPE,
            itemMaxPoints = 10f,
            ItemForm.Flag(
                isVisible = stringToBoolean(IS_VISIBLE),
                isPortfolio = IS_PORTFOLIO,
                isMultiple = stringToBoolean(IS_MULTI),
                isStudentVisible = stringToBoolean(IS_STUDENT_VISIBLE),
                isStudentEditable = stringToBoolean(IS_STUDENT_INPUT)
            )
        )
    }

    fun createUpdateItemForm(): ItemForm {
        val categoryId = categoryRepository.findTopByOrderByIdDesc()?.id!!
        return ItemForm(
            categoryId = categoryId,
            itemName = UPDATE_NAME,
            description1 = UPDATE_DESCRIPTION1,
            description2 = UPDATE_DESCRIPTION2,
            stuType = UPDATE_STU_TYPE,
            itemMaxPoints = 5f,
            ItemForm.Flag(
                isVisible = stringToBoolean(UPDATE_IS_VISIBLE),
                isPortfolio = UPDATE_IS_PORTFOLIO,
                isMultiple = stringToBoolean(UPDATE_IS_MULTI),
                isStudentVisible = stringToBoolean(UPDATE_IS_STUDENT_VISIBLE),
                isStudentEditable = stringToBoolean(UPDATE_IS_STUDENT_INPUT)
            )
        )
    }

    fun createFormCategoryIdNegative(): ItemForm {
        return ItemForm(
            categoryId = -1,
            itemName = UPDATE_NAME,
            description1 = UPDATE_DESCRIPTION1,
            description2 = UPDATE_DESCRIPTION2,
            stuType = UPDATE_STU_TYPE,
            itemMaxPoints = 5f,
            ItemForm.Flag(
                isVisible = stringToBoolean(UPDATE_IS_VISIBLE),
                isPortfolio = UPDATE_IS_PORTFOLIO,
                isMultiple = stringToBoolean(UPDATE_IS_MULTI),
                isStudentVisible = stringToBoolean(UPDATE_IS_STUDENT_VISIBLE),
                isStudentEditable = stringToBoolean(UPDATE_IS_STUDENT_INPUT)
            )
        )
    }

    fun createFormItemNameNull(): ItemForm {
        val categoryId = categoryRepository.findTopByOrderByIdDesc()?.id!!
        return ItemForm(
            categoryId = categoryId,
            itemName = null,
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2,
            stuType = STU_TYPE,
            itemMaxPoints = 10f,
            ItemForm.Flag(
                isVisible = stringToBoolean(IS_VISIBLE),
                isPortfolio = IS_PORTFOLIO,
                isMultiple = stringToBoolean(IS_MULTI),
                isStudentVisible = stringToBoolean(IS_STUDENT_VISIBLE),
                isStudentEditable = stringToBoolean(IS_STUDENT_INPUT)
            )
        )
    }

    fun createFormItemNameBlank(): ItemForm {
        val categoryId = categoryRepository.findTopByOrderByIdDesc()?.id!!
        return ItemForm(
            categoryId = categoryId,
            itemName = "",
            description1 = DESCRIPTION1,
            description2 = DESCRIPTION2,
            stuType = STU_TYPE,
            itemMaxPoints = 10f,
            ItemForm.Flag(
                isVisible = stringToBoolean(IS_VISIBLE),
                isPortfolio = IS_PORTFOLIO,
                isMultiple = stringToBoolean(IS_MULTI),
                isStudentVisible = stringToBoolean(IS_STUDENT_VISIBLE),
                isStudentEditable = stringToBoolean(IS_STUDENT_INPUT)
            )
        )
    }

    fun assertWithDefaultValue(res: Map<String, Int>) {
        itemRepository.findById(res["id"]!!).ifPresent {
            assertThat(it.name).isEqualTo(NAME)
            assertThat(it.isPortfolio).isEqualTo(IS_PORTFOLIO)
            assertThat(it.description1).isEqualTo(DESCRIPTION1)
            assertThat(it.description2).isEqualTo(DESCRIPTION2)
            assertThat(it.stuType).isEqualTo(STU_TYPE)
            assertThat(it.isVisible).isEqualTo(IS_VISIBLE)
            assertThat(it.isMulti).isEqualTo(IS_MULTI)
            assertThat(it.isStudentVisible).isEqualTo(IS_STUDENT_VISIBLE)
            assertThat(it.isStudentInput).isEqualTo(IS_STUDENT_INPUT)
        }
    }

    fun assertWithUpdateValue(id: Int?) {
        itemRepository.findById(id!!).ifPresent {
            assertThat(it.name).isEqualTo(UPDATE_NAME)
            assertThat(it.isPortfolio).isEqualTo(UPDATE_IS_PORTFOLIO)
            assertThat(it.description1).isEqualTo(UPDATE_DESCRIPTION1)
            assertThat(it.description2).isEqualTo(UPDATE_DESCRIPTION2)
            assertThat(it.stuType).isEqualTo(UPDATE_STU_TYPE)
            assertThat(it.isVisible).isEqualTo(UPDATE_IS_VISIBLE)
            assertThat(it.isMulti).isEqualTo(UPDATE_IS_MULTI)
            assertThat(it.isStudentVisible).isEqualTo(UPDATE_IS_STUDENT_VISIBLE)
            assertThat(it.isStudentInput).isEqualTo(UPDATE_IS_STUDENT_INPUT)
        }
    }
}
