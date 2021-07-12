package com.springboard.zzatmari.src.execution;

import com.springboard.zzatmari.src.goal.model.PostGoalReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ExecutionDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //실행 추가
    public int insertExecution(int userIdx, int listIdx, int time){
        String insertExecutionQuery = "INSERT INTO Execution(userIdx, listIdx, min) VALUES(?,?,?)";
        Object[] insertExecutionParams = new Object[]{userIdx, listIdx, time};
        this.jdbcTemplate.update(insertExecutionQuery, insertExecutionParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

}