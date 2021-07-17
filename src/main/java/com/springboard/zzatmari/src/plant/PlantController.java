package com.springboard.zzatmari.src.plant;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.config.BaseResponse;
import com.springboard.zzatmari.src.plant.model.GetPlantRes;
import com.springboard.zzatmari.src.plant.model.PatchPlantReq;
import com.springboard.zzatmari.src.plant.model.PatchPlantRes;
import com.springboard.zzatmari.src.timer.model.PostTimerRes;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.springboard.zzatmari.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/plants")
public class PlantController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PlantProvider plantProvider;
    @Autowired
    private final PlantService plantService;
    @Autowired
    private final JwtService jwtService;

    public PlantController(PlantProvider plantProvider, PlantService plantService, JwtService jwtService) {
        this.plantProvider = plantProvider;
        this.plantService = plantService;
        this.jwtService = jwtService;
    }

    /**
     * 씨앗심기
     * [PATCH] /plants
     * @return BaseResponse<PatchPlantRes>
     */
    @ResponseBody
    @PatchMapping("")
    public BaseResponse<PatchPlantRes> plantSeed(@RequestBody PatchPlantReq patchPlantReq) {

        try{
            int userIdx = jwtService.getUserIdx();

            //빈값 체크
            if(patchPlantReq.getSeedIdx() <= 0){
                return new BaseResponse<>(SEEDS_ID_EMPTY);
            }

            plantService.plantSeed(userIdx, patchPlantReq.getSeedIdx());
            PatchPlantRes patchPlantRes = new PatchPlantRes(patchPlantReq.getSeedIdx());
            return new BaseResponse<PatchPlantRes>(patchPlantRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 키우고있는 식물 조회
     * [GET] /plants/:plantIdx
     * @return BaseResponse<GetPlantRes>
     */
    @ResponseBody
    @GetMapping("{plantIdx}")
    public BaseResponse<GetPlantRes> getPlant(@PathVariable int plantIdx) {

        try{
            int userIdx = jwtService.getUserIdx();

            //빈값 체크
            if(plantIdx <= 0){
                return new BaseResponse<>(PLANTS_ID_EMPTY);
            }

            GetPlantRes getPlantRes = plantProvider.getPlant(plantIdx);
            return new BaseResponse<GetPlantRes>(getPlantRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
