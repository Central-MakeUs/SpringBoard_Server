package com.springboard.zzatmari.src.stat;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.config.BaseResponse;
import com.springboard.zzatmari.src.stat.model.GetStatsRes;
import com.springboard.zzatmari.src.user.UserProvider;
import com.springboard.zzatmari.src.user.UserService;
import com.springboard.zzatmari.src.user.model.GetUserRes;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.springboard.zzatmari.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/stats")
public class StatController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final StatProvider statProvider;
    @Autowired
    private final JwtService jwtService;


    public StatController(StatProvider statProvider, JwtService jwtService) {
        this.statProvider = statProvider;
        this.jwtService = jwtService;
    }

    /**
     * 통계 달력 조회
     * [GET] /stats?month=?
     * @return BaseResponse<List<GetUserRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetStatsRes>> getStats(@RequestParam(required = false, defaultValue = "0") String month, @RequestParam(required = false, defaultValue = "0") String year) {

        try{

            int userIdx = jwtService.getUserIdx();
            int m = Integer.parseInt(month);
            int y = Integer.parseInt(year);

            if(m <= 0 || m > 12){
                return new BaseResponse<>(STATS_MONTH_ERROR_TYPE);
            }
            if(y < 2000 || y > 2100){
                return new BaseResponse<>(STATS_YEAR_ERROR_TYPE);
            }

            List<GetStatsRes> getStatsRes = statProvider.getStats(userIdx, y, m);
            return new BaseResponse<>(getStatsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}