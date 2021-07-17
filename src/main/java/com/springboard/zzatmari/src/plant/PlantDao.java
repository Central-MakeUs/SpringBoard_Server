package com.springboard.zzatmari.src.plant;

import com.springboard.zzatmari.src.plant.model.GetPlantRes;
import com.springboard.zzatmari.src.user.model.GetUserSeedRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

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

    //키우고있는 식물 조회
    public GetPlantRes selectPlant(int plantIdx){
        String selectPlantQuery = "SELECT E.seedIdx, seedImgUrl,\n" +
                "       TRUNCATE(executionTime/growthTime,0) growthStage,\n" +
                "       executionTime, floweringTime, case when TRUNCATE(executionTime/growthTime,0)=0 then 0 when executionTime=floweringTime then 2 else 1 end status\n" +
                "   FROM (SELECT S.idx seedIdx, seedImgUrl, growthTime,\n" +
                "                 case when TRUNCATE((SUM(case when E.createdAt >= US.updatedAt then min else 0 end)/60),0)>floweringTime then floweringTime else TRUNCATE(SUM(case when E.createdAt >= US.updatedAt then min else 0 end)/60,0) end executionTime,\n" +
                "       floweringTime FROM UserSeed US\n" +
                "           JOIN SeedInfo S ON US.seedIdx=S.idx\n" +
                "           LEFT JOIN List L ON L.userIdx=US.userIdx\n" +
                "        LEFT JOIN Execution E ON E.listIdx=L.idx\n" +
                "        WHERE E.status=2\n" +
                "       AND US.idx=? AND US.status=1) E";
        int selectPlantParams = plantIdx;

        String selectPlantImgQuery = "#해당 단계의 이미지 조회\n" +
                "SELECT plantImgUrl\n" +
                "FROM PlantImg PI\n" +
                "WHERE seedIdx=? AND stage=?";

        GetPlantRes getPlantRes = this.jdbcTemplate.queryForObject(selectPlantQuery,
                (rs,rowNum) -> new GetPlantRes(
                        rs.getInt("seedIdx"),
                        rs.getString("seedImgUrl"),
                        null,
                        rs.getInt("growthStage"),
                        rs.getInt("executionTime"),
                        rs.getInt("floweringTime"),
                        rs.getInt("status")), selectPlantParams
        );

        if(getPlantRes.getGrowthStage()!=0) {

            Object[] selectPlantImgParams = new Object[]{getPlantRes.getSeedIdx(), getPlantRes.getGrowthStage()};
            String getPlantImgRes = this.jdbcTemplate.queryForObject(selectPlantImgQuery,
                    String.class,
                    selectPlantImgParams
            );
            getPlantRes.setPlantImgUrl(getPlantImgRes);
        }

        return getPlantRes;
    }
}
