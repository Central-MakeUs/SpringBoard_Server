package com.springboard.zzatmari.src.goal;

import com.springboard.zzatmari.src.goal.model.GetGoalsRes;
import com.springboard.zzatmari.src.list.model.GetListsRes;
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

        String selectGoalsQuery = "SELECT L.idx listIdx, L.listItem, G.goalTime time\n" +
                "FROM Goal G JOIN List L ON G.listIdx=L.idx\n" +
                "WHERE G.userIdx=? AND listType=?";
        Object[] selectGoalsParams1 = new Object[]{userIdx, 0};
        Object[] selectGoalsParams2 = new Object[]{userIdx, 1};

        List<GetListsRes> result1 = this.jdbcTemplate.query(selectGoalsQuery,
                (rs,rowNum)-> new GetListsRes(
                        rs.getInt("listIdx"),
                        rs.getString("listItem"),
                        rs.getInt("time")
                ),selectGoalsParams1
        );

        List<GetListsRes> result2 = this.jdbcTemplate.query(selectGoalsQuery,
                (rs,rowNum)-> new GetListsRes(
                        rs.getInt("listIdx"),
                        rs.getString("listItem"),
                        rs.getInt("time")
                ),selectGoalsParams2
        );

        return new GetGoalsRes(result1, result2);
    }
}
