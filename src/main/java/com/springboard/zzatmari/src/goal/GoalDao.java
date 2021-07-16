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

        String selectGoalsQuery = "SELECT L.idx listIdx, L.listItem, IFNULL(G.goalTime,0) time\n" +
                "FROM  List L LEFT JOIN Goal G ON G.listIdx=L.idx\n" +
                "WHERE L.userIdx=? AND listType=? AND L.status=0";
        Object[] selectGoalsParams1 = new Object[]{userIdx, 0};
        Object[] selectGoalsParams2 = new Object[]{userIdx, 1};

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

        return new GetGoalsRes(result1, result2);
    }

    //목표 중복체크
    public int checkGoal(int userIdx,int listIdx){
        String checkGoalsQuery = "SELECT EXISTS(SELECT ListIdx FROM Goal WHERE listIdx=? AND userIdx=?)";
        Object[] checkGoalsParams = new Object[]{listIdx, userIdx};
        return this.jdbcTemplate.queryForObject(checkGoalsQuery,
                int.class,
                checkGoalsParams);

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
}
