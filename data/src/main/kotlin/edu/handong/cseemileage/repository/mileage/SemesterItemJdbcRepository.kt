package edu.handong.cseemileage.repository.mileage

import edu.handong.cseemileage.domain.mileage.SemesterItem
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.PreparedStatement
import java.sql.Timestamp
import java.time.LocalDateTime

@Repository
class SemesterItemJdbcRepository(private val jdbcTemplate: JdbcTemplate) {

    /**
     * @GeneratedValue(strategy = GenerationType.IDENTITY) 전략의 한계점으로 인해 JDBC template을 활용해 직접 bulk insert 수행
     * */
    fun insertSemesterList(semesterList: List<SemesterItem>) {
        jdbcTemplate.batchUpdate(
            "insert into _sw_mileage_semester_item (point_value, item_max_points, semester_name, item_id, category_id, mod_date, reg_date) values (?, ?, ?, ?, ?, ?, ?, ?)",
            object : BatchPreparedStatementSetter {
                override fun setValues(ps: PreparedStatement, i: Int) {
                    ps.setString(1, semesterList[i].pointValue.toString())
                    ps.setString(2, semesterList[i].itemMaxPoints.toString())
                    ps.setString(3, semesterList[i].semesterName)
                    ps.setString(4, semesterList[i].item.id.toString())
                    ps.setString(5, semesterList[i].category.id.toString())

                    val now = LocalDateTime.now()
                    ps.setTimestamp(7, Timestamp.valueOf(now))
                    ps.setTimestamp(8, Timestamp.valueOf(now))
                }

                override fun getBatchSize(): Int {
                    return semesterList.size
                }
            }
        )
    }
}
