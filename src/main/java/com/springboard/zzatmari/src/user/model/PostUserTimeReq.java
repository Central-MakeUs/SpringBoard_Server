package com.springboard.zzatmari.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserTimeReq {
    private int hour;
    private int minute;
}
