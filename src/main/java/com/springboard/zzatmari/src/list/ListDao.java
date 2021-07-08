package com.springboard.zzatmari.src.list;

import com.springboard.zzatmari.src.goal.model.GetGoalsRes;
import com.springboard.zzatmari.src.list.model.GetListsRes;
import com.springboard.zzatmari.src.list.model.PostListReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ListDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //리스트 등록
    public int insertLists(PostListReq postListReq, int userIdx){
        String insertListsQuery = "INSERT INTO List(userIdx, listItem, listType) VALUES(?, ?, ?)";

        //리스트 개별등록
        if(postListReq.getListItem() != null){
            Object[] insertListsParams = new Object[]{userIdx, postListReq.getListItem(), postListReq.getListType()};
            this.jdbcTemplate.update(insertListsQuery, insertListsParams);
        }
        else{ //리스트 한번에 여러개 등록
            for(int i = 0; i<postListReq.getListItems().size(); i++){
                Object[] insertListsParams = new Object[]{userIdx, postListReq.getListItems().get(i), postListReq.getListType()};
                this.jdbcTemplate.update(insertListsQuery, insertListsParams);
            }
        }

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    //리스트 아이템 중복체크
    public int checkListItem(int userIdx, String listItem){
        String checkListItemQuery = "select exists(select listItem from List where listItem=? and userIdx=? and status=0)";
        Object[] checkListItemParams = new Object[]{listItem, userIdx};
        return this.jdbcTemplate.queryForObject(checkListItemQuery,
                int.class,
                checkListItemParams);

    }

    //리스트 전체조회 (디지털디톡스, 자기계발)
    public List<GetListsRes> selectLists(int userIdx, int type){
        String selectListsQuery = "SELECT L.idx listIdx, L.listItem, ifnull(E.executionTime, 0) time FROM List L\n" +
                "LEFT JOIN (SELECT listIdx, executionTime\n" +
                "FROM Execution E JOIN User U on U.idx=E.userIdx\n" +
                "WHERE E.createdAt >= CONCAT(DATE_FORMAT(CURDATE(),'%Y-%m-%d'),' ',TIME_FORMAT(CONCAT(U.dayStartHour, ':', U.dayStartMinute, ':00' ), '%H:%i:%S'))\n" +
                "AND E.createdAt < CONCAT(DATE_FORMAT(CURDATE()+1,'%Y-%m-%d'),' ',TIME_FORMAT(CONCAT(U.dayStartHour, ':', U.dayStartMinute, ':00' ), '%H:%i:%S'))\n" +
                ") E on L.idx=E.listIdx\n" +
                "WHERE L.status=0 AND L.userIdx=? AND listType=?";
        Object[] selectListsParams = new Object[]{userIdx, type};

        return this.jdbcTemplate.query(selectListsQuery,
        (rs,rowNum)-> new GetListsRes(
                rs.getInt("listIdx"),
                rs.getString("listItem"),
                rs.getInt("time")
        ),
                selectListsParams
        );
    }
}
