package com.springboard.zzatmari.src.list.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class PostListReq {
    private String listItem;
    private ArrayList<String> listItems;
    private int listType;
}
