package com.springboard.zzatmari.src.plant;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.config.BaseResponse;
import com.springboard.zzatmari.src.plant.model.GetPlantRes;
import com.springboard.zzatmari.src.plant.model.GetPlantsRes;
import com.springboard.zzatmari.src.plant.model.PatchPlantReq;
import com.springboard.zzatmari.src.plant.model.PatchPlantRes;
import com.springboard.zzatmari.src.timer.model.PostTimerRes;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

            int status = 0;
            if(patchPlantReq.getType() == 0)
                status = 1;
            else if(patchPlantReq.getType() == 1)
                status = 2;
            else
                return new BaseResponse<>(SEEDS_TYPE_ERROR_TYPE);

            plantService.plantSeed(userIdx, patchPlantReq.getSeedIdx(), status);
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
    @GetMapping("/growing")
    public BaseResponse<GetPlantRes> getPlant() {

        try{
            int userIdx = jwtService.getUserIdx();
            GetPlantRes getPlantRes = plantProvider.getPlant(userIdx);
            return new BaseResponse<GetPlantRes>(getPlantRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 키운 식물 조회
     * [GET] /plants
     * @return BaseResponse<GetPlantsRes>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetPlantsRes>> getPlants() {

        try{
            int userIdx = jwtService.getUserIdx();

            List<GetPlantsRes> getPlantsRes = plantProvider.getPlants(userIdx);
            return new BaseResponse<List<GetPlantsRes>> (getPlantsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
