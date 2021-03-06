package com.springboard.zzatmari.src.plant;

import com.springboard.zzatmari.src.plant.model.GetPlantRes;
import com.springboard.zzatmari.src.plant.model.GetPlantsRes;
import com.springboard.zzatmari.src.plant.model.Plant;
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

    //씨앗심기, 씨앗수확
    public int updateUserSeedStatus(int userIdx, int seedIdx, int status){
        String updateUserSeedStatusQuery1 = "UPDATE UserSeed SET status=? WHERE status=? AND userIdx=? AND seedIdx=? LIMIT 1";
        String updateUserSeedStatusQuery2 = "UPDATE UserSeed SET status=? WHERE status=? AND userIdx=? AND idx=? LIMIT 1";

        Object[] updateUserSeedStatusParams = new Object[]{status, status-1, userIdx, seedIdx};
        if(status==1){
            return this.jdbcTemplate.update(updateUserSeedStatusQuery1, updateUserSeedStatusParams);
        }
        else{
            String updateUserSunlightQuery = "UPDATE User SET sunlight = sunlight+(SELECT rewardSunlight FROM SeedInfo S JOIN UserSeed US ON US.seedIdx=S.idx WHERE US.idx=?) WHERE idx=?";
            Object[] updateUserSunlightParams = new Object[]{seedIdx, userIdx};

            this.jdbcTemplate.update(updateUserSunlightQuery, updateUserSunlightParams);
            return this.jdbcTemplate.update(updateUserSeedStatusQuery2, updateUserSeedStatusParams);
        }
    }

    //키우고있는 식물 조회
    public GetPlantRes selectPlant(int userIdx){
        String selectPlantQuery = "SELECT E.plantIdx, E.seedIdx, seedImgUrl,\n" +
                "       TRUNCATE(executionTime/growthTime,0) growthStage,\n" +
                "       executionTime, floweringTime, case when TRUNCATE(executionTime/growthTime,0)=0 then 0 when executionTime=floweringTime then 2 else 1 end status\n" +
                "   FROM (SELECT US.idx plantIdx, S.idx seedIdx, seedImgUrl, growthTime,\n" +
                "                 case when SUM(case when E.createdAt >= US.updatedAt then min else 0 end)>floweringTime then floweringTime else SUM(case when E.createdAt >= US.updatedAt then min else 0 end) end executionTime,\n" +
                "       floweringTime FROM UserSeed US\n" +
                "           JOIN SeedInfo S ON US.seedIdx=S.idx\n" +
                "           LEFT JOIN List L ON L.userIdx=US.userIdx\n" +
                "        LEFT JOIN (SELECT listIdx, createdAt, min FROM Execution E WHERE E.status=2) E ON E.listIdx=L.idx\n" +
                "        WHERE US.userIdx=? AND US.status=1) E";
        int selectPlantParams = userIdx;

        String selectPlantImgQuery =
                "SELECT plantImgUrl\n" +
                "FROM PlantImg PI\n" +
                "WHERE seedIdx=? AND stage=?";

        GetPlantRes getPlantRes = this.jdbcTemplate.queryForObject(selectPlantQuery,
                (rs,rowNum) -> new GetPlantRes(
                        rs.getInt("plantIdx"),
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

    //씨앗 상태확인
    public Plant checkPlant(int plantIdx){
        String checkPlantQuery = "SELECT count(*) count, ifnull(idx,0) plantIdx, ifnull(status,0) status FROM UserSeed WHERE idx=?";
        int checkPlantParams = plantIdx;


        Plant getPlantRes = this.jdbcTemplate.queryForObject(checkPlantQuery,
                (rs,rowNum) -> new Plant(
                        rs.getInt("count"),
                        rs.getInt("plantIdx"),
                        rs.getInt("status")), checkPlantParams
        );

        return getPlantRes;
    }

    //사용자가 씨앗 조회
    public Plant checkUserPlant(int userIdx){
        String checkPlantQuery = "SELECT count(*) count, ifnull(idx,0) plantIdx, ifnull(status,0) status FROM UserSeed WHERE userIdx=? AND status=1";
        Object[] checkPlantParams = new Object[]{userIdx};


        Plant getPlantRes = this.jdbcTemplate.queryForObject(checkPlantQuery,
                (rs,rowNum) -> new Plant(
                        rs.getInt("count"),
                        rs.getInt("plantIdx"),
                        rs.getInt("status")), checkPlantParams
        );

        return getPlantRes;
    }

    //키운씨앗 조회
    public List<GetPlantsRes> selectPlants(int userIdx){
        String selectPlantsQuery = "SELECT plantImgUrl\n" +
                "FROM UserSeed US\n" +
                "JOIN PlantImg PI on US.seedIdx=PI.seedIdx\n" +
                "JOIN SeedInfo S on S.idx=US.seedIdx\n" +
                "WHERE US.status=2 AND S.growthStage=PI.stage AND US.userIdx=?";
        int selectPlantsParams = userIdx;


         return this.jdbcTemplate.query(selectPlantsQuery,
                (rs,rowNum) -> new GetPlantsRes(
                        rs.getString("plantImgUrl")), selectPlantsParams
        );
    }
}
