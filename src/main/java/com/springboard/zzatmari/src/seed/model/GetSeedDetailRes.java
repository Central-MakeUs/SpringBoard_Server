package com.springboard.zzatmari.src.seed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetSeedDetailRes {
    private int seedIdx;
    private String seedName;
    private String seedImgUrl;
    private int sunlight;
    private int floweringTime;
    private int growthTime;
    private int reward;
    private int quantity;
}
