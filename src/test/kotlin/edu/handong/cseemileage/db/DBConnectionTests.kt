package edu.handong.cseemileage.db

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


@SpringBootTest
class DBConnectionTests {

    companion object {
        const val DB_URL: String = "jdbc:mysql://walab.handong.edu:3306/sw_mileage"
        const val DB_USER: String = "sw_mileage"
        const val DB_PASSWORD: String = "Q0IGqX92kEKBHX0L"
    }
    @Test
    fun connectToDatabase() {
        var connection: Connection? = null
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)
            assertNotNull(connection, "Connection should not be null")
            println("connection = $connection")
        } catch (e: SQLException) {
            fail("Connection failed: ${e.message}")
        } finally {
            connection?.close()
        }
    }
}