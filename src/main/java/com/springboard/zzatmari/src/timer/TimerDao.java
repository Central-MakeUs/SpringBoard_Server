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
        String selectTimersQuery = "SELECT T.idx timerIdx, hour, minute,\n" +
                "       DATE_FORMAT(DATE_ADD(DATE_ADD(now(), INTERVAL hour HOUR), INTERVAL minute MINUTE), '%H:%i') time\n" +
                "FROM (SELECT idx, ifnull(case when timer >= 60 then TRUNCATE(timer/60, 0) end, 0) hour, timer%60 minute FROM Timer WHERE userIdx=?) T\n" +
                "ORDER BY time";
        Object[] selectTimersParams = new Object[]{userIdx};

        return this.jdbcTemplate.query(selectTimersQuery,
                (rs,rowNum)-> new GetTimersRes(
                        rs.getInt("timerIdx"),
                        rs.getInt("hour"),
                        rs.getInt("minute"),
                        rs.getString("time")
                ),
                selectTimersParams
        );
    }

    //타이머 중복체크
    public int checkTimer(int userIdx, int time){
        String checkTimerQuery = "SELECT EXISTS(SELECT idx FROM Timer WHERE userIdx=? AND timer = ?)";
        Object[] checkTimerParams = new Object[]{userIdx, time};
        return this.jdbcTemplate.queryForObject(checkTimerQuery,
                int.class,
                checkTimerParams);
    }

    //타이머 추가
    public int insertTimer(int userIdx, int time){
        String insertTimerQuery = "INSERT INTO Timer(userIdx, timer) VALUES(?, ?)";

        Object[] insertTimerParams = new Object[]{userIdx, time};
        this.jdbcTemplate.update(insertTimerQuery, insertTimerParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    //타이머 수정
    public int updateTimer(int timerIdx, int time){
        String updateTimerQuery = "UPDATE Timer Set timer=? WHERE idx=?";

        Object[] updateTimerParams = new Object[]{time, timerIdx};
        return this.jdbcTemplate.update(updateTimerQuery, updateTimerParams);
    }
}
