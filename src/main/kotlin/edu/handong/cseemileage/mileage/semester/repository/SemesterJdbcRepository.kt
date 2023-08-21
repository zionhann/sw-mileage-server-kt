package edu.handong.cseemileage.mileage.semester.repository

import edu.handong.cseemileage.mileage.semester.domain.Semester
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.PreparedStatement

@Repository
class SemesterJdbcRepository(private val jdbcTemplate: JdbcTemplate) {

    /**
     * @GeneratedValue(strategy = GenerationType.IDENTITY) 전략의 한계점으로 인해 JDBC template을 활용해 직접 bulk insert 수행
     * */
    fun insertSemesterList(semesterList: List<Semester>) {
        jdbcTemplate.batchUpdate(
            "insert into semester (weight, max_points, name, subitem_id, category_id) values (?, ?, ?, ?, ?)",
            object : BatchPreparedStatementSetter {
                override fun setValues(ps: PreparedStatement, i: Int) {
                    ps.setString(1, semesterList[i].weight.toString())
                    ps.setString(2, semesterList[i].maxPoints.toString())
                    ps.setString(3, semesterList[i].name)
                    ps.setString(4, semesterList[i].item.id.toString())
                    ps.setString(5, semesterList[i].category.id.toString())
                }

                override fun getBatchSize(): Int {
                    return semesterList.size
                }
            }
        )
    }
}
