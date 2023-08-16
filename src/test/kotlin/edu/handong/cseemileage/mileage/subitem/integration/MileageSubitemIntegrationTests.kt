package edu.handong.cseemileage.mileage.subitem.integration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MileageSubitemIntegrationTests(
    @Autowired val restTemplate: TestRestTemplate
) {

    @DisplayName("integration: create subitem controller test")
    @Test
    fun createSubitem() {
        val result =
            restTemplate.postForEntity("/subitem", SubitemRequest(1, "test", "2021-1"), String::class.java)
        assertThat(result).isNotNull
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }
}
