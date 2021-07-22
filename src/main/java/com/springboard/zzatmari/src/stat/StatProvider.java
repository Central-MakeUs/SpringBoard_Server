package com.springboard.zzatmari.src.stat;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.src.stat.model.GetStatsListRes;
import com.springboard.zzatmari.src.stat.model.GetStatsRes;
import com.springboard.zzatmari.src.user.UserDao;
import com.springboard.zzatmari.src.user.model.GetUserRes;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.springboard.zzatmari.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class StatProvider {

    private final StatDao statDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public StatProvider(StatDao statDao, JwtService jwtService) {
        this.statDao = statDao;
        this.jwtService = jwtService;
    }
    //통계 달력 조회
    public GetStatsRes getStats(int userIdx, int year, int month) throws BaseException {
        try{
            return statDao.selectStats(userIdx, year, month);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //통계 리스트 조회
    public GetStatsListRes getStatsList(int userIdx, String type, int year, int month, int day) throws BaseException {
        try{
            return statDao.selectStatsList(userIdx, type, year, month, day);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}