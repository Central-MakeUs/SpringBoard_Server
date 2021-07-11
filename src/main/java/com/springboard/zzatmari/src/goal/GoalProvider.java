package com.springboard.zzatmari.src.goal;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.src.goal.model.GetGoalsRes;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import static com.springboard.zzatmari.config.BaseResponseStatus.*;

@Service
public class GoalProvider {

    private final GoalDao goalDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public GoalProvider(GoalDao goalDao, JwtService jwtService) {
        this.goalDao = goalDao;
        this.jwtService = jwtService;
    }

    //목표 조회
    public GetGoalsRes getGoals(int userIdx) throws BaseException{
        try{
            GetGoalsRes response = goalDao.selectGoals(userIdx);
            return response;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //목표 중복체크
    public int checkGoal(int userIdx, int listIdx) throws BaseException{
        try{
            return goalDao.checkGoal(userIdx, listIdx);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
