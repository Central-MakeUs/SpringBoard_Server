package com.springboard.zzatmari.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDeviceId {
    private int count;
    private int userIdx;
    private String deviceId;
}
