package com.springboard.zzatmari.src.user;


import com.springboard.zzatmari.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //하루 시작시간 설정
    public int updateUserTime(PostUserTimeReq postUserTimeReq, int userIdx){
        String updateUserTimeQuery = "UPDATE User SET dayStartHour=?, dayStartMinute=? WHERE idx=?;";

        Object[] updateUserTimeParams = new Object[]{postUserTimeReq.getHour(), postUserTimeReq.getMinute(), userIdx};

        return this.jdbcTemplate.update(updateUserTimeQuery,updateUserTimeParams);
    }

    //씨앗창고조회
    public List<GetUserSeedRes> selectUserSeeds(int userIdx){
        String selectUserSeedsQuery = "SELECT US.seedIdx seedIdx, seedName, seedImgUrl\n" +
                "FROM UserSeed US\n" +
                "JOIN SeedInfo S ON S.idx=US.seedIdx\n" +
                "WHERE US.userIdx=? AND US.status=0\n" +
                "GROUP BY seedIdx\n" +
                "ORDER BY S.idx";

        int selectUserSeedsParams = userIdx;
        return this.jdbcTemplate.query(selectUserSeedsQuery,
                (rs,rowNum) -> new GetUserSeedRes(
                        rs.getInt("seedIdx"),
                        rs.getString("seedName"),
                        rs.getString("seedImgUrl")), selectUserSeedsParams
        );
    }

    //기기번호 체크
    public UserDeviceId checkDeviceId(String token){
        String checkDeviceIdQuery = "SELECT count(*) count, ifnull(idx,0) userIdx, deviceId FROM User WHERE deviceId=?";
        String checkDeviceIdParams = token;
        return this.jdbcTemplate.queryForObject(checkDeviceIdQuery,
                (rs,rowNum) -> new UserDeviceId(
                        rs.getInt("count"),
                        rs.getInt("userIdx"),
                        rs.getString("deviceId")),
                checkDeviceIdParams);

    }

    //비회원 회원가입
    public int createUnknownUser(String deviceId){
        String createUnknownUserQuery = "insert into User (deviceId) VALUES (?)";
        Object[] createUnknownUserParams = new Object[]{deviceId};
        this.jdbcTemplate.update(createUnknownUserQuery, createUnknownUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }


    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select * from UserInfo";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password"))
                );
    }

    public List<GetUserRes> getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from UserInfo where email =?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUsersByEmailParams);
    }

    public GetUserRes getUser(int userIdx){
        String getUserQuery = "select * from UserInfo where userIdx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUserParams);
    }
    

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into UserInfo (userName, ID, password, email) VALUES (?,?,?,?)";
        //Object[] createUserParams = new Object[]{postUserReq.getUserName(), postUserReq.getId(), postUserReq.getPassword(), postUserReq.getEmail()};
        this.jdbcTemplate.update(createUserQuery);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from UserInfo where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update UserInfo set userName = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserName(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userIdx, password,email,userName,ID from UserInfo where ID = ?";
        String getPwdParams = postLoginReq.getId();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("ID"),
                        rs.getString("userName"),
                        rs.getString("password"),
                        rs.getString("email")
                ),
                getPwdParams
                );

    }



}
