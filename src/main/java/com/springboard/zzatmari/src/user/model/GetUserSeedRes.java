package com.springboard.zzatmari.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserSeedRes {
    private int seedIdx;
    private String seedName;
    private String seedImgUrl;
}
