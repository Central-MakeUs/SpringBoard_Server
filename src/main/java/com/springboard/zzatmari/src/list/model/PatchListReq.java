package com.springboard.zzatmari.src.list.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchListReq {
    private int listIdx;
    private String listItem;
}
