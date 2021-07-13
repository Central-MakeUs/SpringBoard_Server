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
    public GetExecutionRes getExecution(int userIdx) throws BaseException {

        int isExist = executionDao.checkExecution(userIdx);

        if(isExist == 0){
            throw new BaseException(EXECUTION_NOT_EXIST);
        }

        try{
            return executionDao.selectExecution(userIdx);

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
