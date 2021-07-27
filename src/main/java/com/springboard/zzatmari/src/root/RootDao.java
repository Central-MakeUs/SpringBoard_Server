package com.springboard.zzatmari.src.root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class RootDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //이메일 회원가입
    public int insertUser(String email, String password){
        String insertUserQuery = "INSERT INTO User(email, password, loginType) VALUES(?,?,1)";

        Object[] insertUserParams = new Object[]{email, password};

        this.jdbcTemplate.update(insertUserQuery, insertUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }
}