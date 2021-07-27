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
        String updateExecutionDateQuery = "UPDATE Execution SET executionDate=DATE_FORMAT(CURDATE()-1, '%Y-%m-%d %H:%i:%S') WHERE userIdx=? AND DATE_FORMAT(updatedAt, '%Y-%m-%d') = DATE_FORMAT(now(), '%Y-%m-%d') AND TIME_FORMAT(updatedAt, '%H:%i') < TIME_FORMAT('"+ postUserTimeReq.getHour()+":"+ postUserTimeReq.getMinute()+"', '%H:%i')\n";
        System.out.println(updateExecutionDateQuery);
        Object[] updateUserTimeParams = new Object[]{postUserTimeReq.getHour(), postUserTimeReq.getMinute(), userIdx};
        Object[] updateExecutionDateParams = new Object[]{userIdx};

        this.jdbcTemplate.update(updateExecutionDateQuery,updateExecutionDateParams);
        return this.jdbcTemplate.update(updateUserTimeQuery,updateUserTimeParams);
    }

    //하루 시작시간 체크
    public String checkUserTime(int userIdx){
        String checkUserTimeQuery = "select case when U.dayStartHour <= DATE_FORMAT(now(), '%H') then DATE_FORMAT(CURDATE(),'%Y-%m-%d %H:%i:%S') else DATE_FORMAT(CURDATE()-1,'%Y-%m-%d %H:%i:%S') end date from User U where idx=?";

        Object[] checkUserTimeParams = new Object[]{userIdx};

        return this.jdbcTemplate.queryForObject(checkUserTimeQuery, String.class, checkUserTimeParams);
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

    //사용자 기본값 설정
        public void insertUserDefault(int userIdx){
        String createUserDefaultQuery = "insert into Timer(userIdx, timer) VALUES(?, ?)";
        int[] timerList = {1, 5, 10, 30, 60};

        for(int i = 0; i<timerList.length; i++){
            Object[] createUserDefaultParams = new Object[]{userIdx, timerList[i]};
            this.jdbcTemplate.update(createUserDefaultQuery, createUserDefaultParams);
        }

        return;
    }

    //사용자 이메일 체크
    public UserEmail checkUserEmail(String email){
        String checkUserEmailQuery = "SELECT count(*) count, ifnull(idx,0) userIdx, email, password, loginType FROM User WHERE email=?";
        String checkUserEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkUserEmailQuery,
                (rs,rowNum) -> new UserEmail(
                        rs.getInt("count"),
                        rs.getInt("userIdx"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("loginType")),
                checkUserEmailParams);

    }


    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select * from UserInfo";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getString("Email"))
                );
    }

    public List<GetUserRes> getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from UserInfo where email =?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getString("Email")),
                getUsersByEmailParams);
    }

    public GetUserRes getUser(int userIdx){
        String getUserQuery = "select email from User where idx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getString("email")),
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
