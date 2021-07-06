package com.springboard.zzatmari.src.goal;

import com.springboard.zzatmari.src.goal.model.GetGoalsRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.config.BaseResponse;
import com.springboard.zzatmari.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goals")
public class GoalController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final GoalProvider goalProvider;
    @Autowired
    private final GoalService goalService;
    @Autowired
    private final JwtService jwtService;

    public GoalController(GoalProvider goalProvider, GoalService goalService, JwtService jwtService){
        this.goalProvider = goalProvider;
        this.goalService = goalService;
        this.jwtService = jwtService;
    }

    /**
     * 목표 조회 API
     * [GET] /goals
     * @return BaseResponse<GetGoalsRes>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetGoalsRes> getGoals() {
        try{
            int userIdx = jwtService.getUserIdx();
            GetGoalsRes response = goalProvider.getGoals(userIdx);
            return new BaseResponse<>(response);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}


