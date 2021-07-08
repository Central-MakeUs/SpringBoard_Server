package com.springboard.zzatmari.src.timer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetTimersRes {
    private int timerIdx;
    private int hour;
    private int minute;
    private String time;
}
