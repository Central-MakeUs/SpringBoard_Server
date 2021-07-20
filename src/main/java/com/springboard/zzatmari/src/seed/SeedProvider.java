package com.springboard.zzatmari.src.seed;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.src.seed.model.GetSeedDetailRes;
import com.springboard.zzatmari.src.seed.model.GetSeedsRes;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.springboard.zzatmari.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class SeedProvider {

    private final SeedDao seedDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SeedProvider(SeedDao seedDao, JwtService jwtService) {
        this.seedDao = seedDao;
        this.jwtService = jwtService;
    }

    //씨앗정보 조회
    public GetSeedDetailRes getSeedDetail(int userIdx, int seedIdx) throws BaseException {
        try {
            return seedDao.selectSeedDetail(userIdx, seedIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //씨앗상점 조회
    public GetSeedsRes getSeeds(int userIdx) throws BaseException {
        try {

            return seedDao.selectSeeds(userIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //씨앗 가격 조회
    public int getSeedSunlight(int seedIdx) throws BaseException {
        try {
            return seedDao.selectSeedSunlight(seedIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //사용자 햇살 조회
    public int getUserSunlight(int userIdx) throws BaseException {
        try {
            return seedDao.selectUserSunlight(userIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}