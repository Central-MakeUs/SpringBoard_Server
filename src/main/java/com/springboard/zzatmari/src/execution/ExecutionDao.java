package com.springboard.zzatmari.src.execution;

import com.springboard.zzatmari.src.execution.model.Execution;
import com.springboard.zzatmari.src.execution.model.GetExecutionRes;
import com.springboard.zzatmari.src.execution.model.PatchExecutionReq;
import com.springboard.zzatmari.src.goal.model.PostGoalReq;
import com.springboard.zzatmari.src.list.model.GetListsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ExecutionDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //실행 추가
    public int insertExecution(int userIdx, int listIdx, int time){
        String insertExecutionQuery = "INSERT INTO Execution(userIdx, listIdx, timer) VALUES(?,?,?)";
        Object[] insertExecutionParams = new Object[]{userIdx, listIdx, time};
        this.jdbcTemplate.update(insertExecutionQuery, insertExecutionParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    //실행 조회
    public GetExecutionRes selectExecution(int userIdx){

        String selectExecutionQuery = "SELECT case when sec=0 then timer-min else timer-min-1 end min,\n" +
                "case when sec=0 then 0 else 60-sec end sec, status\n" +
                "FROM Execution WHERE userIdx=? AND (status=0 OR status=1)";
        Object[] selectExecutionParams = new Object[]{userIdx};

        return this.jdbcTemplate.queryForObject(selectExecutionQuery,
                (rs,rowNum)-> new GetExecutionRes(
                        rs.getInt("min"),
                        rs.getInt("sec"),
                        rs.getInt("status")
                    ),
                    selectExecutionParams
            );
        }


    //실행중 체크
    public int checkExecution(int userIdx){
        String checkExecutionQuery = "SELECT EXISTS(SELECT idx FROM Execution WHERE userIdx=? AND (status=0 OR status=1))";
        Object[] checkExecutionParams = new Object[]{userIdx};
        return this.jdbcTemplate.queryForObject(checkExecutionQuery,
                int.class,
                checkExecutionParams);

    }

    //실행 일시정지
    public int pauseExecution(int userIdx, int min, int sec){
        String pauseExecutionQuery = "UPDATE Execution SET status=1,min=?,sec=? WHERE userIdx=? AND status=0;";
        Object[] pauseExecutionParams = new Object[]{min, sec, userIdx};

        return this.jdbcTemplate.update(pauseExecutionQuery, pauseExecutionParams);
    }

    //실행 재개
    public int continueExecution(int userIdx){
        String pauseExecutionQuery = "UPDATE Execution SET status=0 WHERE userIdx=? AND status=1;";
        Object[] pauseExecutionParams = new Object[]{userIdx};

        return this.jdbcTemplate.update(pauseExecutionQuery, pauseExecutionParams);
    }

    //실행 완료
    public int completeExecution(int userIdx, int min, int sec){
        String pauseExecutionQuery = "UPDATE Execution SET status=2,min=?,sec=? WHERE userIdx=? AND (status=0 OR status=1);";
        Object[] pauseExecutionParams = new Object[]{min, sec, userIdx};

        return this.jdbcTemplate.update(pauseExecutionQuery, pauseExecutionParams);
    }

    //실행 상세조회
    public Execution selectExecutionDetail(int userIdx){

        String selectExecutionDetailQuery = "SELECT min, sec, status, timer" +
                " FROM Execution WHERE userIdx=? AND (status=0 OR status=1)";
        Object[] selectExecutionDetailParams = new Object[]{userIdx};

        return this.jdbcTemplate.queryForObject(selectExecutionDetailQuery,
                (rs,rowNum)-> new Execution(
                        rs.getInt("min"),
                        rs.getInt("sec"),
                        rs.getInt("status"),
                        rs.getInt("timer")
                ),
                selectExecutionDetailParams
        );
    }
}