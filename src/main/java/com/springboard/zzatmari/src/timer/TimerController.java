package com.springboard.zzatmari.src.timer;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.config.BaseResponse;
import com.springboard.zzatmari.src.timer.model.GetTimersRes;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

}


