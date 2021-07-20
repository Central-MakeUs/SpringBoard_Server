package com.springboard.zzatmari.src.plant;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.src.plant.PlantDao;
import com.springboard.zzatmari.src.plant.PlantProvider;
import com.springboard.zzatmari.src.plant.model.Plant;
import com.springboard.zzatmari.src.user.model.PostUserTimeReq;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.springboard.zzatmari.config.BaseResponseStatus.*;

@Service
public class PlantService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PlantDao plantDao;
    private final PlantProvider plantProvider;
    private final JwtService jwtService;


    @Autowired
    public PlantService(PlantDao plantDao, PlantProvider plantProvider, JwtService jwtService) {
        this.plantDao = plantDao;
        this.plantProvider = plantProvider;
        this.jwtService = jwtService;
    }

    //씨앗심기
    public void plantSeed(int userIdx, int userSeedIdx, int status) throws BaseException {

        Plant plant = plantProvider.checkPlant(userSeedIdx);

        if(plant.getCount() == 0)
            throw new BaseException(PLANTS_NOT_EXIST);
        else{
            //심을수 없는 경우
            if(status == 1 && plant.getStatus() != 0)
                throw new BaseException(PLANTS_CANT_PLANT);

            //수확할 수 없는 경우
            if(status == 2 && plant.getStatus() != 1)
                throw new BaseException(PLANTS_CANT_HARVEST);
        }

        try{
            int result = plantDao.updateUserSeedStatus(userIdx, userSeedIdx, status);

            if(result != 1)
                throw new BaseException(REQUEST_FAIL);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
