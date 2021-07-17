package com.springboard.zzatmari.src.seed;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.config.BaseResponse;
import com.springboard.zzatmari.src.seed.model.GetSeedDetailRes;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.springboard.zzatmari.config.BaseResponseStatus.SEEDS_ID_EMPTY;

@RestController
@RequestMapping("/seeds")
public class SeedController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final SeedProvider seedProvider;
    @Autowired
    private final SeedService seedService;
    @Autowired
    private final JwtService jwtService;

    public SeedController(SeedProvider seedProvider, SeedService seedService, JwtService jwtService) {
        this.seedProvider = seedProvider;
        this.seedService = seedService;
        this.jwtService = jwtService;
    }

    /**
     * 씨앗정보 조회
     * [GET] /seeds/:seedIdx
     * @return BaseResponse<GetSeedDetailRes>
     */
    @ResponseBody
    @GetMapping("/{seedIdx}")
    public BaseResponse<GetSeedDetailRes> getSeedDetail(@PathVariable int seedIdx) {
        try{
            int userIdx = jwtService.getUserIdx();

            if(seedIdx <= 0){
                return new BaseResponse<>(SEEDS_ID_EMPTY);
            }

            GetSeedDetailRes response = seedProvider.getSeedDetail(userIdx, seedIdx);
            return new BaseResponse<>(response);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
