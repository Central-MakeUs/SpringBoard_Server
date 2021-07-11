package com.springboard.zzatmari.src.timer;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.src.timer.model.PostTimerRes;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.springboard.zzatmari.config.BaseResponseStatus.*;

@Service
public class TimerService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TimerDao timerDao;
    private final TimerProvider timerProvider;
    private final JwtService jwtService;


    @Autowired
    public TimerService(TimerDao timerDao, TimerProvider timerProvider, JwtService jwtService) {
        this.timerDao = timerDao;
        this.timerProvider = timerProvider;
        this.jwtService = jwtService;

    }

    //타이머 추가
    public PostTimerRes createTimer(int userIdx, int time) throws BaseException {

        //타이머 중복체크
        if(timerProvider.checkTimers(userIdx, time) == 1){
            throw new BaseException(TIMERS_EXIST_TIME);
        }

        try{
            int timerIdx = timerDao.insertTimer(userIdx, time);
            return new PostTimerRes(timerIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //타이머 수정
    public PostTimerRes updateTimer(int userIdx, int timerIdx, int time) throws BaseException {

        //타이머 중복체크
        if(timerProvider.checkTimers(userIdx, time) == 1){
            throw new BaseException(TIMERS_EXIST_TIME);
        }

        try{
            timerDao.updateTimer(timerIdx, time);
            return new PostTimerRes(timerIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}