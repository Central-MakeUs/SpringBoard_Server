package com.springboard.zzatmari.src.list;

import com.springboard.zzatmari.src.goal.model.GetGoalsRes;
import com.springboard.zzatmari.src.list.model.GetListsRes;
import com.springboard.zzatmari.src.list.model.Lists;
import com.springboard.zzatmari.src.list.model.PatchListReq;
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
    public Lists checkListItem(int userIdx, String listItem){
        String checkListItemQuery = "select idx listIdx, count(*) count, ifnull(status, 0) status, ifnull(userIdx, 0) userIdx\n" +
                "from (select idx, status, userIdx from List where listItem=? and userIdx=?) L";
        Object[] checkListItemParams = new Object[]{listItem, userIdx};
        return this.jdbcTemplate.queryForObject(checkListItemQuery,
                (rs,rowNum)-> new Lists(
                        rs.getInt("listIdx"),
                        rs.getInt("count"),
                        rs.getInt("status"),
                        rs.getInt("userIdx")) ,checkListItemParams);
    }

    //리스트 존재여부
    public Lists checkListIdx(int listIdx){
        String checkListIdxQuery = "select idx listIdx, count(*) count, ifnull(status, 0) status, ifnull(userIdx, 0) userIdx\n" +
                "from (select idx, status, userIdx from List where idx=?) L";
        Object[] checkListIdxParams = new Object[]{listIdx};
        return this.jdbcTemplate.queryForObject(checkListIdxQuery,
                (rs,rowNum)-> new Lists(
                        rs.getInt("listIdx"),
                        rs.getInt("count"),
                        rs.getInt("status"),
                        rs.getInt("userIdx")),
                checkListIdxParams);
    }

    //리스트 전체조회 (디지털디톡스, 자기계발)
    public List<GetListsRes> selectLists(int userIdx, int type){
        String selectListsQuery = "SELECT L.idx listIdx, L.listItem, ifnull(E.min, 0) time, ifnull(goalTime, 0) goalTime\n" +
                "FROM List L\n" +
                "    LEFT JOIN Goal G on G.listIdx=L.idx\n" +
                "    LEFT JOIN (SELECT listIdx, TRUNCATE(SUM(min*60+sec)/60,0) min\n" +
                "FROM Execution E\n" +
                "    JOIN User U on U.idx=E.userIdx\n" +
                "WHERE (((DATE_FORMAT(now(), '%H') < U.dayStartHour)\n" +
                "AND (E.createdAt BETWEEN CONCAT(DATE_FORMAT(CURDATE()-1,'%Y-%m-%d'),' ',TIME_FORMAT(CONCAT(U.dayStartHour, ':', U.dayStartMinute, ':00' ), '%H:%i:%S'))\n" +
                "    AND CONCAT(DATE_FORMAT(CURDATE(),'%Y-%m-%d'),' ',TIME_FORMAT(CONCAT(U.dayStartHour, ':', U.dayStartMinute, ':00' ), '%H:%i:%S')))) OR\n" +
                "((DATE_FORMAT(now(), '%H') >= U.dayStartHour)\n" +
                "AND (E.createdAt BETWEEN CONCAT(DATE_FORMAT(CURDATE(),'%Y-%m-%d'),' ',TIME_FORMAT(CONCAT(U.dayStartHour, ':', U.dayStartMinute, ':00' ), '%H:%i:%S'))\n" +
                "    AND CONCAT(DATE_FORMAT(CURDATE()+1,'%Y-%m-%d'),' ',TIME_FORMAT(CONCAT(U.dayStartHour, ':', U.dayStartMinute, ':00' ), '%H:%i:%S')))))\n" +
                "AND E.status=2 GROUP BY listIdx) E on L.idx=E.listIdx\n" +
                "WHERE L.status=0 AND L.userIdx=? AND listType=?";
        Object[] selectListsParams = new Object[]{userIdx, type};

        return this.jdbcTemplate.query(selectListsQuery,
        (rs,rowNum)-> new GetListsRes(
                rs.getInt("listIdx"),
                rs.getString("listItem"),
                rs.getInt("time"),
                rs.getInt("goalTime")
        ),
                selectListsParams
        );
    }

    //리스트 수정
    public int updateList(int listIdx, PatchListReq patchListReq){
        String updateListQuery = "UPDATE List SET listItem=? WHERE idx=?";

        Object[] updateListParams = new Object[]{patchListReq.getListItem(), listIdx};
        return this.jdbcTemplate.update(updateListQuery, updateListParams);
    }

    //리스트 상태 살리기
    public int updateListStatus(int listIdx){
        String updateListStatusQuery = "UPDATE List SET status=0 WHERE idx=?";

        Object[] updateListStatusParams = new Object[]{listIdx};
        return this.jdbcTemplate.update(updateListStatusQuery, updateListStatusParams);

    }

    //리스트 상태 죽이기
    public int deleteList(int listIdx){
        String deleteListQuery = "UPDATE List SET status=1 WHERE idx=?";

        Object[] deleteListParams = new Object[]{listIdx};
        return this.jdbcTemplate.update(deleteListQuery, deleteListParams);

    }
}
