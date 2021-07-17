package com.springboard.zzatmari.src.seed;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.src.seed.model.GetSeedsRes;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.springboard.zzatmari.config.BaseResponseStatus.*;

@Service
public class SeedService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SeedDao seedDao;
    private final SeedProvider seedProvider;
    private final JwtService jwtService;


    @Autowired
    public SeedService(SeedDao seedDao, SeedProvider seedProvider, JwtService jwtService) {
        this.seedDao = seedDao;
        this.seedProvider = seedProvider;
        this.jwtService = jwtService;

    }

    //Todo: Add Transaction
    //씨앗 구매
    public void purchaseSeed(int userIdx, int seedIdx) throws BaseException {

        //씨앗가격 조회
        int seedSunlight = seedProvider.getSeedSunlight(seedIdx);

        //사용자 햇살 조회
        int userSunlight = seedProvider.getUserSunlight(userIdx);

        if(seedSunlight > userSunlight)
            throw new BaseException(USER_SUNLIGHT_NOT_ENOUGH);

        System.out.println(seedSunlight);
        System.out.println(userSunlight);

        try{
            int result1 = seedDao.insertUserSeed(userIdx, seedIdx);
            int result2 = seedDao.updateUserSunlight(userIdx, userSunlight-seedSunlight);

            if(result1 != 1 || result2 !=1)
                throw new BaseException(REQUEST_FAIL);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}