package com.springboard.zzatmari.src.goal;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.src.goal.model.PostGoalReq;
import com.springboard.zzatmari.src.list.model.PostListReq;
import com.springboard.zzatmari.src.list.model.PostListRes;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.springboard.zzatmari.config.BaseResponseStatus.*;

@Service
public class GoalService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GoalDao goalDao;
    private final GoalProvider goalProvider;
    private final JwtService jwtService;


    @Autowired
    public GoalService(GoalDao goalDao, GoalProvider goalProvider, JwtService jwtService) {
        this.goalDao = goalDao;
        this.goalProvider = goalProvider;
        this.jwtService = jwtService;

    }

    //목표 등록
    public void createGoals(int userIdx, PostGoalReq postGoalReq) throws BaseException {

        //목표 중복체크
        int isExist = goalProvider.checkGoal(userIdx, postGoalReq.getListIdx());

        try{

            if(isExist == 1){
                //이미 존재하면 업데이트
                int result = goalDao.updateGoal(userIdx, postGoalReq);
                if(result == 0)
                    throw new BaseException(REQUEST_FAIL);
            }
            else{
                //추가
                int result = goalDao.insertGoal(userIdx, postGoalReq);
                if(result == 0)
                    throw new BaseException(REQUEST_FAIL);
            }

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //목표 초기화
    public void resetGoals(int userIdx) throws BaseException {

        try{
                int result = goalDao.resetGoals(userIdx);
                if(result == 0)
                    throw new BaseException(REQUEST_FAIL);


        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}

