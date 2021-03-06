package com.springboard.zzatmari.src.plant;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.src.plant.PlantDao;
import com.springboard.zzatmari.src.plant.model.GetPlantRes;
import com.springboard.zzatmari.src.plant.model.GetPlantsRes;
import com.springboard.zzatmari.src.plant.model.Plant;
import com.springboard.zzatmari.src.user.model.GetUserSeedRes;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.springboard.zzatmari.config.BaseResponseStatus.*;
import static com.springboard.zzatmari.config.BaseResponseStatus.PLANTS_CANT_HARVEST;

@Service
public class PlantProvider {

    private final PlantDao plantDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PlantProvider(PlantDao plantDao, JwtService jwtService) {
        this.plantDao = plantDao;
        this.jwtService = jwtService;
    }

    //키우고있는 식물 조회
    public GetPlantRes getPlant(int userIdx) throws BaseException {

        Plant plant = checkPlant(userIdx);

        try{
            return plantDao.selectPlant(userIdx);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //식물 상태체크
    public Plant checkPlant(int plantIdx) throws BaseException {

        try{
            return plantDao.checkPlant(plantIdx);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //사용자 씨앗체크
    public Plant checkUserPlant(int userIdx) throws BaseException {

        try{
            return plantDao.checkUserPlant(userIdx);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //키운 식물 조회
    public List<GetPlantsRes> getPlants(int userIdx) throws BaseException {

        try{
            return plantDao.selectPlants(userIdx);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}