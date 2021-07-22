package com.springboard.zzatmari.src.goal;

import com.springboard.zzatmari.src.goal.model.GetGoalsRes;
import com.springboard.zzatmari.src.goal.model.GoalLists;
import com.springboard.zzatmari.src.goal.model.PostGoalReq;
import com.springboard.zzatmari.src.list.model.GetListsRes;
import com.springboard.zzatmari.src.user.model.PatchUserReq;
import com.springboard.zzatmari.src.user.model.PostUserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class GoalDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //목표조회
    public GetGoalsRes selectGoals(int userIdx){

        //목표 리스트 조회
        String selectGoalsQuery = "SELECT L.idx listIdx, L.listItem, IFNULL(G.goalTime,0) time\n" +
                "FROM  List L LEFT JOIN Goal G ON G.listIdx=L.idx\n" +
                "WHERE L.userIdx=? AND listType=? AND L.status=0";
        Object[] selectGoalsParams1 = new Object[]{userIdx, 0};
        Object[] selectGoalsParams2 = new Object[]{userIdx, 1};

        //초기화 여부 체크
        String selectGoalResetCheckQuery = "SELECT count(*) count\n" +
                "    FROM Goal G\n" +
                "    JOIN User U on U.idx=G.userIdx\n" +
                "WHERE (((DATE_FORMAT(now(), '%H') < U.dayStartHour)\n" +
                "AND (G.updatedAt  BETWEEN CONCAT(DATE_FORMAT(CURDATE()-1,'%Y-%m-%d'),' ',TIME_FORMAT(CONCAT(U.dayStartHour, ':', U.dayStartMinute, ':00' ), '%H:%i:%S'))\n" +
                "    AND CONCAT(DATE_FORMAT(CURDATE(),'%Y-%m-%d'),' ',TIME_FORMAT(CONCAT(U.dayStartHour, ':', U.dayStartMinute, ':00' ), '%H:%i:%S')))) OR\n" +
                "((DATE_FORMAT(now(), '%H') >= U.dayStartHour)\n" +
                "AND (G.updatedAt  BETWEEN CONCAT(DATE_FORMAT(CURDATE(),'%Y-%m-%d'),' ',TIME_FORMAT(CONCAT(U.dayStartHour, ':', U.dayStartMinute, ':00' ), '%H:%i:%S'))\n" +
                "    AND CONCAT(DATE_FORMAT(CURDATE()+1,'%Y-%m-%d'),' ',TIME_FORMAT(CONCAT(U.dayStartHour, ':', U.dayStartMinute, ':00' ), '%H:%i:%S'))))) AND userIdx=?";
        Object[] selectGoalResetCheckParams = new Object[]{userIdx};

        List<GoalLists> result1 = this.jdbcTemplate.query(selectGoalsQuery,
                (rs,rowNum)-> new GoalLists(
                        rs.getInt("listIdx"),
                        rs.getString("listItem"),
                        rs.getInt("time")
                ),selectGoalsParams1
        );

        List<GoalLists> result2 = this.jdbcTemplate.query(selectGoalsQuery,
                (rs,rowNum)-> new GoalLists(
                        rs.getInt("listIdx"),
                        rs.getString("listItem"),
                        rs.getInt("time")
                ),selectGoalsParams2
        );

        boolean resetCheck = this.jdbcTemplate.queryForObject(selectGoalResetCheckQuery,
                boolean.class
                ,selectGoalResetCheckParams);

        return new GetGoalsRes(resetCheck, result1, result2);
    }

    //목표 중복체크
    public int checkGoal(int userIdx,int listIdx){
        String checkGoalsQuery = "SELECT EXISTS(SELECT listIdx FROM Goal WHERE listIdx=? AND userIdx=?)";
        Object[] checkGoalsParams = new Object[]{listIdx, userIdx};
        return this.jdbcTemplate.queryForObject(checkGoalsQuery,
                int.class,
                checkGoalsParams);

    }

    //목표 시간 체크
    public int checkGoalTime(int listIdx){
        String checkGoalTimeQuery = "SELECT goalTime FROM Goal WHERE listIdx=?";
        Object[] checkGoalTimeParams = new Object[]{listIdx};
        return this.jdbcTemplate.queryForObject(checkGoalTimeQuery,
                int.class,
                checkGoalTimeParams);
    }

    //목표 업데이트
    public int updateGoal(int userIdx, PostGoalReq postGoalReq){
        String updateGoalQuery = "UPDATE Goal SET goalTime=? WHERE userIdx=? AND listIdx=?";
        Object[] updateGoalParams = new Object[]{postGoalReq.getTime(), userIdx, postGoalReq.getListIdx()};

        return this.jdbcTemplate.update(updateGoalQuery,updateGoalParams);
    }

    //목표 추가
    public int insertGoal(int userIdx, PostGoalReq postGoalReq){
        String insertGoalQuery = "INSERT INTO Goal(userIdx, listIdx, goalTime) VALUES(?,?,?)";
        Object[] insertGoalParams = new Object[]{userIdx, postGoalReq.getListIdx(), postGoalReq.getTime()};
        return this.jdbcTemplate.update(insertGoalQuery, insertGoalParams);
    }

    //목표 초기화
    public int resetGoals(int userIdx){
        String resetGoalsQuery = "UPDATE Goal SET goalTime=0 WHERE userIdx=?";
        Object[] resetGoalsParams = new Object[]{userIdx};

        return this.jdbcTemplate.update(resetGoalsQuery, resetGoalsParams);
    }
}
