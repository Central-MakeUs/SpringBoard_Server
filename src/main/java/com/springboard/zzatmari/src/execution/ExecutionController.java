package com.springboard.zzatmari.src.execution;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.config.BaseResponse;
import com.springboard.zzatmari.src.execution.model.GetExecutionRes;
import com.springboard.zzatmari.src.execution.model.PostExecutionStartReq;
import com.springboard.zzatmari.src.execution.model.PostExecutionStartRes;
import com.springboard.zzatmari.src.goal.model.PostGoalReq;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.springboard.zzatmari.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/execution")
public class ExecutionController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ExecutionProvider executionProvider;
    @Autowired
    private final ExecutionService executionService;
    @Autowired
    private final JwtService jwtService;

    public ExecutionController(ExecutionProvider executionProvider, ExecutionService executionService, JwtService jwtService){
        this.executionProvider = executionProvider;
        this.executionService = executionService;
        this.jwtService = jwtService;
    }

    /**
     * 실행 시작 API
     * [POST] /execution
     * @return BaseResponse<>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostExecutionStartRes> startExecution(@RequestBody PostExecutionStartReq postExecutionStartReq) throws BaseException {

        int userIdx = jwtService.getUserIdx();

        //빈값체크
        if(postExecutionStartReq.getListIdx() <= 0)
            return new BaseResponse<>(EXECUTION_LIST_ID_EMPTY);

        if(postExecutionStartReq.getTimerIdx() <= 0)
            return new BaseResponse<>(EXECUTION_TIMER_ID_EMPTY);

        try{
            PostExecutionStartRes response = executionService.startExecution(userIdx, postExecutionStartReq);
            return new BaseResponse<PostExecutionStartRes>(response);

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 실행 조회 API
     * [GET] /execution
     * @return BaseResponse<>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetExecutionRes> getExecution() throws BaseException {

        int userIdx = jwtService.getUserIdx();

        try{
            GetExecutionRes response = executionProvider.getExecution(userIdx);
            return new BaseResponse<GetExecutionRes>(response);

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}

