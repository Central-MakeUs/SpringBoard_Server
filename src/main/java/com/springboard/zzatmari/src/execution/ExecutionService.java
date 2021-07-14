package com.springboard.zzatmari.src.execution;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.src.execution.model.Execution;
import com.springboard.zzatmari.src.execution.model.PatchExecutionReq;
import com.springboard.zzatmari.src.execution.model.PostExecutionStartReq;
import com.springboard.zzatmari.src.execution.model.PostExecutionStartRes;
import com.springboard.zzatmari.src.goal.GoalDao;
import com.springboard.zzatmari.src.goal.GoalProvider;
import com.springboard.zzatmari.src.goal.model.PostGoalReq;
import com.springboard.zzatmari.src.timer.TimerProvider;
import com.springboard.zzatmari.src.timer.model.PostTimerRes;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.springboard.zzatmari.config.BaseResponseStatus.*;

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

        //실행중인 타이머 확인
        /*int isExist = executionDao.checkExecution(userIdx);
        if(isExist == 1)
            throw new BaseException(EXECUTION_IS_EXIST);*/

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

    //실행 일시정지
    public void pauseExecution(int userIdx, int executionIdx, PatchExecutionReq patchExecutionReq) throws BaseException {

        Execution execution = executionProvider.getExecutionDetail(executionIdx);

        if (execution.getUserIdx() != userIdx)
            throw new BaseException(EXECUTION_USER_NOT_MATCH);

        if(execution.getStatus()!=0)
            throw new BaseException(EXECUTION_NOT_EXIST);

        try{
            //수행시간
            int min = execution.getTimer() - patchExecutionReq.getMin() - 1;
            int sec = 60 - patchExecutionReq.getSec();

            if(sec == 60){
                min += 1;
                sec = 0;
            }

            int result = executionDao.pauseExecution(executionIdx, min, sec);

            if(result == 0)
                throw new BaseException(REQUEST_FAIL);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //실행 재개
    public void continueExecution(int userIdx) throws BaseException {

        Execution execution = executionProvider.getExecutionDetail(userIdx);

        if(execution.getStatus()!=1)
            throw new BaseException(EXECUTION_PAUSE_NOT_EXIST);

        try{

            int result = executionDao.continueExecution(userIdx);

            if(result == 0)
                throw new BaseException(REQUEST_FAIL);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //실행 완료
    public void completeExecution(int userIdx, PatchExecutionReq patchExecutionReq) throws BaseException {

        Execution execution = executionProvider.getExecutionDetail(userIdx);

        if(execution.getStatus()!=0 && execution.getStatus()!=1)
            throw new BaseException(EXECUTION_NOT_EXIST);

        try{
            //수행시간
            int min = execution.getTimer() - patchExecutionReq.getMin() - 1;
            int sec = 60 - patchExecutionReq.getSec();

            if(sec == 60){
                min += 1;
                sec = 0;
            }

            int result = executionDao.completeExecution(userIdx, min, sec);

            if(result == 0)
                throw new BaseException(REQUEST_FAIL);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}