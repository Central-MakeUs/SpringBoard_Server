package com.springboard.zzatmari.src.plant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class PlantDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //씨앗심기
    public int updateUserSeedStatus(int userIdx, int userSeedIdx){
        String updateUserSeedStatusQuery = "UPDATE UserSeed SET status=1 WHERE userIdx=? AND idx=?;";

        Object[] updateUserSeedStatusParams = new Object[]{userIdx, userSeedIdx};
        return this.jdbcTemplate.update(updateUserSeedStatusQuery, updateUserSeedStatusParams);
    }
}
