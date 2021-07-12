package com.springboard.zzatmari.src.timer;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.src.timer.model.GetTimersRes;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.springboard.zzatmari.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class TimerProvider{

    private final TimerDao timerDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public TimerProvider(TimerDao timerDao, JwtService jwtService) {
        this.timerDao = timerDao;
        this.jwtService = jwtService;
    }

    //타이머 전체조회
    public List<GetTimersRes> getTimers(int userIdx) throws BaseException {
        try {

            List<GetTimersRes> response = timerDao.selectTimers(userIdx);
            return response;

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //타이머 중복체크
    public int checkTimers(int userIdx, int time) throws BaseException {
        try {
            return timerDao.checkTimer(userIdx, time);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //타이머 1개 시간조회
    public int getTimer(int timerIdx) throws BaseException {
        try {
            return timerDao.selectTimer(timerIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
