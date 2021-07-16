package com.springboard.zzatmari.src.stat;

import com.springboard.zzatmari.src.stat.model.GetStatsRes;
import com.springboard.zzatmari.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class StatDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

    }

    public List<GetStatsRes> selectStats(int userIdx, int year, int month){
        String selectStatsQuery = "SELECT DAY(executionDate) day, case when time>100 then 100 else time end percent\n" +
                "FROM (SELECT TRUNCATE((SUM(E.min)/E.goalTime)*100,0) time, executionDate, listIdx FROM Execution E WHERE status=2 GROUP BY executionDate) E\n" +
                "JOIN List L on L.idx=E.listIdx\n" +
                "JOIN User U on U.idx=L.userIdx\n" +
                "WHERE U.idx=? AND YEAR(executionDate)=? AND MONTH(executionDate)=? AND time>0 ORDER BY day";
        Object[] selectStatsParams = new Object[]{userIdx, year, month};
        return this.jdbcTemplate.query(selectStatsQuery,
                (rs, rowNum) -> new GetStatsRes(
                        rs.getInt("day"),
                        rs.getInt("percent")),
                        selectStatsParams);
    }
}
