package com.springboard.zzatmari.src.plant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPlantRes {
    private int seedIdx;
    private String seedImgUrl;
    private String plantImgUrl;
    private int growthStage;
    private int executionTime;
    private int floweringTime;
    private int status;
}
