package com.springboard.zzatmari.src.execution;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.src.execution.model.PostExecutionStartReq;
import com.springboard.zzatmari.src.execution.model.PostExecutionStartRes;
import com.springboard.zzatmari.src.goal.GoalDao;
import com.springboard.zzatmari.src.goal.GoalProvider;
import com.springboard.zzatmari.src.goal.model.PostGoalReq;
import com.springboard.zzatmari.src.timer.TimerProvider;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.springboard.zzatmari.config.BaseResponseStatus.DATABASE_ERROR;
import static com.springboard.zzatmari.config.BaseResponseStatus.REQUEST_FAIL;

@Service
public class ExecutionService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ExecutionDao executionDao;
    private final ExecutionProvider executionProvider;
    private final TimerProvider timerProvider;
    private final JwtService jwtService;


    @Autowired
    public ExecutionService(ExecutionDao executionDao, ExecutionProvider executionProvider, TimerProvider timerProvider, JwtService jwtService) {
        this.executionDao = executionDao;
        this.executionProvider = executionProvider;
        this.timerProvider = timerProvider;
        this.jwtService = jwtService;

    }

    //실행 시작
    public PostExecutionStartRes startExecution(int userIdx, PostExecutionStartReq postExecutionStartReq) throws BaseException {

        //타이머 확인
        int time = timerProvider.getTimer(postExecutionStartReq.getTimerIdx());

        try{
            //추가
            int executionIdx = executionDao.insertExecution(userIdx, postExecutionStartReq.getListIdx(), time);
            return new PostExecutionStartRes(executionIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}