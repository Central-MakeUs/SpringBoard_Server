package com.springboard.zzatmari.src.seed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetSeedsRes {
    private int mySunlight;
    private List<SeedStore> seedList;
}
