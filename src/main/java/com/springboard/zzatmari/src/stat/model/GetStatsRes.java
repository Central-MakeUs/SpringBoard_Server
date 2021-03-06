package com.springboard.zzatmari.src.stat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetStatsRes {
    private int continuousDay;
    private List<Stat> statList;
}
