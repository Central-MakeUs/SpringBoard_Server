package com.springboard.zzatmari.src.timer;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.config.BaseResponse;
import com.springboard.zzatmari.src.timer.model.GetTimersRes;
import com.springboard.zzatmari.src.timer.model.PostTimerReq;
import com.springboard.zzatmari.src.timer.model.PostTimerRes;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.springboard.zzatmari.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/timers")
public class TimerController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final TimerProvider timerProvider;
    @Autowired
    private final TimerService timerService;
    @Autowired
    private final JwtService jwtService;

    public TimerController(TimerProvider timerProvider, TimerService timerService, JwtService jwtService){
        this.timerProvider = timerProvider;
        this.timerService = timerService;
        this.jwtService = jwtService;
    }

    /**
     * 타이머 조회 API
     * [GET] /timers
     * @return BaseResponse<List<GetTimersRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetTimersRes>> getTimers() {
        try{
            int userIdx = jwtService.getUserIdx();

            List<GetTimersRes> response = timerProvider.getTimers(userIdx);
            return new BaseResponse<>(response);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 타이머 추가 API
     * [POST] /timers
     * @return BaseResponse<PostTimerRes>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostTimerRes> createTimers(@RequestBody PostTimerReq postTimerReq) throws BaseException {

        int userIdx = jwtService.getUserIdx();

        //빈값 체크
        if(postTimerReq.getHour() == 0 && postTimerReq.getMinute() == 0){
            return new BaseResponse<>(TIMERS_TIME_EMPTY);
        }

        //형식 체크
        if(postTimerReq.getHour() < 0 || postTimerReq.getHour() >= 24 || postTimerReq.getMinute() < 0 || postTimerReq.getMinute() >= 60){
            return new BaseResponse<>(TIMERS_TIME_ERROR_TYPE);
        }

        //시간 + 분 -> 분 변환
        int time = postTimerReq.getHour() * 60 + postTimerReq.getMinute();

        try{
            PostTimerRes response = timerService.createTimer(userIdx, time);
            return new BaseResponse<PostTimerRes>(response);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}


