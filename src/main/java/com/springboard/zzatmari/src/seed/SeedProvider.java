package com.springboard.zzatmari.src.seed;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.src.seed.model.GetSeedDetailRes;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public GetSeedDetailRes getSeedDetail(int userIdx, int seedIdx, int type) throws BaseException {
        try {

            GetSeedDetailRes response = seedDao.selectSeedDetail(userIdx, seedIdx, type);
            return response;

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}