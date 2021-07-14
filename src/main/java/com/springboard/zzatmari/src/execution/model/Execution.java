package com.springboard.zzatmari.src.execution.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Execution {
    private int min;
    private int sec;
    private int status;
    private int timer;
    private int userIdx;
}
