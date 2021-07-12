package com.springboard.zzatmari.src.execution.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostExecutionStartReq {
    private int listIdx;
    private int timerIdx;
}
