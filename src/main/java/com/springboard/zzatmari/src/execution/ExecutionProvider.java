package com.springboard.zzatmari.src.execution;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.config.BaseResponse;
import com.springboard.zzatmari.src.execution.model.Execution;
import com.springboard.zzatmari.src.execution.model.GetExecutionRes;
import com.springboard.zzatmari.src.execution.model.PostExecutionStartReq;
import com.springboard.zzatmari.src.execution.model.PostExecutionStartRes;
import com.springboard.zzatmari.src.goal.GoalDao;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.springboard.zzatmari.config.BaseResponseStatus.*;

@Service
public class ExecutionProvider {

    private final ExecutionDao executionDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ExecutionProvider(ExecutionDao executionDao, JwtService jwtService) {
        this.executionDao = executionDao;
        this.jwtService = jwtService;
    }

    //실행 조회
    public GetExecutionRes getExecution(int userIdx, int executionIdx) throws BaseException {

        Execution execution = executionDao.selectExecutionDetail(executionIdx);

        if (execution.getUserIdx() != userIdx)
            throw new BaseException(EXECUTION_USER_NOT_MATCH);

        if (execution.getStatus() != 0 && execution.getStatus() != 1)
            throw new BaseException(EXECUTION_NOT_EXIST);

        try {

            //실행중 상태
            if (execution.getStatus() == 0)
                return executionDao.selectExecution(executionIdx);

            //일시정지 상태
            return executionDao.selectPauseExecution(executionIdx);


        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //실행 정보조회
    public Execution getExecutionDetail(int userIdx) throws BaseException {

        try{
            return executionDao.selectExecutionDetail(userIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
