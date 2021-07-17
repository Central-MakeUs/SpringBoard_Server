package com.springboard.zzatmari.src.plant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchPlantReq {
    private int seedIdx;
    private int type;
}
