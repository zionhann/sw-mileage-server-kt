package edu.handong.cseemileage.db

import edu.handong.cseemileage.config.DataBaseProperties
import org.hibernate.validator.internal.util.Contracts.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import java.sql.Connection
import java.sql.DriverManager

@SpringBootTest
@Profile("prod")
class DBConnectionTests {

    @Autowired
    lateinit var datasource: DataBaseProperties

    @Test
    fun connectToDatabase() {
        val connection: Connection? = DriverManager.getConnection(
            datasource.url,
            datasource.username,
            datasource.password
        )
        assertNotNull(connection, "Connection should not be null")
        connection?.close()
    }
}
