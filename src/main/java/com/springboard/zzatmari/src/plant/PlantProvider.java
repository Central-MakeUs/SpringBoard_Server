package com.springboard.zzatmari.src.plant;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.src.plant.PlantDao;
import com.springboard.zzatmari.src.plant.model.GetPlantRes;
import com.springboard.zzatmari.src.user.model.GetUserSeedRes;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.springboard.zzatmari.config.BaseResponseStatus.DATABASE_ERROR;

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
    public GetPlantRes getPlant(int plantIdx) throws BaseException {
        try{
            return plantDao.selectPlant(plantIdx);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}