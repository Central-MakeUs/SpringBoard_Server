package com.springboard.zzatmari.src.timer;

import com.springboard.zzatmari.src.timer.model.GetTimersRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class TimerDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //타이머 전체조회
    public List<GetTimersRes> selectTimers(int userIdx){
        String selectTimersQuery = "SELECT hour, minute,\n" +
                "       DATE_FORMAT(DATE_ADD(DATE_ADD(now(), INTERVAL hour HOUR), INTERVAL minute MINUTE), '%H:%i') time\n" +
                "FROM (SELECT idx, ifnull(case when timer >= 60 then TRUNCATE(timer/60, 0) end, 0) hour, timer%60 minute FROM Timer WHERE userIdx=?) T\n" +
                "ORDER BY time";
        Object[] selectTimersParams = new Object[]{userIdx};

        return this.jdbcTemplate.query(selectTimersQuery,
                (rs,rowNum)-> new GetTimersRes(
                        rs.getInt("hour"),
                        rs.getInt("minute"),
                        rs.getString("time")
                ),
                selectTimersParams
        );
    }

}
