package com.springboard.zzatmari.src.goal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchGoalResetReq {
    private int isReset;
    private int userIdx;
}
