package com.springboard.zzatmari.src.seed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SeedStore {
    private int seedIdx;
    private String seedName;
    private String seedImgUrl;
    private int sunlight;
}
