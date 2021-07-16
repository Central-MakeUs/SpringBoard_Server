package com.springboard.zzatmari.src.stat.model;

import com.springboard.zzatmari.src.list.model.GetListsRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetStatsListRes {
    private int digitalDetoxChart;
    private int selfDevelopmentChart;
    private int digitalDetoxTime;
    private int selfDevelopmentTime;
    private List<GetListsRes> digitalDetox;
    private List<GetListsRes> selfDevelopment;
}
